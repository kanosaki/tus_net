package othello;

import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.*;

import othello.Board.CellState;

public abstract class Command {
	protected static final Logger log = Logger.getLogger("Message");

	public static Command decode(String line) {
		if (line == null)
			return null;
		if (line.startsWith("NICK"))
			return NICK.restore(line);
		if (line.startsWith("SAY"))
			return SAY.restore(line);
		if (line.startsWith("PUT"))
			return PUT.restore(line);
		if (line.startsWith("BOARD"))
			return BOARD.restore(line);
		if (line.startsWith("TURN"))
			return TURN.restore(line);
		if (line.startsWith("END"))
			return END.restore(line);
		if (line.startsWith("START"))
			return START.restore(line);
		if (line.startsWith("CLOSE"))
			return CLOSE.restore(line);
		return null;
	}

	public abstract String encode();

	public abstract void received(Controller ctrl);

	// NICK <username>
	public static class NICK extends Command {
		private String _name;

		public NICK(String name) {
			_name = name;
		}

		public String getName() {
			return _name;
		}

		public static Command restore(String line) {
			return new NICK(line.substring(4).trim());
		}

		@Override
		public String toString() {
			return String.format("<NICK: %s>", _name);
		}

		@Override
		public String encode() {
			return String.format("NICK %s", this.getName());
		}

		@Override
		public void received(Controller ctrl) {

		}
	}

	// SAY <message>
	// SAY <username>:<message>
	public static class SAY extends Command {
		static final Pattern SAY_RECV_PATTERN = Pattern.compile("SAY (.+):(.*)");

		public SAY(String message) {
			_message = message;
		}

		protected String _message;

		public String getMessage() {
			return _message;
		}

		@Override
		public String toString() {
			return String.format("<SAY: %s>", this.getMessage());
		}

		public static Command restore(String line) {
			Matcher recv = SAY_RECV_PATTERN.matcher(line);
			if (recv.matches()) {
				return new SAY_RECV(recv.group(0), recv.group(1));
			} else {
				return new SAY(line.substring(3).trim());
			}
		}

		public static class SAY_RECV extends SAY {
			private String _user;

			public String getUser() {
				return _user;
			}

			public SAY_RECV(String user, String msg) {
				super(msg);
				_user = user;
			}

			@Override
			public String toString() {
				return String.format("<SAY: %s from %s>", this.getMessage(), this.getUser());
			}

			@Override
			public String encode() {
				return String.format("SAY %s:%s", this.getUser(), this.getMessage());
			}

			@Override
			public void received(Controller ctrl) {
				ctrl.said(this.getUser(), this.getMessage());
			}
		}

		@Override
		public String encode() {
			return String.format("SAY %s", this.getMessage());
		}

		@Override
		public void received(Controller ctrl) {
			log.warning("Unexpected SAY(send) command received. " + this);
		}

	}

	// PUT <x> <y>
	// PUT ERROR <errno>
	public static class PUT extends Command {
		static final Pattern PUT_ERROR_PATTERN = Pattern.compile("PUT ERROR (\\d)");
		// TODO: Check format
		static final Pattern PUT_PATTERN = Pattern.compile("PUT (\\d) (\\d)"); //

		private int _x, _y;

		public PUT(int x, int y) {
			_x = x;
			_y = y;
		}

		public int getX() {
			return _x;
		}

		public int getY() {
			return _y;
		}

		public static Command restore(String line) {
			Matcher error = PUT_ERROR_PATTERN.matcher(line);
			if (error.matches()) {
				return new PUT_ERROR(Integer.parseInt(error.group(1)));
			} else {
				Matcher put = PUT_PATTERN.matcher(line);
				if (put.matches()) {
					int x = Integer.parseInt(put.group(1));
					int y = Integer.parseInt(put.group(2));
					return new PUT(x, y);
				} else {
					log.warning(String.format("Invalid PUT command received. [%s]", line));
					return Command.VOID;
				}
			}
		}

		@Override
		public String toString() {
			return String.format("<PUT (%d, %d)>", getX(), getY());
		}

		@Override
		public String encode() {
			return String.format("PUT %d %d", this.getX(), this.getY());
		}

		@Override
		public void received(Controller ctrl) {
			log.warning("Unexpected PUT command received : " + this);
		}

	}

	public static class PUT_ERROR extends Command { // PUT ERROR seems to be
		// not subclass of PUT.
		public PUT_ERROR(int code) {
			_code = code;
		}

		private int _code;
		static final String[] _errorDetail = { "Error 0 is Undefined.", "ERROR 1: Invalid format",
				"ERROR 2: Invalid position on PUT", "ERROR 3: Attempted to PUT on hostiles turn",
				"ERROR 4: Unknown request" };

