package othello;

public class Game {
	// 0: black, 0: white
	Player[] _players;
	Board _board;
	
	public static enum State {
		Ready, BlackTurn, WhiteTurn, Finished
	}
}
