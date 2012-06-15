package othello;

public class Controller {
	Game _game;
	RemoteAdapter _remote;

	public Controller() {
	}
	
	public void setGame(Game game){
		_game = game;
	}
	
	public void setRemoteAdapter(RemoteAdapter adapter){
		_remote = adapter;
	}

	public void updateBoard(Board board) {
		_game.updateBoard(board);
	}

	public void close() {
		_game.close();
		_remote.close();
	}

	public void startGame(int myColor) {
		_game.start(myColor);
	}

	public void endGame(String result) {
		_game.end();
	}

	public void turnChange(int turn) {
		_game.turnChange(turn);
	}

	public void error() {
		// TODO: implement
	}

	public void said(String user, String message) {

	}
	
	public void say(String msg){
		Command cmd = new Command.SAY(msg);
		_remote.send(cmd);
	}

	public void putStone(int x, int y) {
		Command cmd = new Command.PUT(x, y);
		_remote.send(cmd);
	}

}
