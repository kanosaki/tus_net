package othello;

public class Board {
	public static final int SIZE = 8;
	public static final int CELLS = SIZE * SIZE;
	private CellState _board[][];

	public Board() {
		_board = new CellState[SIZE][SIZE];
	}

	public CellState get(int x, int y) {
		return _board[x][y];
	}

	/**
	 * Same as set, but value will be checked. 
	 */
	public void put(int x, int y, CellState state) {
		if (state == null)
			throw new IllegalArgumentException("Null CellState is not allowed");
		if (state == CellState.Void)
			throw new IllegalArgumentException("Void state is not allowed");
		this.set(x, y, state);
	}
	
	/**
	 * Simply set state to board. If you need value checking, use put method.
	 */
	public void set(int x, int y, CellState state) {
		_board[x][y] = state;
	}

	public void flip(int x, int y) {
		_board[x][y] = _board[x][y].flip();
	}

	public enum CellState {
		Void, Black, White;

		public CellState flip() {
			switch (this) {
			case Void:
				throw new IllegalStateException("You cannot flip Void block");
			case Black:
				return White;
			case White:
				return Black;
			default:
				throw new UnsupportedOperationException("Not implemented");
			}

		}
	}

}
