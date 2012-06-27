package othello;

public class Game extends Model {
    int _myCode;
    int _turn;
    Player _player;
    String _nickName;
    Board _board;

    public Game(Player player) {
        _player = player;
    }

    public Board getBoard() {
        return _board;
    }

    public void updateBoard(Board board) {
        _board = board;
    }

    public static enum State {
        Ready, BlackTurn, WhiteTurn, Finished
    }

    public void close() {

    }

    public void start(int myColor) {
        _myCode = myColor;
        _player.setColor(myColor);
        _player.onStart(this);
    }

    public void end() {
        _player.onFinish(this);
    }

    public int getTurn() {
        return _turn / 2;
    }
    
    public boolean isMyTurn(int code) {
        return _myCode == code;
    }

    public void turnChange(int turn) {
        _turn++;
        System.out.println("Turn " + _turn);
        if (isMyTurn(turn)) {
            _player.playTurn(_board, this);
        }
    }
}
