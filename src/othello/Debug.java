package othello;

import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class Debug extends Model {
    private static final String DEFAULT_LOGGER_NAME = "DebugConsole";
    private static Logger _classLogger = Logger.getLogger(DEFAULT_LOGGER_NAME);
    
    private Logger _instanceLogger;
    private Signal<LogRecord> _onNextLog;
    
    protected static Logger getClassLogger() {
        return _classLogger;
    }

    protected Logger getLog() {
        if (_instanceLogger == null)
            return _classLogger;
        else
            return _instanceLogger;
    }

    public void setLoggerName(String name) {
        _instanceLogger = Logger.getLogger(DEFAULT_LOGGER_NAME + "#" + name);
    }

    private static Debug _instance;
    private DebugFrame _frame;
    private LogHandler _logHandler;

    Debug(boolean withFrame) {
        if (withFrame)
            this.initFrame();
        this.initLogger();
        getLog().finest("Debug Console Ready.");
        _onNextLog = new Signal<LogRecord>();
    }

    public static Debug getInstance() {
        if (_instance == null)
            _instance = new Debug(false);
        return _instance;
    }

    static void setInstance(Debug dbg) {
        _instance = dbg;
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
        if (_frame != null)
            _frame.setVisible(true);
    }

    public DebugFrame getFrame() {
        return _frame;
    }
    
    public void addOnNextListener(Listener<LogRecord> listener){
        _onNextLog.addListener(listener);
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
            DebugFrame frame = getFrame();
            if(frame != null)
                frame.pushLog(record);
            _onNextLog.fire(record);
        }

    }
}
