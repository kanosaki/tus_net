package othello;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

public class RemoteAdapter {
	private static Logger log = Logger.getLogger("RemoteAdapter");

	private Signal<Command> _onMessageReceived;
	private Queue<Command> _sendBuffer;
	private BufferedReader _input;
	private BufferedWriter _output;
	private Socket _sock;
	private String _remoteHost;
	private int _remotePort;

	protected Sender _sender;
	protected Receiver _reciever;

	public RemoteAdapter(String host, int port) {
		_onMessageReceived = new Signal<Command>();
		_sendBuffer = new ConcurrentLinkedQueue<Command>();
	}

	protected class Sender extends Thread {
		@Override
		public void run() {
			try {
				Command next;
				while ((next = _sendBuffer.poll()) != null) {
					if (this.isInterrupted())
						return;
					String line = next.encode();
					_output.write(line + "\n");
					_output.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
				log.warning(e.getMessage());
				// TODO: write error handling
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
				log.warning(e.getMessage());
				// TODO: write error handling
			}
		}

		protected Command decodeMessage(String line) {
			try {
				Command msg = Command.decode(line);
				if (msg == null) {
					log.warning("Message decoding failed. Decoder returned null.");
					return Command.VOID;
				}
				if (msg == Command.VOID) {
					log.warning("Message decoding failed. Decoder returned VOID message");
				}
				return msg;
			} catch (Exception e) {
				log.warning("Message decoding failed. " + e.getMessage());
			}
			return Command.VOID;
		}
	}

	public void send(Command msg) {
		_sendBuffer.add(msg);
		if (!_sender.isAlive()) {
			_sender = new Sender();
			_sender.start();
		}
	}

	public void start(String host, int port) throws IOException {
		_remoteHost = host;
		_remotePort = port;
		_sock = new Socket(host, port);
		_input = new BufferedReader(new InputStreamReader(_sock.getInputStream()));
		_output = new BufferedWriter(new OutputStreamWriter(_sock.getOutputStream()));

		_sender = new Sender();
		_reciever = new Receiver();
		log.info("RemoteAdapter started.");
	}

	public void close() {
		if (_sender.isAlive())
			_sender.interrupt();
		if (_reciever.isAlive())
			_reciever.interrupt();
	}

	protected void onMessageReceived(Command msg) {
		log.info("RECEIVED: " + msg);
		_onMessageReceived.fire(msg);
	}

	public void addMessageListener(Listener<Command> listener) {
		_onMessageReceived.addListener(listener);
	}
}
