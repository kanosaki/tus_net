package othello;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RemoteAdapter extends Model {
	private Signal<Command> _onMessageReceived;
	private Signal<RemoteAdapter> _onConnectionClosing;

	private BufferedReader _input;
	private BufferedWriter _output;
	private Socket _sock;
	private String _remoteHost;
	private int _remotePort;
	private int _ID;

	protected Sender _sender;
	protected Receiver _reciever;

	public RemoteAdapter() {
		_onMessageReceived = new Signal<Command>();
		_onConnectionClosing = new Signal<RemoteAdapter>();
	}
	
	public RemoteAdapter(int id) {
		this();
		_ID = id;
	}

	protected class Sender extends Thread {
		private Queue<Command> _sendBuffer;

		protected Sender() {
			_sendBuffer = new ConcurrentLinkedQueue<Command>();
		}

		public synchronized void push(Command com) {
			_sendBuffer.add(com);
			notifyAll();
		}

		@Override
		public synchronized void run() {
			try {
				Command next;
				while (true) {
					wait();
					while ((next = _sendBuffer.poll()) != null) {
						if (this.isInterrupted())
							return;
						onMessageSending(next);
						String line = next.encode();
						_output.write(line + "\n");
						_output.flush();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				getLog().warning(e.getMessage());
				// TODO: write error handling
			} catch (InterruptedException e) {
				e.printStackTrace();
				getLog().warning(e.getMessage());
			}
		}
	}

	protected class Receiver extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					if (this.isInterrupted())
						return;
					String line = _input.readLine();
					Command msg = decodeMessage(line);
					onMessageReceived(msg);
				}
			} catch (IOException e) {
				e.printStackTrace();
				getLog().warning(e.getMessage());
				// TODO: write error handling
			}
		}

		protected Command decodeMessage(String line) {
			try {
				Command msg = Command.decode(line, RemoteAdapter.this);
				if (msg == null) {
					getLog().warning("Message decoding failed. Decoder returned null.");
					getLog().warning("Data: " + line);
					return Command.VOID;
				}
				if (msg == Command.VOID) {
					getLog().warning("Message decoding failed. Decoder returned VOID message");
					getLog().warning("Data: " + line);
				}
				return msg;
			} catch (Exception e) {
				getLog().warning("Message decoding failed. " + e.getMessage());
			}
			return Command.VOID;
		}
	}

	public void send(Command com) {
		_sender.push(com);
	}

	public void start(Socket sock) throws IOException {
		_sock = sock;
		_input = new BufferedReader(new InputStreamReader(_sock.getInputStream()));
		_output = new BufferedWriter(new OutputStreamWriter(_sock.getOutputStream()));

		_sender = new Sender();
		_sender.start();
		_reciever = new Receiver();
		_reciever.start();
		getLog().info("RemoteAdapter started.");
	}
	
	public void start(String host, int port) throws IOException {
		setRemoteHost(host);
		setRemotePort(port);
		start(new Socket(host, port));
	}

	public void close() {
		if (_sender.isAlive())
			_sender.interrupt();
		if (_reciever.isAlive())
			_reciever.interrupt();
	}

	protected void onMessageReceived(Command msg) {
		getLog().info("RECEIVED: " + msg);
		_onMessageReceived.fire(msg);
	}
	
	protected void onMessageSending(Command msg) {
		getLog().info("SENDING: " + msg);
	}

	public void addMessageListener(Listener<Command> listener) {
		_onMessageReceived.addListener(listener);
	}
	
	public void addOnConnectionClosing(Listener<RemoteAdapter> listener){
		_onConnectionClosing.addListener(listener);
	}

	public String getRemoteHost() {
		return _remoteHost;
	}

	public void setRemoteHost(String remoteHost) {
		_remoteHost = remoteHost;
	}

	public int getRemotePort() {
		return _remotePort;
	}

	public void setRemotePort(int remotePort) {
		_remotePort = remotePort;
	}

	public int getID() {
		return _ID;
	}

	public void setID(int iD) {
		_ID = iD;
	}

}
