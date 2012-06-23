package othello;

import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.regex.*;

import othello.Board.CellState;

public abstract class Command extends Model {

	public static Command decode(String line, RemoteAdapter sender) throws DecodeError {
		if (line == null)
			throw new DecodeError("Null argument");
		if (line.startsWith("BOARD"))
			return BOARD.restore(line);
		if (line.startsWith("TURN"))
			return TURN.restore(line);
		if (line.startsWith("SAY"))
			return SAY.restore(sender, line);
		if (line.startsWith("PUT"))
			return PUT.restore(sender, line);
		if (line.startsWith("END"))
			return END.restore(line);
		if (line.startsWith("START"))
			return START.restore(line);
		if (line.startsWith("CLOSE"))
			return CLOSE.restore(line);
		if (line.startsWith("NICK"))
			return NICK.restore(sender, line);
		if (line.startsWith("ERROR"))
			return PUT_ERROR.restore(line);
		throw new DecodeError("Unknown command");
	}

	public abstract String encode();

	public abstract void receivedClient(Controller ctrl);

	public abstract void receivedServer(Lobby lobby);

	// NICK <username>
	public static class NICK extends Command {
		private String _name;
		protected RemoteAdapter _sender;

		public NICK(String name, RemoteAdapter sender) {
			_name = name;
			_sender = sender;
		}

		public String getName() {
			return _name;
		}

		public static Command restore(RemoteAdapter sender, String line) {
			return new NICK(line.substring(4).trim(), sender);
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
		public void receivedClient(Controller ctrl) {
			getLog().warning("Client mode not supports " + this);
		}

		@Override
		public void receivedServer(Lobby lobby) {
			lobby.setNickName(_sender, getName());
		}
	}

	// SAY <message>
	// SAY <username>:<message>
	public static class SAY extends Command {
		static final Pattern SAY_RECV_PATTERN = Pattern.compile("SAY <(.+?)>:(.*)");
		RemoteAdapter _sender;

		public SAY(String message, RemoteAdapter sender) {
			_message = message;
			_sender = sender;
		}

		protected String _message;

		public String getMessage() {
			return _message;
		}

		@Override
		public String toString() {
			return String.format("<SAY: %s>", this.getMessage());
		}

		public static Command restore(RemoteAdapter sender, String line) {
			Matcher recv = SAY_RECV_PATTERN.matcher(line);
			if (recv.matches()) {
				return new SAY_RECV(recv.group(1), recv.group(2), sender);
			} else {
				return new SAY(line.substring(3).trim(), sender);
			}
		}

		public static class SAY_RECV extends SAY {
			private String _user;

			public String getUser() {
				return _user;
			}

			public SAY_RECV(String user, String msg, RemoteAdapter sender) {
				super(msg, sender);
				_user = user;
			}

			@Override
			public String toString() {
				return String.format("<SAY: %s from %s>", this.getMessage(), this.getUser());
			}

			@Override
			public String encode() {
				return String.format("SAY <%s>:%s", this.getUser(), this.getMessage());
			}

			@Override
			public void receivedClient(Controller ctrl) {
				ctrl.said(this.getUser(), this.getMessage());
			}

			@Override
			public void receivedServer(Lobby lobby) {
				getLog().warning("Server has received named say command.");
			}
		}

		@Override
		public String encode() {
			return String.format("SAY %s", this.getMessage());
		}

		@Override
		public void receivedClient(Controller ctrl) {
			getLog().warning("Client mode not supports " + this);
		}

		@Override
		public void receivedServer(Lobby lobby) {
			lobby.said(_sender, getMessage());
		}

	}

	// PUT <x> <y>
	// PUT ERROR <errno>
	public static class PUT extends Command {
		// TODO: Check format
		static final Pattern PUT_PATTERN = Pattern.compile("PUT (\\d) (\\d)"); //

		private int _x, _y;
		private RemoteAdapter _sender;

		public PUT(int x, int y, RemoteAdapter sender) {
			_x = x;
			_y = y;
			_sender = sender;
		}

		public int getX() {
			return _x;
		}

		public int getY() {
			return _y;
		}

		public static Command restore(RemoteAdapter sender, String line) {
			Matcher put = PUT_PATTERN.matcher(line);
			if (put.matches()) {
				int x = Integer.parseInt(put.group(1));
				int y = Integer.parseInt(put.group(2));
				return new PUT(x, y, sender);
			} else {
				getClassLogger().warning(String.format("Invalid PUT command received. [%s]", line));
				return Command.VOID;
			}
		}

		@Override
		public String toString() {
			return String.format("<PUT (%d, %d)>", getX(), getY());
		}

		@Override
		public String encode() {
			return String.format("PUT %s %s", this.getX(), this.getY());
		}

