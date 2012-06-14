package othello;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Debug {
	private static final Logger log = Logger.getLogger("DebugConsole");
	private static Debug _instance;
	private DebugFrame _frame;
	private LogHandler _logHandler;

	private Debug() {
		this.initLogger();
		log.finest("Debug Console Ready.");
	}

	public static Debug getInstance() {
		if(_instance == null)
			_instance = new Debug();
		return _instance;
	}
	
	public static Debug initialize() {
		return Debug.getInstance();
	}

	private void initFrame() {
		if (_frame == null)
			_frame = new DebugFrame();
	}

	private void initLogger() {
		_logHandler = new LogHandler();
		Logger rootLogger = Logger.getLogger("");
		rootLogger.addHandler(_logHandler);
	}

	public void showFrame() {
		this.initFrame();
		_frame.setVisible(true);
	}

	public DebugFrame getFrame() {
		this.initFrame();
		return _frame;
	}

	class LogHandler extends java.util.logging.Handler {

		@Override
		public void close() throws SecurityException {
		}

		@Override
		public void flush() {

		}

		@Override
		public void publish(LogRecord record) {
			getFrame().pushLog(record);

		}

	}
}
