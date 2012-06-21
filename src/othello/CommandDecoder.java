package othello;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import othello.Board.CellState;

@Deprecated
public class CommandDecoder extends Model {
	private boolean _isServer;
	private Map<String, Decoder> _decoders;

	public CommandDecoder() {
		initDecoders();
	}

	public Command deocde(RemoteAdapter source, String line) throws DecodeError {
		int firstSpaceIndex = line.indexOf(' ');
		String header = line.substring(0, firstSpaceIndex);
		Decoder dec = _decoders.get(header);
		if (dec == null)
			throw new DecodeError("Unknown command :" + header);
		if (isServer())
			return dec.decodeServer(source, line.substring(firstSpaceIndex + 1));
		else
			return dec.decodeClient(source, line.substring(firstSpaceIndex + 1));
	}

	private Decoder[] getDecoders() {
		return new Decoder[] {
				new BOARD(),
				new CLOSE(),
				new END(), 
				new NICK(),
				new PUT(),
				new PUT_ERROR(),
				new SAY(),
				new START(),
				new TURN() };
	}

	private void initDecoders() {
		Decoder[] decoders = getDecoders();
		_decoders = new HashMap<String, Decoder>(decoders.length, 1);
		for (Decoder dec : getDecoders()) {
			_decoders.put(dec.getHeader(), dec);
		}
	}

	boolean isServer() {
		return _isServer;
	}

	void setServer(boolean isServer) {
		_isServer = isServer;
	}

	abstract class Decoder {
		public abstract String getHeader();

		public abstract Command decodeClient(RemoteAdapter source, String line) throws DecodeError;

		public abstract Command decodeServer(RemoteAdapter source, String line) throws DecodeError;

	}

	public class BOARD extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) {
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
			return new Command.BOARD(content);

		}

		private Board.CellState TokenToState(String token) {
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

		@Override
		public Command decodeServer(RemoteAdapter source, String line) throws DecodeError {
			throw new DecodeError("BOARD command is not supported in server mode");
		}

		@Override
		public String getHeader() {
			return "BOARD";
		}

	}

	public class CLOSE extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Command decodeServer(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getHeader() {
			return "CLOSE";
		}

	}

	public class END extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Command decodeServer(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getHeader() {
			return "END";
		}

	}

	public class NICK extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) throws DecodeError {
			throw new DecodeError("NICK command is not supported in client mode");
		}

		@Override
		public Command decodeServer(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getHeader() {
			return "NICK";
		}

	}

	public class PUT extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) throws DecodeError {
			throw new DecodeError("PUT command is not supported in client mode");
		}

		@Override
		public Command decodeServer(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getHeader() {
			return "PUT";
		}

	}

	public class PUT_ERROR extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Command decodeServer(RemoteAdapter source, String line) throws DecodeError {
			throw new DecodeError("ERROR command is not supported in server mode");
		}

		@Override
		public String getHeader() {
			return "ERROR";
		}

	}

	public class SAY extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Command decodeServer(RemoteAdapter source, String line) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getHeader() {
			return "SAY";
		}

	}

	public class START extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) {
			try {
				int code = Integer.parseInt(line.substring(5).trim());
				return new Command.START(code);
			} catch (NumberFormatException e) {
				getClassLogger().warning(String.format("Invalid START command received [%s]", line));
				return Command.VOID;
			}

		}

		@Override
		public Command decodeServer(RemoteAdapter source, String line) throws DecodeError {
			throw new DecodeError("START command is not supported in server mode");
		}

		@Override
		public String getHeader() {
			return "START";
		}

	}

	public class TURN extends Decoder {

		@Override
		public Command decodeClient(RemoteAdapter source, String line) {
			try {
				int code = Integer.parseInt(line.substring(4).trim());
				return new Command.TURN(code);
			} catch (NumberFormatException e) {
				getClassLogger().warning(String.format("Invalid TURN command received [%s]", line));
				return Command.VOID;
			}
		}

		@Override
		public Command decodeServer(RemoteAdapter source, String line) throws DecodeError {
			throw new DecodeError("TURN command is not supported in server mode");
		}

		@Override
		public String getHeader() {
			return "TURN";
		}

	}

}