		@Override
		public void receivedClient(Controller ctrl) {
			getLog().warning("Client mode not supports " + this);
		}

		@Override
		public void receivedServer(Lobby lobby) {
			lobby.put(_sender, _x, _y);
		}

	}

	public static class PUT_ERROR extends Command {
		public PUT_ERROR(int code) {
			_code = code;
		}

		public static Command restore(String line) {
			try {
				int code = Integer.parseInt(line.substring(5).trim());
				return new PUT_ERROR(code);
			} catch (NumberFormatException e) {
				getClassLogger().warning(String.format("Invalid ERROR command received [%s]", line));
				return Command.VOID;
			}
		}

		private int _code;
		static final String[] _errorDetail = { "Error 0 is Undefined.", "ERROR 1: Invalid format",
				"ERROR 2: Invalid position on PUT", "ERROR 3: Attempted to PUT on hostiles turn",
				"ERROR 4: Unknown request" };

		@Override
		public String toString() {
			return String.format("<%s>", _errorDetail[_code]);
		}

		@Override
		public String encode() {
			return String.format("ERROR %d", this.getCode());
		}

		public int getCode() {
			return _code;
		}

		@Override
		public void receivedClient(Controller ctrl) {
			ctrl.error(getCode());
		}

		@Override
		public void receivedServer(Lobby lobby) {
			getLog().warning("Server mode not supports " + this);
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
				getClassLogger().warning(
						String.format("Invalid BOARD data, content length expecded %d but received %d", Board.CELLS,
								tokenCount));
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
				getClassLogger().warning("Invalid stone color: " + cellVal);
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
		public void receivedClient(Controller ctrl) {
			ctrl.updateBoard(_board);
		}

		@Override
		public String toString() {
			return "<BOARD>";
		}

		@Override
		public void receivedServer(Lobby lobby) {
			getLog().warning("Server mode not supports " + this);
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
				getClassLogger().warning(String.format("Invalid TURN command received [%s]", line));
				return Command.VOID;
			}
		}

		@Override
		public String encode() {
			return String.format("TURN %d", this.getCode());
		}

		@Override
		public void receivedClient(Controller ctrl) {
			ctrl.turnChange(this.getCode());
		}

		@Override
		public String toString() {
			return String.format("<TURN %d>", this.getCode());
		}

		@Override
		public void receivedServer(Lobby lobby) {
			getLog().warning("Server mode not supports " + this);
		}
	}

	// END
	// and follows game result
	public static class END extends Command {
		static final Pattern END_PATTERN = Pattern.compile("END (.*)");
		String _message;

		public END(String msg) {
			_message = msg;
		}

		public String getMessage() {
			return _message;
		}

		public static Command restore(String line) {
			Matcher match = END_PATTERN.matcher(line);
			if (match.matches()) {
				return new END(match.group(1));
			} else {
				return Command.VOID;
			}
		}

		@Override
		public String encode() {
			return "END " + _message;
		}

		@Override
		public void receivedClient(Controller ctrl) {
			ctrl.endGame(getMessage());
		}

		@Override
		public String toString() {
			return String.format("<END>");
		}

		@Override
		public void receivedServer(Lobby lobby) {
			getLog().warning("Server mode not supports " + this);
		}
	}

	// START <playercolor>
	public static class START extends Command {
		int _code;

		public START(int code) {
			_code = code;
		}

		public int getCode() {
			return _code;
		}

		public static Command restore(String line) {
			try {
				int code = Integer.parseInt(line.substring(5).trim());
				return new START(code);
			} catch (NumberFormatException e) {
				getClassLogger().warning(String.format("Invalid START command received [%s]", line));
				return Command.VOID;
			}
		}

		@Override
		public String encode() {
			return String.format("START %d", this.getCode());
		}

		@Override
		public void receivedClient(Controller ctrl) {
			ctrl.startGame(this.getCode());
		}

		@Override
		public String toString() {
			return String.format("<START %d>", this.getCode());
		}

		@Override
		public void receivedServer(Lobby lobby) {
			getLog().warning("Server mode not supports " + this);
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
		public void receivedClient(Controller ctrl) {
			ctrl.close();
		}

		@Override
		public String toString() {
			return "<CLOSE>";
		}

		@Override
		public void receivedServer(Lobby lobby) {
			getLog().warning("Server mode not supports " + this);
		}
	}

	public static class VoidCommand extends Command {

		@Override
		public String encode() {
			throw new InternalError("Void message cannot be encode");
		}

		@Override
		public void receivedClient(Controller ctrl) {
			getLog().warning("VoidMessage.receivedClient has been called.");
		}

		@Override
		public String toString() {
			return "<VOID>";
		}

		@Override
		public void receivedServer(Lobby lobby) {
			getLog().warning("VoidMessage.receivedServer has been called.");
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
