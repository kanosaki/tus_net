package othello;

import java.awt.Color;
import java.util.HashMap;

import othello.Board.CellState;

public class Board extends Model {
    public static final int SIZE = 8;
    public static final int CELLS = SIZE * SIZE;
    private CellState _board[][];
    private boolean _autoFlip = false;
    private int _stones = 0;

    public Board() {
        _board = new CellState[SIZE][SIZE];
        this.setInitialState();
    }

    protected void setInitialState() {
        this.put(3, 3, CellState.Black);
        this.put(4, 4, CellState.Black);
        this.put(3, 4, CellState.White);
        this.put(4, 3, CellState.White);
    }

    public CellState get(int x, int y) {
        CellState st = _board[x][y];
        if (st != null)
            return st;
        else
            return CellState.Void;
    }

    public boolean canPut(int x, int y, CellState color) {
        if (color == CellState.Void)
            throw new AssertionError();
        if (hasStone(x, y))
            return false;
        CellState hostileColor = color.flip();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;
                int seekX = x + dx;
                int seekY = y + dy;
                if (isInBound(seekX, seekY) && _board[seekX][seekY] == hostileColor) {
                    while (true) {
                        seekX += dx;
                        seekY += dy;
                        if (isInBound(seekX, seekY)) {
                            if (_board[seekX][seekY] == hostileColor)
                                continue;
                            else if (_board[seekX][seekY] == color) {
                                return true;
                            } else {
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }

            }
        }
        return false;
    }

    private boolean isInBound(int x, int y) {
        return x >= 0 && x < Board.SIZE && y >= 0 && y < Board.SIZE;
    }

    public boolean isFilled() {
        return _stones == SIZE * SIZE;
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
        if (_autoFlip)
            checkFlip(x, y);
    }

    protected void checkFlip(int x, int y) {
        CellState hostile = get(x, y);
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0)
                    continue;
                checkFlipRec(x + dx, y + dy, dx, dy, hostile);
            }
        }
    }

    private boolean checkFlipRec(int x, int y, int dx, int dy, CellState my) {
        if (!isInBound(x, y) || !hasStone(x, y)) {
            return false;
        } else if (get(x, y) == my) {
            return true;
        } else {
            if (checkFlipRec(x + dx, y + dy, dx, dy, my)) {
                flip(x, y);
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean hasStone(int x, int y) {
        return _board[x][y] != null && _board[x][y] != CellState.Void;
    }

    /**
     * Simply set state to board. If you need value checking, use put method.
     */
    void set(int x, int y, CellState state) {
        if (!hasStone(x, y))
            _stones += 1;
        _board[x][y] = state;
    }

    public void flip(int x, int y) {
        _board[x][y] = _board[x][y].flip();
    }

    public void setAutoFlip(boolean val) {
        _autoFlip = val;
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

        public Color getColor() {
            switch (this) {
            case Void:
                return Color.RED;
            case Black:
                return Color.BLACK;
            case White:
                return Color.WHITE;
            default:
                return Color.RED;
            }
        }
    }

    public String getResultString(String blacksName, String whitesName) {
        int blacks = 0, whites = 0;
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                switch (_board[x][y]) {
                case Black:
                    blacks++;
                    break;
                case White:
                    whites++;
                default:
                    break;
                }
            }
        }
        if (blacks == whites) {
            return "Draw!!";
        }
        String winner = blacks > whites ? blacksName : whitesName;
        return String.format("%s won. %s -> %s, %s -> %s", winner, blacksName, blacks, whitesName, whites);
    }

    public boolean canPutAny(CellState color) {
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                if(canPut(x, y, color)) {
                    return true;
                }
            }
        }
        return false;
    }
}
