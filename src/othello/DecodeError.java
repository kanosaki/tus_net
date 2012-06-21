package othello;

public class DecodeError extends Exception {
	private static final long serialVersionUID = 1L;

	public DecodeError(String msg) {
		super(msg);
	}
}
