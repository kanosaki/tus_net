package othello;

public class Controller {
	private Game _game;
	RemoteAdapter _remote;
	MainFrame _mainFrame;

	public Controller() {
	}

	public void setGame(Game game) {
		_game = game;
	}

	public void setRemoteAdapter(RemoteAdapter adapter) {
		_remote = adapter;
	}
	
	public void setMainFrame(MainFrame frame){
		_mainFrame = frame;
	}

	public void updateBoard(Board board) {
		getGame().updateBoard(board);
		_mainFrame.getBoardView().updateModel(board);
	}

	public void close() {
		getGame().close();
		_remote.close();
	}

	public void startGame(int myColor) {
		getGame().start(myColor);
	}

	public void endGame(String result) {
		getGame().end();
	}

	public void turnChange(int turn) {
		getGame().turnChange(turn);
		if(getGame().isMyTurn(turn))
			_mainFrame.setMessage("YOUR TURN!");
		else
			_mainFrame.setMessage("Hostile's turn");
	}

	public void error(int errorCode) {
		// TODO: implement
	}

	public void said(String user, String message) {
		_mainFrame.setChatMessage(user, message);
	}

	public void say(String msg) {
		Command cmd = new Command.SAY(msg);
		_remote.send(cmd);
	}

	public void showMessage(String msg) {
		_mainFrame.setMessage(msg);
	}
	
	public void pushMessage(String msg){
		_mainFrame.pushMessage(msg);
	}

	public void putStone(int x, int y) {
		Command cmd = new Command.PUT(x, y);
		_remote.send(cmd);
	}

	Game getGame() {
		return _game;
	}

}
