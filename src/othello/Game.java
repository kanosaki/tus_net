package othello;

public class Game {
	Player _player;
	Board _board;
	
	public Game(Player player){
		_player = player;
	}
	
	public Board getBoard(){
		return _board;
	}
	
	public void updateBoard(Board board){
		_board = board;
	}
	
	public static enum State {
		Ready, BlackTurn, WhiteTurn, Finished
	}

	public void close() {
		
	}

	public void start(int myColor) {
		
	}

	public void end() {
		
	}

	public void turnChange(int turn) {
		
	}
}
