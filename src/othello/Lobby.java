package othello;

import java.io.IOException;
import java.net.Socket;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import othello.Board.CellState;

public class Lobby extends Model {
    private int _lobbyID;
    private ConcurrentHashMap<RemoteAdapter, ServerPlayer> _playerMap;
    private Board _board;
    private CyclicIterator<ServerPlayer> _currentPlayer;

    public Lobby(int lobbyID) {
        _lobbyID = lobbyID;
        _playerMap = new ConcurrentHashMap<RemoteAdapter, ServerPlayer>(2, 1f);
        setLoggerName(String.format("Lobby%2d", _lobbyID));
    }

    public void addConnection(Socket sock) {
        RemoteAdapter adapter = new RemoteAdapter();
        try {
            adapter.start(sock);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        adapter.addMessageListener(new Listener<Command>() {
            @Override
            public void next(Command val) {
                val.receivedServer(Lobby.this);
            }
        });
        adapter.addOnConnectionClosing(new Listener<RemoteAdapter>() {
            @Override
            public void next(RemoteAdapter val) {
                onClose(val);
            }
        });
        ServerPlayer player = new ServerPlayer(this, adapter);
        _playerMap.put(adapter, player);
    }

    public Iterable<ServerPlayer> getPlayers() {
        return _playerMap.values();
    }

    public void startGame() {
        getLog().info("Game started " + _lobbyID);
        _board = new Board();
        _board.setAutoFlip(true);
        Enumeration<ServerPlayer> players = _playerMap.elements();
        players.nextElement().tellStart(1);
        players.nextElement().tellStart(-1);
        _currentPlayer = new CyclicIterator<ServerPlayer>(getPlayers());
        onBoardUpdated();
        turnChange();
    }

    public boolean isReady() {
        return _playerMap.size() == 2;
    }

    private ServerPlayer getPlayer(RemoteAdapter sender) {
        return _playerMap.get(sender);
    }

    public void setNickName(RemoteAdapter sender, String name) {
        ServerPlayer player = getPlayer(sender);
        player.setName(name);
    }

    public void said(RemoteAdapter sender, String message) {
        ServerPlayer player = getPlayer(sender);
        onSaid(player, message);
    }

    public void error() {
        // TODO Auto-generated method stub

    }

    protected void onBoardUpdated() {
        for (ServerPlayer player : getPlayers()) {
            player.tellBoardUpdated(_board);
        }
    }

    protected void onClose(RemoteAdapter closing) {
        for (ServerPlayer player : getPlayers()) {
            RemoteAdapter adapter = player.getAdapter();
            if (adapter != closing) {
                player.tellClosing();
            }
        }
    }

    protected void onSaid(ServerPlayer src, String message) {
        for (ServerPlayer player : getPlayers()) {
            player.tellSaid(src, message);
        }
    }

    protected void turnChange() {
        ServerPlayer nextPlayer = _currentPlayer.next();
        if(!_board.canPutAny(nextPlayer.getColor())) {
            getLog().info(String.format("%s can put nowhere!", nextPlayer.getName()));
            turnChange();
        }
        for (ServerPlayer player : getPlayers()) {
            player.tellTurn(nextPlayer.getCode());
        }
    }

    private ServerPlayer getBlack() {
        for (ServerPlayer player : getPlayers()) {
            if (player.getColor() == CellState.Black) {
                return player;
            }
        }
        throw new AssertionError();
    }

    private ServerPlayer getWhite() {
        for (ServerPlayer player : getPlayers()) {
            if (player.getColor() == CellState.White) {
                return player;
            }
        }
        throw new AssertionError();
    }

    protected void onEnd() {
        String result = _board.getResultString(getBlack().getName(), getWhite().getName());
        for (ServerPlayer player : getPlayers()) {
            player.tellEnd(result);
        }
    }

    public void put(RemoteAdapter sender, int x, int y) {
        ServerPlayer player = getPlayer(sender);
        if (!player.equals(_currentPlayer.peek())) {
            player.tellError(3); // Invalid turn
            return;
        }
        if (_board.canPut(x, y, player.getColor())) {
            _board.put(x, y, player.getColor());
        } else {
            player.tellError(2); // Invalid position
            return;
        }
        if (_board.isFilled()) {
            onEnd();
            return;
        }
        onBoardUpdated();
        turnChange();
    }
}