		@Override
		public String toString() {
			return String.format("<PUT %s>", _errorDetail[_code]);
		}

		@Override
		public String encode() {
			return String.format("PUT ERROR %d", this.getCode());
		}

		public int getCode() {
			return _code;
		}

		@Override
		public void received(Controller ctrl) {
			ctrl.error();
			throw new ServerError(this);
		}
	}

	// BOARD <board data...>
	public static class BOARD extends Command {
		Board _board;

		public BOARD(Board brd) {
			_board = brd;
		}

		public static Command restore(String line) {
			String boardData = line.substring(5).trim();
			StringTokenizer tokenizer = new StringTokenizer(boardData, " ");
			int tokenCount = 0;
			if ((tokenCount = tokenizer.countTokens()) != Board.CELLS) {
				log.warning(String.format("Invalid BOARD data, content length expecded %d but received %d",
						Board.CELLS, tokenCount));
				return Command.VOID;
			}
			Board content = new Board();
			for (int x = 0; x < Board.SIZE; x++) {
				for (int y = 0; y < Board.SIZE; y++) {
					CellState state = TokenToState(tokenizer.nextToken());
					content.set(x, y, state);
				}
			}
			return new BOARD(content);
		}

		private static Board.CellState TokenToState(String token) {
			int cellVal = Integer.parseInt(token);
			switch (cellVal) {
			case 0:
				return CellState.Void;
			case 1:
				return CellState.Black;
			case -1:
				return CellState.White;
			default:
				log.warning("Invalid stone color: " + cellVal);
				return CellState.Void;
			}
		}

		private static String StateToToken(CellState state) {
			switch (state) {
			case Void:
				return "0";
			case Black:
				return "1";
			case White:
				return "-1";
			default:
				throw new InternalError("Invalid cell state");
			}
		}

		@Override
		public String encode() {
			StringBuffer sb = new StringBuffer(6 + 3 * Board.CELLS);
			sb.append("BOARD");
			for (int x = 0; x < Board.SIZE; x++) {
				for (int y = 0; y < Board.SIZE; y++) {
					sb.append(" ");
					sb.append(StateToToken(_board.get(x, y)));
				}
			}
			return sb.toString();
		}

		@Override
		public void received(Controller ctrl) {
			ctrl.updateBoard(_board);
		}
	}

	// TURN <playercolor>
	public static class TURN extends Command {
		int _code;

		public TURN(int code) {
			_code = code;
		}

		public int getCode() {
			return _code;
		}

		public static Command restore(String line) {
			try {
				int code = Integer.parseInt(line.substring(4).trim());
				return new TURN(code);
			} catch (NumberFormatException e) {
				log.warning(String.format("Invalid TURN command received [%s]", line));
				return Command.VOID;
			}
		}

		@Override
		public String encode() {
			return String.format("TURN %d", this.getCode());
		}

		@Override
		public void received(Controller ctrl) {
			ctrl.turnChange(this.getCode());
		}
	}

	// END
	// and follows game result
	public static class END extends Command {
		public static Command restore(String line) {
			return new END();
		}

		@Override
		public String encode() {
			return "END";
		}

		@Override
		public void received(Controller ctrl) {
			ctrl.endGame("");
		}
	}

	// START <playercolor>
	public static class START extends Command {
		int _code;

		public START(int code) {

		}

		public int getCode() {
			return _code;
		}

		public static Command restore(String line) {
			try {
				int code = Integer.parseInt(line.substring(4).trim());
				return new START(code);
			} catch (NumberFormatException e) {
				log.warning(String.format("Invalid START command received [%s]", line));
				return Command.VOID;
			}
		}

		@Override
		public String encode() {
			return String.format("START %d", this.getCode());
		}

		@Override
		public void received(Controller ctrl) {
			ctrl.startGame(this.getCode());
		}
	}

	// CLOSE
	public static class CLOSE extends Command {
		public static Command restore(String line) {
			return new CLOSE();
		}

		@Override
		public String encode() {
			return "CLOSE";
		}

		@Override
		public void received(Controller ctrl) {
			ctrl.close();
		}
	}

	public static class VoidCommand extends Command {

		@Override
		public String encode() {
			throw new InternalError("Void message cannot be encode");
		}

		@Override
		public void received(Controller ctrl) {
			log.warning("VoidMessage.received has been called.");
		}

	}

	public static final Command VOID = new VoidCommand();

	public class ServerError extends RuntimeException {
		private static final long serialVersionUID = -947711276850387192L;
		Command _msg;

		public ServerError(Command put_ERROR) {
			_msg = put_ERROR;
		}

	}
}
