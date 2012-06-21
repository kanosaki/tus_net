package othello;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Main {

	private JFrame _frame;
	private final JPanel panel_1 = new JPanel();
	private JTextField _bindPortText;
	private JTextField _serverHostText;
	private JTextField _serverPortText;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window._frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		_frame = new JFrame();
		BorderLayout borderLayout = (BorderLayout) _frame.getContentPane().getLayout();
		borderLayout.setVgap(3);
		borderLayout.setHgap(3);
		_frame.setBounds(100, 100, 450, 300);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel mainWrap = new JPanel();
		_frame.getContentPane().add(mainWrap, BorderLayout.CENTER);
		mainWrap.setLayout(new GridLayout(0, 1, 3, 3));
		
		JPanel clientWrap = new JPanel();
		mainWrap.add(clientWrap);
		GridBagLayout gbl_clientWrap = new GridBagLayout();
		gbl_clientWrap.columnWidths = new int[]{0, 0, 0, 0};
		gbl_clientWrap.rowHeights = new int[]{0, 0, 0, 0, 0, 0};
		gbl_clientWrap.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_clientWrap.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		clientWrap.setLayout(gbl_clientWrap);
		
		JLabel lblNewLabel = new JLabel("Client");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(5, 5, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		clientWrap.add(lblNewLabel, gbc_lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Server Host");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 1;
		clientWrap.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		_serverHostText = new JTextField();
		GridBagConstraints gbc_serverHostText = new GridBagConstraints();
		gbc_serverHostText.insets = new Insets(0, 0, 5, 0);
		gbc_serverHostText.fill = GridBagConstraints.HORIZONTAL;
		gbc_serverHostText.gridx = 2;
		gbc_serverHostText.gridy = 1;
		clientWrap.add(_serverHostText, gbc_serverHostText);
		_serverHostText.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("Server Port");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 1;
		gbc_lblNewLabel_2.gridy = 2;
		clientWrap.add(lblNewLabel_2, gbc_lblNewLabel_2);
		
		_serverPortText = new JTextField();
		GridBagConstraints gbc_bindPortText = new GridBagConstraints();
		gbc_bindPortText.insets = new Insets(0, 0, 5, 0);
		gbc_bindPortText.fill = GridBagConstraints.HORIZONTAL;
		gbc_bindPortText.gridx = 2;
		gbc_bindPortText.gridy = 2;
		clientWrap.add(_serverPortText, gbc_bindPortText);
		_serverPortText.setColumns(10);
		
		JLabel lblPlayer = new JLabel("Player");
		GridBagConstraints gbc_lblPlayer = new GridBagConstraints();
		gbc_lblPlayer.anchor = GridBagConstraints.EAST;
		gbc_lblPlayer.insets = new Insets(0, 0, 5, 5);
		gbc_lblPlayer.gridx = 1;
		gbc_lblPlayer.gridy = 3;
		clientWrap.add(lblPlayer, gbc_lblPlayer);
		
		JComboBox comboBox = new JComboBox();
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 2;
		gbc_comboBox.gridy = 3;
		clientWrap.add(comboBox, gbc_comboBox);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.anchor = GridBagConstraints.EAST;
		gbc_btnConnect.gridx = 2;
		gbc_btnConnect.gridy = 4;
		clientWrap.add(btnConnect, gbc_btnConnect);
		
		JPanel serverWrap = new JPanel();
		mainWrap.add(serverWrap);
		GridBagLayout gbl_serverWrap = new GridBagLayout();
		gbl_serverWrap.columnWidths = new int[]{0, 0, 0, 0};
		gbl_serverWrap.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_serverWrap.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_serverWrap.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		serverWrap.setLayout(gbl_serverWrap);
		
		JLabel lblServer = new JLabel("Server");
		GridBagConstraints gbc_lblServer = new GridBagConstraints();
		gbc_lblServer.insets = new Insets(5, 5, 5, 5);
		gbc_lblServer.gridx = 0;
		gbc_lblServer.gridy = 0;
		serverWrap.add(lblServer, gbc_lblServer);
		
		JLabel lblPort = new JLabel("Port");
		GridBagConstraints gbc_lblPort = new GridBagConstraints();
		gbc_lblPort.insets = new Insets(0, 0, 5, 5);
		gbc_lblPort.anchor = GridBagConstraints.EAST;
		gbc_lblPort.gridx = 1;
		gbc_lblPort.gridy = 1;
		serverWrap.add(lblPort, gbc_lblPort);
		
		_bindPortText = new JTextField();
		GridBagConstraints gbc_bindPortText = new GridBagConstraints();
		gbc_bindPortText.insets = new Insets(0, 0, 5, 0);
		gbc_bindPortText.fill = GridBagConstraints.HORIZONTAL;
		gbc_bindPortText.gridx = 2;
		gbc_bindPortText.gridy = 1;
		serverWrap.add(_bindPortText, gbc_bindPortText);
		_bindPortText.setColumns(10);
		
		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		GridBagConstraints gbc_btnStartServer = new GridBagConstraints();
		gbc_btnStartServer.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartServer.anchor = GridBagConstraints.EAST;
		gbc_btnStartServer.gridx = 2;
		gbc_btnStartServer.gridy = 2;
		serverWrap.add(btnStartServer, gbc_btnStartServer);
		_frame.getContentPane().add(panel_1, BorderLayout.EAST);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JTextArea txtrHogehogeFugafuga = new JTextArea();
		txtrHogehogeFugafuga.setText("hogehoge\r\nfugafuga");
		panel_1.add(txtrHogehogeFugafuga, BorderLayout.NORTH);
	}

}
