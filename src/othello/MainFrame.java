package othello;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel _contentPane;
	private JTextField tf;
	private JTextArea ta;
	private JLabel label;
	private BoardView canvas;
	private JMenuBar _menuBar;
	private JMenu _menu;
	private JMenuItem _connectMenuItem;
	private JMenu _viewMenuItem;
	private JCheckBoxMenuItem _checkBoxMenuItem;
	private JMenu _exportMenuItem;
	private JMenu _exportChatLogMenuItem;
	private JCheckBoxMenuItem _autoPilotCheckBoxMenuItem;
	private JSeparator _separator;

	public MainFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);

		_menuBar = new JMenuBar();
		setJMenuBar(_menuBar);

		_menu = new JMenu("Game");
		_menuBar.add(_menu);

		_connectMenuItem = new JMenuItem("Connect...");
		_menu.add(_connectMenuItem);

		_separator = new JSeparator();
		_menu.add(_separator);

		_autoPilotCheckBoxMenuItem = new JCheckBoxMenuItem("Auto Pilot");
		_menu.add(_autoPilotCheckBoxMenuItem);

		_exportMenuItem = new JMenu("Export");
		_menu.add(_exportMenuItem);

		_exportChatLogMenuItem = new JMenu("Chat log");
		_exportMenuItem.add(_exportChatLogMenuItem);

		_viewMenuItem = new JMenu("View");
		_menuBar.add(_viewMenuItem);

		_checkBoxMenuItem = new JCheckBoxMenuItem("Debug mode");
		_checkBoxMenuItem.setSelected(true);
		_checkBoxMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		_viewMenuItem.add(_checkBoxMenuItem);
		_contentPane = new JPanel();
		_contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		_contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(_contentPane);
		this.setSize(640, 320);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		tf = new JTextField(40);
		tf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tf.setText("");
			}
		});
		ta = new JTextArea(18, 40);
		ta.setLineWrap(true);
		ta.setEditable(false);

		JPanel mainp = (JPanel) getContentPane();
		JPanel ep = new JPanel();
		GridLayout gl = new GridLayout(1, 2);
		gl.setHgap(5);
		mainp.setLayout(gl);
		ep.setLayout(new BorderLayout());
		ep.add(new JScrollPane(ta), BorderLayout.CENTER);
		ep.add(tf, BorderLayout.SOUTH);
		label = new JLabel();
		canvas = new BoardView();
		JPanel wp = new JPanel();
		_contentPane.add(wp, BorderLayout.NORTH);
		wp.setLayout(new BorderLayout());
		wp.add(label, BorderLayout.SOUTH);
		wp.add(canvas, BorderLayout.CENTER);
		mainp.add(ep);
		this.setMessage("Ready.");
	}

	public void setMessage(String msg) {
		label.setText(msg);
	}

	public BoardView getBoardView() {
		return canvas;
	}
}
