package othello;

import othello.Board.CellState;

public abstract class Player extends Model {
	private CellState _myColor;
	public abstract void playTurn(Board board, Game game);
	public abstract void onStart(Game game);
	public abstract void onFinish(Game game);
	
	public CellState getColor(){
		return _myColor;
	}
	public void setColor(int code){
		if(code == -1){
			setColor(CellState.White);
		} else if(code == 1){
			setColor(CellState.Black);
		} else {
			throw new AssertionError();
		}
	}

	public void setColor(CellState color) {
		_myColor = color;
	}

}
