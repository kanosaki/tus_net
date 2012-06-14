package othello;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Debug {
	private static final Logger log = Logger.getLogger("DebugConsole");
	private static Debug _instance = new Debug();

	public static Debug getInstance() {
		return _instance;
	}

	class LogHandler extends java.util.logging.Handler {

		@Override
		public void close() throws SecurityException {
			// TODO Auto-generated method stub
		}

		@Override
		public void flush() {
			// TODO Auto-generated method stub

		}

		@Override
		public void publish(LogRecord record) {
			// TODO Auto-generated method stub

		}

	}
}
