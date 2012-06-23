package othello;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ServerDaemon extends Model {
	private Signal<Lobby> _onLobbyMatched;
	private Daemon _waitDaemon;
	private int _waitPort;
	private int _nextId;

	public ServerDaemon(int waitport) throws IOException {
		_nextId = 0;
		_onLobbyMatched = new Signal<Lobby>();
		_waitPort = waitport;
		_waitDaemon = new Daemon();
	}

	public void addOnLobbyMatched(Listener<Lobby> listener) {
		_onLobbyMatched.addListener(listener);
	}

	public void start() {
		_waitDaemon.start();
	}

	private int getNextLobbyID() {
		return _nextId++;
	}

	private class Daemon extends Thread {
		ServerSocket _sock;

		Daemon() throws IOException {
			_sock = new ServerSocket(_waitPort);
		}

		@Override
		public void run() {
			Lobby waiting = null;
			try {
				while (true) {
					Socket sock = _sock.accept();
					if (waiting == null) {
						waiting = new Lobby(getNextLobbyID());
					}
					waiting.addConnection(sock);
					if (waiting.isReady()) {
						_onLobbyMatched.fire(waiting);
						waiting = null;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
