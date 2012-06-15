package othello;

import java.awt.Point;
import java.util.logging.Logger;

public class UserPlayer implements Player {
	private static final Logger log = Logger.getLogger("User Player");
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

	public boolean isMyTurn(){
		return _isMyTurn;
	}
	
	@Override
	public void playTurn(Board board) {
		_isMyTurn = true;
	}

	private void putStone(int x, int y) {
		if (_isMyTurn)
			_controller.putStone(x, y);
		else
			log.warning("NOT YOUR TURN!!");
	}

}
