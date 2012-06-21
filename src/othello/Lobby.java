package othello;

import java.net.Socket;
import java.util.ArrayList;

public class Lobby extends Model {
	private int _lobbyID;
	private Controller _controller;
	private ArrayList<Player> _players;
	

	public Lobby(int lobbyID) {
		_lobbyID = lobbyID;
		setLoggerName(String.format("Lobby%2d", lobbyID));
	}

	public void addConnection(Socket sock) {

	}
}
