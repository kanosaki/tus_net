package othello;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.logging.Logger;

public class RemoteAdapter extends Model {
	private Signal<Command> _onMessageReceived;

	private BufferedReader _input;
	private BufferedWriter _output;
	private Socket _sock;
	private String _remoteHost;
	private int _remotePort;

	protected Sender _sender;
	protected Receiver _reciever;

	public RemoteAdapter(String host, int port) {
		_onMessageReceived = new Signal<Command>();

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
				Command msg = Command.decode(line);
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

	public void start(String host, int port) throws IOException {
		_remoteHost = host;
		_remotePort = port;
		_sock = new Socket(host, port);
		_input = new BufferedReader(new InputStreamReader(_sock.getInputStream()));
		_output = new BufferedWriter(new OutputStreamWriter(_sock.getOutputStream()));

		_sender = new Sender();
		_sender.start();
		_reciever = new Receiver();
		_reciever.start();
		getLog().info("RemoteAdapter started.");
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

	public void addMessageListener(Listener<Command> listener) {
		_onMessageReceived.addListener(listener);
	}

}
