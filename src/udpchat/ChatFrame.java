/*
 * Copyright (c) 2012 kanosaki
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the "Software"), 
 * to deal in the Software without restriction, including without limitation 
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 * and/or sell copies of the Software, and to permit persons to whom 
 * the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 */


package udpchat;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.RenderingHints.Key;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JSplitPane;
import javax.swing.JInternalFrame;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.FlowLayout;
import javax.swing.AbstractListModel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JCheckBox;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.Box;

public class ChatFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea inputText;
	JList _messagesList;
	private MessagesImpl _messages;
	private App _app;
	private JPanel _panel;
	private JCheckBox sendWithSEnterCheckBox;
	private JButton systemButton;
	private JPanel _panel_1;
	private JTextPane logPane;
	private Box _verticalBox;

	public MessagesImpl getMessages() {
		return _messages;
	}

	public class MessagesImpl extends AbstractListModel {
		private static final long serialVersionUID = 1L;
		public final static int DEFAULT_HISTORY_SIZE = 100;
		private int _historySize;
		private Vector<String> _messages;

		public MessagesImpl(int histsize) {
			_historySize = histsize;
			_messages = new Vector<String>(histsize);
		}

		public MessagesImpl() {
			this(DEFAULT_HISTORY_SIZE);
		}

		public void add(String msg) {
			_messages.insertElementAt(msg, 0);
			if (_messages.size() > _historySize * 2) {
				for (int i = _messages.size() - 1; i >= _historySize - 1; i--) {
					_messages.remove(i);
				}
			}
			this.fireContentsChanged(this, 0, 1);
		}

		@Override
		public Object getElementAt(int arg0) {
			return _messages.get(arg0);
		}

		@Override
		public int getSize() {
			return _messages.size();
		}
	}

	private void send() {
		String val = inputText.getText();
		inputText.setText("");
		_app.send(val);
	}

	public void log(String str) {
		logPane.setText(logPane.getText() + str + "\n");
	}

	/**
	 * Create the frame.
	 */
	@SuppressWarnings("serial")
	public ChatFrame(App app) {
		_app = app;
		_messages = new MessagesImpl();
		setFont(new Font("Monoscaped", Font.PLAIN, 13));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 638, 454);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(4, 4));

		JPanel inputPanel = new JPanel();
		contentPane.add(inputPanel, BorderLayout.NORTH);
		inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));

		inputText = new JTextArea();
		inputText.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent k) {
				if (k.getKeyCode() == KeyEvent.VK_ENTER) {
					if (sendWithSEnterCheckBox.isSelected()) {
						if (k.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
							send();
						}
					} else {
						send();
					}
				}
			}
		});
		inputText.setFont(new Font("Monoscaped", Font.PLAIN, 13));
		inputPanel.add(inputText);
		inputText.setColumns(10);

		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send();
			}
		});

		sendButton.setFont(new Font("Meiryo UI", Font.PLAIN, 13));
		inputPanel.add(sendButton);

		JPanel bodyPanel = new JPanel();
		contentPane.add(bodyPanel, BorderLayout.CENTER);
		bodyPanel.setLayout(new BorderLayout(0, 0));

		_messagesList = new JList();
		_messagesList.setFont(new Font("Monospaced", Font.PLAIN, 13));
		_messagesList.setModel(_messages);
		bodyPanel.add(_messagesList);

		_panel = new JPanel();
		contentPane.add(_panel, BorderLayout.EAST);
		_panel.setLayout(new BorderLayout(0, 0));

		_panel_1 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) _panel_1.getLayout();
		flowLayout.setAlignment(FlowLayout.LEADING);
		_panel.add(_panel_1, BorderLayout.CENTER);

		logPane = new JTextPane();
		logPane.setEditable(false);
		_panel_1.add(logPane);

		_verticalBox = Box.createVerticalBox();
		_panel.add(_verticalBox, BorderLayout.NORTH);

		systemButton = new JButton("system");
		systemButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String res = Helper.system(inputText.getText());
					_app.send(res);
				} catch (IOException e) {
					e.printStackTrace();
					log("Command failed.");
				}
			}
		});
		_verticalBox.add(systemButton);
		systemButton.setHorizontalAlignment(SwingConstants.LEFT);

		sendWithSEnterCheckBox = new JCheckBox("Send with Ctrl-Enter");
		_verticalBox.add(sendWithSEnterCheckBox);
	}

}
