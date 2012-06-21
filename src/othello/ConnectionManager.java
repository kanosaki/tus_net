package othello;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectionManager extends Model {
	private Signal<Lobby> _onLobbyMatched;
	private ArrayList<Lobby> _lobbies;
	private Daemon _waitDaemon;
	private int _waitPort;

	public ConnectionManager(int waitport) throws IOException {
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
					if(waiting == null) {
						waiting = new Lobby(_lobbies.size());
						waiting.addConnection(sock);
					} else {
						waiting.addConnection(sock);
						_onLobbyMatched.fire(waiting);
						_lobbies.add(waiting);
						waiting = null;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}
}
