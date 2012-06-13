package othello;

public class Game {
	// 0: black, 1: white
	Player[] _players;
	Board _board;
	
	public Game(){
		_players  = new Player[2];
		
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
}
