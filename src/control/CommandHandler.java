package control;

public abstract class CommandHandler<T extends ChannelContext> {
	public static final CommandHandler<ChannelContext> Void = new VoidHandlerImpl();
	public abstract boolean canHandle(Command com);
	public abstract boolean isPersistent();
	
	public static class VoidHandlerImpl extends CommandHandler<ChannelContext> {

		@Override
		public boolean canHandle(Command com) {
			return false;
		}

		@Override
		public void handle(Command com) {
			
		}

		@Override
		public boolean isPersistent() {
			return true;
		}
		
	}

	public abstract void handle(Command com);
}
