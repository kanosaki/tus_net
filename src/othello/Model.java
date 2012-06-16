package othello;

import java.lang.reflect.Type;
import java.util.logging.Logger;

public abstract class Model {
	static String DEFAULT_LOGGER_NAME = Type.class.getName();
	private static Logger _classLogger = Logger.getLogger(DEFAULT_LOGGER_NAME);

	private Logger _instanceLogger;
	protected static Logger getClassLogger(){
		return _classLogger;
	}
	protected Logger getLog(){
		if(_instanceLogger == null)
			return _classLogger;
		else
			return _instanceLogger;
	}
	public void setLoggerName(String name){
		_instanceLogger = Logger.getLogger(DEFAULT_LOGGER_NAME + "#" + name); 
	}
}
