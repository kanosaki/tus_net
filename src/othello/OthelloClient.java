package othello;

import java.io.IOException;

/***
 * A model class for othello client
 * 
 * @author \@kanosaki
 */
public class OthelloClient extends Model {
    public static final String DEFAULT_HOST = "localhost";
    public static final int DEFAULT_PORT = 9876;

    private MainFrame _mainFrame;
    private Controller _controller;
    private Game _game;
    private RemoteAdapter _remote;
    private String _remoteHost;
    private int _remotePort;

    public OthelloClient(String serverHost, int serverPort) {
        getLog().info("Client is starting...");
        setController(this.createController());
        setRemoteHost(serverHost);
        setRemotePort(serverPort);
        _mainFrame = new MainFrame(getController());
        getController().setMainFrame(_mainFrame);
    }

    public void start() {
        _game = this.createGame();
        getController().setGame(_game);
        _remote = this.createAdapter();
        getController().setRemoteAdapter(_remote);
        _mainFrame.setVisible(true);
        this.connect(getRemoteHost(), getRemotePort());
    }

    public void connect(String host, int port) {
        try {
            _remote.start(host, port);
            getLog().info("Connected to " + getRemoteHost());
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    protected Game newGame(RemoteAdapter adapter) {

        return null;
    }

    protected Controller createController() {
        return new Controller();
    }

    protected Player createPlayer() {
        return new UserPlayer(this.getController(), this.getFrame().getBoardView());
    }

    protected Game createGame() {
        return new Game(createPlayer());
    }

    protected RemoteAdapter createAdapter() {
        RemoteAdapter adapter = new RemoteAdapter();
        adapter.addMessageListener(new Listener<Command>() {
            @Override
            public void next(Command val) {
                val.receivedClient(getController());
            }
        });
        return adapter;
    }

    protected MainFrame getFrame() {
        return _mainFrame;
    }

    protected Controller getController() {
        return _controller;
    }

    protected String getRemoteHost() {
        return _remoteHost;
    }

    protected void setRemoteHost(String remoteHost) {
        _remoteHost = remoteHost;
    }

    protected int getRemotePort() {
        return _remotePort;
    }

    protected void setRemotePort(int remotePort) {
        _remotePort = remotePort;
    }

    private void setController(Controller controller) {
        _controller = controller;
    }

    public static class AI extends OthelloClient {
        private Player _player;

        public AI(String serverHost, int serverPort, AIPlayer.Strategy strategy) {
            super(serverHost, serverPort);
            _player = AIPlayer.create(getController(), strategy);
            this.getController().showMessage("AI Mode");
        }

        @Override
        protected Player createPlayer() {
            return _player;
        }
    }
}
