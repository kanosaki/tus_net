package othello;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

public class BoardView extends JPanel {
    private static final long serialVersionUID = 1L;
    private Board _model;
    private Signal<Point> _onClicked;
    private Color _gridColor;
    private Color _boardBaseColor;
    private final static int PADDING_X = 20;
    private final static int PADDING_Y = 10;
    private final static int CELL_SIZE = 30;
    private final static int CELL_PADDING = 2;
    private final Rectangle BOARD_AREA = new Rectangle(PADDING_X, PADDING_Y, CELL_SIZE * Board.SIZE, CELL_SIZE
            * Board.SIZE);

    public BoardView() {
        this(new Board());
    }

    public BoardView(Board model) {
        _onClicked = new Signal<Point>();
        setBoardBaseColor(new Color(0, 180, 0));
        this.setGridColor(Color.BLACK);
        this.updateModel(model);
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                Point p = toLogicalPoint(e.getPoint());
                if (BOARD_AREA.contains(e.getPoint()) && isNotNearGrid(e.getPoint()))
                    _onClicked.fireAsync(p);
            }
        });
    }

    private static boolean isNotNearGrid(Point p) {
        final int margin = 4;
        int x = p.x - PADDING_X;
        int y = p.y - PADDING_Y;
        return ((x % CELL_SIZE > margin) && ((x % CELL_SIZE) < (CELL_SIZE - margin)))
                || ((y % CELL_SIZE > margin) && ((y % CELL_SIZE) < (CELL_SIZE - margin)));
    }

    protected Point toLogicalPoint(Point p) {
        int x = (p.x - PADDING_X) / CELL_SIZE;
        int y = (p.y - PADDING_Y) / CELL_SIZE;
        return new Point(x, y);
    }

    public void updateModel(Board model) {
        if (model == null)
            throw new IllegalArgumentException("NullArgumentException");
        _model = model;
        repaint();
    }

    public void addOnClickedListender(Listener<Point> listener) {
        _onClicked.addListener(listener);
    }

    public void paintComponent(Graphics gg) {
        Graphics2D g = (Graphics2D)gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g);
        g.setColor(getBoardBaseColor());
        g.fillRect(PADDING_X, PADDING_Y, CELL_SIZE * Board.SIZE, CELL_SIZE * Board.SIZE);

        g.setColor(this.getGridColor());
        for (int i = 0; i < Board.SIZE + 1; i++) {
            g.drawLine(PADDING_X, PADDING_Y + i * CELL_SIZE, PADDING_X + Board.SIZE * CELL_SIZE, PADDING_Y + i
                    * CELL_SIZE);
            g.drawLine(PADDING_X + i * CELL_SIZE, PADDING_Y, PADDING_X + i * CELL_SIZE, PADDING_Y + Board.SIZE
                    * CELL_SIZE);
        }
        for (int i = 0; i < Board.SIZE; i++) {
            for (int j = 0; j < Board.SIZE; j++) {
                if (_model.hasStone(i, j)) {
                    g.setColor(_model.get(i, j).getColor());
                    g.fillOval(PADDING_X + CELL_SIZE * i + CELL_PADDING + 1, PADDING_Y + CELL_SIZE * j + CELL_PADDING + 1,
                            CELL_SIZE - CELL_PADDING * 2, CELL_SIZE - CELL_PADDING * 2);
                }
            }
        }
    }

    public Color getGridColor() {
        return _gridColor;
    }

    public void setGridColor(Color gridColor) {
        _gridColor = gridColor;
    }

    private Color getBoardBaseColor() {
        return _boardBaseColor;
    }

    private void setBoardBaseColor(Color boardBaseColor) {
        _boardBaseColor = boardBaseColor;
    }

}
