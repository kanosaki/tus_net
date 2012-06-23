package othello;

public class ServerPlayer extends Player {
    private Lobby _lobby;
    private RemoteAdapter _adapter;
    String _name;

    public ServerPlayer(Lobby lobby, RemoteAdapter adapter) {
        _lobby = lobby;
        _adapter = adapter;
    }

    @Override
    public void playTurn(Board board, Game game) {

    }

    @Override
    public void onStart(Game game) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onFinish(Game game) {
        // TODO Auto-generated method stub

    }

    public RemoteAdapter getAdapter() {
        return _adapter;
    }

    public void setName(String name) {
        _name = name;
    }

    public String getName() {
        if (_name != null)
            return _name;
        else {
            switch (getColor()) {
            case Black:
                return "BLACK";
            case White:
                return "WHITE";
            default:
                throw new AssertionError();
            }
        }
    }

    public void tellStart(int i) {
        setColor(i);
        Command start = new Command.START(i);
        _adapter.send(start);
    }

    public void tellError(int code) {
        Command com = new Command.PUT_ERROR(code);
        _adapter.send(com);
    }

    public void tellTurn(int code) {
        Command com = new Command.TURN(code);
        _adapter.send(com);
    }

    public void tellBoardUpdated(Board board) {
        Command com = new Command.BOARD(board);
        _adapter.send(com);
    }

    public void tellSaid(ServerPlayer src, String message) {
        Command com = new Command.SAY.SAY_RECV(src.getName(), message, null);
        _adapter.send(com);
    }

    public void tellClosing() {
        Command com = new Command.CLOSE();
        _adapter.send(com);
        _adapter.close();
    }

    public void tellEnd(String resultString) {
        Command com = new Command.END(resultString);
        _adapter.send(com);
    }
}
