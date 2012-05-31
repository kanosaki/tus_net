package smtp;

import control.*;

public class Smtp extends Protocol {
	@Override
	public void dispatch(String line) {
		// TODO Auto-generated method stub

	}

	public static class EHLO extends Command {
		@Override
		public Message getType() {
			return Messages.EHLO;
		}
	}

	public static class RCPT_TO extends Command {
		String _destinationAddr;
		public RCPT_TO(String to_addr){
			_destinationAddr = to_addr;
		}
		@Override
		public Message getType() {
			return Messages.RCPT_TO;
		}
	}

	public static class MAIL_FROM extends Command {
		@Override
		public Message getType() {
			return Messages.MAIL_FROM;
		}
	}

	public static class DATA extends Command {
		@Override
		public Message getType() {
			return Messages.DATA;
		}
	}

	public static class QUIT extends Command {
		@Override
		public Message getType() {
			return Messages.QUIT;
		}
	}
	
	public static enum Messages implements Message {
		EHLO, RCPT_TO, MAIL_FROM, DATA, QUIT,
	}
}
