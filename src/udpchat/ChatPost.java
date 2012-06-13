package udpchat;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class ChatPost extends JPanel {

	/**
	 * Create the panel.
	 */
	public ChatPost(ChatMessage msg) {
		setLayout(null);
		
		JLabel fomLabel = new JLabel(msg.getFromAddr().getHostAddress().toString());
		fomLabel.setBounds(0, 0, 100, 24);
		add(fomLabel);
		
		JLabel messageLabel = new JLabel(msg.getMessage());
		messageLabel.setBounds(112, 0, 338, 24);
		add(messageLabel);

	}

}
