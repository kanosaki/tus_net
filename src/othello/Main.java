package othello;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
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
import java.io.IOException;
import java.util.logging.LogRecord;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.BoxLayout;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

import othello.AIPlayer.Strategy;

public class Main extends Model {
	private static final boolean DEBUG_MODE = true;
	private JFrame _frame;
	private final JPanel panel_1 = new JPanel();
	private JTextField _bindPortText;
	private JTextField _serverHostText;
	private JTextField _serverPortText;
	private JComboBox _comboBox;
	private JList _debugList;
	private DefaultListModel _debugListModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Debug.initialize();
					Main window = new Main();
					window._frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void showMessage(String msg) {

	}

	protected void startClient() {
		String host = _serverHostText.getText();
		if (host.length() == 0) {
			showMessage("Server Host is empty");
			return;
		}
		String portExpr = _serverPortText.getText();
		if (portExpr.length() == 0) {
			showMessage("Server port is empty.");
			return;
		}
		int port = 0;
		try {
			port = Integer.parseInt(portExpr);
		} catch (NumberFormatException ex) {
			showMessage("Invalid number format at Server port");
			return;
		}
		if (port < 1024 || 65535 < port) {
			showMessage("Invalid number is specified at Server port, port must be in between 1024 and 65535");
			return;
		}
		OthelloClient client = new OthelloClient(host, port);
		client.start();
	}

	protected void startServer() {
		String portExpr = _bindPortText.getText();
		if (portExpr.length() == 0) {
			showMessage("Server port is not given.");
			return;
		}
		int port = 0;
		try {
			port = Integer.parseInt(portExpr);
		} catch (NumberFormatException ex) {
			showMessage("Invalid number format at Server port");
			return;
		}
		if (port < 1024 || 65535 < port) {
			showMessage("Invalid number is specified at Server port, port must be in between 1024 and 65535");
			return;
		}

		OthelloServer server = new OthelloServer(port);
		try {
			server.start();
		} catch (IOException e) {
			showMessage(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public Main() {
		setLoggerName(getClass().getSimpleName());
		_debugListModel = new DefaultListModel();
		initDebugger();
		initialize();
		getLog().info("Ready.");
		if (DEBUG_MODE)
			debugMode();
	}

	private void debugMode() {
		getLog().info("DEBUG MODE");
		String host = "localhost";
		int port = 9876;
		OthelloServer server = new OthelloServer(port);
		try {
			server.start();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		OthelloClient user = new OthelloClient(host, port);
		user.start();
		OthelloClient ai = new OthelloClient.AI(host, port, Strategy.Simple);
		ai.start();
	}

	private void initDebugger() {
		Debug debug = Debug.getInstance();
		debug.addOnNextListener(new Listener<LogRecord>() {
			@Override
			public void next(LogRecord val) {
				String msg = String.format("[%s - %s] : %s", val.getLevel(), val.getLoggerName(), val.getMessage());
				_debugListModel.addElement(msg);
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		_frame = new JFrame();
		_frame.setBounds(100, 100, 755, 543);
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.getContentPane().setLayout(new BoxLayout(_frame.getContentPane(), BoxLayout.X_AXIS));

		JPanel mainWrap = new JPanel();
		_frame.getContentPane().add(mainWrap);
		mainWrap.setLayout(new GridLayout(0, 1, 3, 0));

		JPanel clientWrap = new JPanel();
		clientWrap.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainWrap.add(clientWrap);
		GridBagLayout gbl_clientWrap = new GridBagLayout();
		gbl_clientWrap.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_clientWrap.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gbl_clientWrap.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_clientWrap.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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
		_serverHostText.setText("localhost");
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
		_serverPortText.setText("9876");
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

		_comboBox = new JComboBox();
		_comboBox.setModel(new DefaultComboBoxModel(new String[] { "User", "AI - Simple" }));
		_comboBox.setSelectedIndex(0);
		GridBagConstraints gbc__comboBox = new GridBagConstraints();
		gbc__comboBox.insets = new Insets(0, 0, 5, 0);
		gbc__comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc__comboBox.gridx = 2;
		gbc__comboBox.gridy = 3;
		clientWrap.add(_comboBox, gbc__comboBox);

		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				startClient();
			}
		});
		GridBagConstraints gbc_btnConnect = new GridBagConstraints();
		gbc_btnConnect.anchor = GridBagConstraints.EAST;
		gbc_btnConnect.gridx = 2;
		gbc_btnConnect.gridy = 4;
		clientWrap.add(btnConnect, gbc_btnConnect);

		JPanel serverWrap = new JPanel();
		serverWrap.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		mainWrap.add(serverWrap);
		GridBagLayout gbl_serverWrap = new GridBagLayout();
		gbl_serverWrap.columnWidths = new int[] { 0, 0, 0, 0 };
		gbl_serverWrap.rowHeights = new int[] { 0, 0, 0, 0, 0 };
		gbl_serverWrap.columnWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		gbl_serverWrap.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
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
		_bindPortText.setText("9876");
		GridBagConstraints gbc_bindPortText_2 = new GridBagConstraints();
		gbc_bindPortText_2.insets = new Insets(0, 0, 5, 0);
		gbc_bindPortText_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_bindPortText_2.gridx = 2;
		gbc_bindPortText_2.gridy = 1;
		serverWrap.add(_bindPortText, gbc_bindPortText_2);
		_bindPortText.setColumns(10);

		JButton btnStartServer = new JButton("Start Server");
		btnStartServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startServer();
			}
		});
		GridBagConstraints gbc_btnStartServer = new GridBagConstraints();
		gbc_btnStartServer.insets = new Insets(0, 0, 5, 0);
		gbc_btnStartServer.anchor = GridBagConstraints.EAST;
		gbc_btnStartServer.gridx = 2;
		gbc_btnStartServer.gridy = 2;
		serverWrap.add(btnStartServer, gbc_btnStartServer);
		panel_1.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		_frame.getContentPane().add(panel_1);
		panel_1.setLayout(new BorderLayout(0, 0));

		_debugList = new JList(_debugListModel);
		panel_1.add(_debugList, BorderLayout.CENTER);
	}

}
