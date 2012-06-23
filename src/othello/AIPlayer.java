package othello;

import java.awt.Point;

import othello.Board.CellState;

public abstract class AIPlayer extends Player {
    Controller _controller;
    CellState _myColor;

    public AIPlayer(Controller controller) {
        _controller = controller;
    }

    public abstract Point think(Board board, Game game);

    public static AIPlayer create(Controller controller, Strategy strategy) {
        switch (strategy) {
        case Simple:
            return new Simple(controller);
        default:
            throw new AssertionError();
        }
    }

    @Override
    public void onStart(Game game) {
        _controller.nick("Simpson");
        _controller.say("Let's enjoy the game!");
    }
    
    @Override
    public void onFinish(Game game) {
        _controller.say("Thank you for playing.");

    }

    protected void phaseHandle(Board board, Game game) {
    }

    @Override
    public void playTurn(Board board, Game game) {
        phaseHandle(board, game);
        _controller.pushMessage(String.format("AI is thinking..."));
        Point next = think(board, game);
        _controller.pushMessage(String.format("Done, (%s, %s)", next.x, next.y));
        putStone(next.x, next.y);
    }

    public enum Strategy {
        Simple
    }

    protected void putStone(int x, int y) {
        _controller.putStone(x, y);
    }

    public void setColor(int code) {
        if (code == -1) {
            setColor(CellState.White);
        } else if (code == 1) {
            setColor(CellState.Black);
        } else {
            throw new AssertionError();
        }
    }

    public static class Simple extends AIPlayer {
        public Simple(Controller _controller) {
            super(_controller);
        }

        @Override
        public Point think(Board board, Game game) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int x = 0; x < Board.SIZE; x++) {
                for (int y = 0; y < Board.SIZE; y++) {
                    if (board.canPut(x, y, this.getColor())) {
                        return new Point(x, y);
                    }
                }
            }
            throw new AssertionError();
        }
    }
}
