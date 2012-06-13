package othello;

import java.awt.EventQueue;

/***
 * A model class for othello client
 * 
 * @author \@kanosaki
 */
public class OthelloClient {

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
