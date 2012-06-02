package control;

import java.util.Stack;

public abstract class CommandDispatcher {
	public CommandDispatcher() {
		_handlers = new Stack<CommandHandler>();
	}

	protected Stack<CommandHandler> _handlers;

	protected CommandHandler getFallbackHandler(Command com) {
		return CommandHandler.Void;
	}

	public abstract CommandHandler dispatchDefault(Command com);

	protected CommandHandler findFromStack(Command com) {
		CommandHandler result = null;
		for (CommandHandler handler : _handlers) {
			if (handler.canHandle(com)) {
				result = handler;
			}
		}
		if (result != null) {
			_handlers.remove(result);
		}
		return result;
	}

	public CommandHandler findHandler(Command com) {
		CommandHandler fromStack = this.findFromStack(com);
		if(fromStack != null)
			return fromStack;
		
		CommandHandler defaultHandler = dispatchDefault(com);
		if(defaultHandler != null)
			return defaultHandler;
		else
			return this.getFallbackHandler(com);
	}

}
