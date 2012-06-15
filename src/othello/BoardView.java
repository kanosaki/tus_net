package othello;

import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;

public class BoardView extends JPanel {
	private static final long serialVersionUID = 1L;
	private Board _model;
	private Signal<Point> _onClicked;
	private Color _gridColor;
	private final static int PADDING_X = 20;
	private final static int PADDING_Y = 10;
	private final static int CELL_SIZE = 30;

	public BoardView() {
		this(new Board());
	}

	public BoardView(Board model) {
		this.updateModel(model);
		_onClicked = new Signal<Point>();
		this.setBackground(new Color(0, 180, 0));
		this.setGridColor(Color.BLACK);
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				_onClicked.fire(toLogicalPoint(e.getPoint()));
			}
		});
	}

	protected Point toLogicalPoint(Point p) {
		int x = (int) ((p.getX() - PADDING_X) / Board.SIZE);
		int y = (int) ((p.getY() - PADDING_Y) / Board.SIZE);
		return new Point(x, y);
	}

	public void updateModel(Board model) {
		if (model == null)
			throw new IllegalArgumentException("NullArgumentException");
		_model = model;
		this.repaint();
	}
	
	public void addOnClickedListender(Listener<Point> listener){
		_onClicked.addListener(listener);
	}

	public void paintComponent(Graphics g) {
		g.setColor(this.getBackground());
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
					g.fillOval(PADDING_X + CELL_SIZE * i, PADDING_Y + CELL_SIZE * j, CELL_SIZE, CELL_SIZE);
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

}
