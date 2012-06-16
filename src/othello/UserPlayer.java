package othello;

import java.awt.Point;
import java.util.logging.Logger;

import othello.Board.CellState;

public class UserPlayer extends Player {
	Controller _controller;
	boolean _isMyTurn = false;

	public UserPlayer(Controller controller, BoardView view) {
		_controller = controller;
		view.addOnClickedListender(new Listener<Point>() {
			@Override
			public void next(Point val) {
				putStone(val.x, val.y);
			}
		});
	}

	public boolean isMyTurn() {
		return _isMyTurn;
	}

	@Override
	public void playTurn(Board board, Game game) {
		_isMyTurn = true;
	}

	private boolean checkPutting(int x, int y) {
		Board board = _controller.getGame().getBoard();
		return board.canPut(x, y, getColor());
	}

	private void putStone(int x, int y) {
		if (_isMyTurn) {
			if (checkPutting(x, y)) {
				_controller.putStone(x, y);
				_isMyTurn = false;
			} else {
				_controller.showMessage("YOU CAANNOT PUT THERE.");
			}
		} else
			_controller.showMessage("NOT YOUR TURN!!");
	}

}
