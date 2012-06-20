package othello;

import java.awt.EventQueue;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

/***
 * A model class for othello client
 * 
 * @author \@kanosaki
 */
public class OthelloClient extends Model{
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
		_game = this.createGame();
		getController().setGame(_game);
		_remote = this.createAdapter();
		getController().setRemoteAdapter(_remote);
	}

	public void start() {
		Debug.getInstance().showFrame();
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

	protected Controller createController() {
		return new Controller();
	}

	protected Player createPlayer() {
		return new UserPlayer(this.getController(), this.getFrame().getBoardView());
	}

	protected Game createGame() {
		return new Game(this.createPlayer());
	}

	protected RemoteAdapter createAdapter() {
		RemoteAdapter adapter = new RemoteAdapter();
		adapter.addMessageListener(new Listener<Command>() {
			@Override
			public void next(Command val) {
				val.received(getController());
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

	public static void main(String[] args) {
		try {
			Debug.initialize();
			OthelloClient client = args.length == 2 ? new OthelloClient(args[1], Integer.parseInt(args[0]))
					: new OthelloClient(DEFAULT_HOST, DEFAULT_PORT);
			client.start();

			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					getClassLogger().info("Starting AI Client...");
					AI ai = new AI(DEFAULT_HOST, DEFAULT_PORT);
					ai.start();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		public AI(String serverHost, int serverPort) {
			super(serverHost, serverPort);
			this.getController().showMessage("AI Mode");
		}

		@Override
		protected Player createPlayer() {
			return AIPlayer.create(getController(), AIPlayer.Strategy.Simple);
		}
	}
}
