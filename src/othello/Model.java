package othello;

import java.lang.reflect.Type;
import java.util.logging.Logger;

public abstract class Model {
    private static Logger _classLogger;

    public Model() {
        _classLogger = Logger.getLogger(getClass().getSimpleName());
    }

    private Logger _instanceLogger;

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
        _instanceLogger = Logger.getLogger(name);
    }
}
