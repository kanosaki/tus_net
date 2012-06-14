package othello;

import java.awt.EventQueue;
import java.util.logging.Logger;

/***
 * A model class for othello client
 * 
 * @author \@kanosaki
 */
public class OthelloClient {
	private static final Logger log = Logger.getLogger("Client");
	private RemoteAdapter _adapter;
	public OthelloClient() {
	}
	
	public void start() {
		this.initDebugConsole();
		this.initFrame();	
		log.info("Client started.");
	}

	private void initDebugConsole() {
		Debug dbg = Debug.getInstance();
		dbg.showFrame();
	}

	private void initFrame() {
		MainFrame frame = new MainFrame();
		frame.setVisible(true);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					OthelloClient client = new OthelloClient();
					client.start();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
