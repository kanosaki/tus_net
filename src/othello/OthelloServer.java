package othello;

import java.io.IOException;
import java.util.Vector;

public class OthelloServer extends Model {
    int _port;
    ServerDaemon _daemon;
    Vector<Lobby> _lobbies;

    public OthelloServer(int bindPort) {
        _port = bindPort;
        _lobbies = new Vector<Lobby>(1, 10);
    }

    public void start() throws IOException {
        _daemon = new ServerDaemon(_port);
        _daemon.addOnLobbyMatched(new Listener<Lobby>() {
            @Override
            public void next(Lobby val) {
                _lobbies.add(val);
                val.startGame();
            }
        });
        _daemon.start();
        getLog().info("Server started");
    }
}
