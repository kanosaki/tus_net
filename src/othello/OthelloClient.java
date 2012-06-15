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
public class OthelloClient {
	public static final String DEFAULT_HOST = "localhost";
	public static final int DEFAULT_PORT = 9876;

	private static final Logger log = Logger.getLogger("Client");
	private MainFrame _mainFrame;
	private Controller _controller;
	private Game _game;
	private RemoteAdapter _remote;
	private String _remoteHost;
	private int _remotePort;

	public OthelloClient(String serverHost, int serverPort) {
		_controller = this.createController();
		_remoteHost = serverHost;
		_remotePort = serverPort;
		_mainFrame = new MainFrame();
		_game = this.createUserGame();
		_controller.setGame(_game);
		_remote = this.createAdapter();
		_controller.setRemoteAdapter(_remote);
	}

	public void start() {
		Debug.getInstance().showFrame();
		_mainFrame.setVisible(true);
		log.info("Client started.");
	}

	protected Controller createController() {
		return new Controller();
	}

	protected Game createUserGame() {
		Player player = new UserPlayer(this.getController(), this.getFrame().getBoardView());
		return new Game(player);
	}

	protected RemoteAdapter createAdapter() {
		try {
			return new RemoteAdapter(_remoteHost, _remotePort);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(-1);
		return null;
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
