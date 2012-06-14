package othello;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JTextArea;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JTextField;
import java.awt.Insets;
import java.util.logging.LogRecord;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class DebugFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel _wrappingPane;
	private JTextField _textField;
	private JTextArea _loggingArea;

	public DebugFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 748, 527);
		_wrappingPane = new JPanel();
		_wrappingPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(_wrappingPane);
		_wrappingPane.setLayout(new GridLayout(0, 2, 3, 0));

		JPanel _controlPanel = new JPanel();
		_wrappingPane.add(_controlPanel);
		GridBagLayout gbl__controlPanel = new GridBagLayout();
		gbl__controlPanel.columnWidths = new int[] { 114, 0, 0, 131, 0 };
		gbl__controlPanel.rowHeights = new int[] { 23, 0, 0, 0, 0, 0 };
		gbl__controlPanel.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl__controlPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		_controlPanel.setLayout(gbl__controlPanel);

		JLabel lblImmediateCommand = new JLabel("Immediate Command");
		GridBagConstraints gbc_lblImmediateCommand = new GridBagConstraints();
		gbc_lblImmediateCommand.anchor = GridBagConstraints.WEST;
		gbc_lblImmediateCommand.insets = new Insets(0, 0, 5, 5);
		gbc_lblImmediateCommand.gridx = 0;
		gbc_lblImmediateCommand.gridy = 0;
		_controlPanel.add(lblImmediateCommand, gbc_lblImmediateCommand);

		_textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 4;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		_controlPanel.add(_textField, gbc_textField);
		_textField.setColumns(10);

		JButton btnLoopback = new JButton("Loopback");
		GridBagConstraints gbc_btnLoopback = new GridBagConstraints();
		gbc_btnLoopback.insets = new Insets(0, 0, 5, 5);
		gbc_btnLoopback.gridx = 2;
		gbc_btnLoopback.gridy = 2;
		_controlPanel.add(btnLoopback, gbc_btnLoopback);

		JButton btnServer = new JButton("Server");
		GridBagConstraints gbc_btnServer = new GridBagConstraints();
		gbc_btnServer.insets = new Insets(0, 0, 5, 0);
		gbc_btnServer.gridx = 3;
		gbc_btnServer.gridy = 2;
		_controlPanel.add(btnServer, gbc_btnServer);

		JLabel lblLoggingLevel = new JLabel("Logging level");
		GridBagConstraints gbc_lblLoggingLevel = new GridBagConstraints();
		gbc_lblLoggingLevel.anchor = GridBagConstraints.EAST;
		gbc_lblLoggingLevel.insets = new Insets(0, 0, 0, 5);
		gbc_lblLoggingLevel.gridx = 0;
		gbc_lblLoggingLevel.gridy = 4;
		_controlPanel.add(lblLoggingLevel, gbc_lblLoggingLevel);

		JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "FINEST", "FINER", "FINE", "CONFIG", "INFO",
				"WARNING", "SEVERE" }));
		comboBox.setSelectedIndex(2);
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.gridwidth = 3;
		gbc_comboBox.insets = new Insets(0, 0, 0, 5);
		gbc_comboBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 4;
		_controlPanel.add(comboBox, gbc_comboBox);

		JPanel _loggingPanel = new JPanel();
		_wrappingPane.add(_loggingPanel);
		_loggingPanel.setLayout(new BorderLayout(0, 0));

		_loggingArea = new JTextArea();
		_loggingPanel.add(_loggingArea);
	}

	public void pushLog(LogRecord rec) {
		_loggingArea.setText(_loggingArea.getText() + "\n" + rec.getMessage());
	}
	
}
