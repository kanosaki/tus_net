package smtp;

import control.*;

public class Smtp extends Protocol {
	@Override
	public void dispatch(String line) {

	}

	public static class EHLO extends Command.StringLine {
		public EHLO() {
			super("EHLO");
		}

		@Override
		public Message getType() {
			return Messages.EHLO;
		}
	}

	public static class RCPT_TO extends Command.StringLine {
		public RCPT_TO(String to_addr){
			super("RCPT TO " + to_addr);
		}
		@Override
		public Message getType() {
			return Messages.RCPT_TO;
		}
	}

	public static class MAIL_FROM extends Command.StringLine {
		public MAIL_FROM(String addr) {
			super("MAIL FROM " + addr);
		}

		@Override
		public Message getType() {
			return Messages.MAIL_FROM;
		}
	}

	public static class DATA extends Command.StringLine {
		String _body;
		public DATA(String body){
			super(DATA.buildData(body));
		}
		@Override
		public Message getType() {
			return Messages.DATA;
		}
		
		static String buildData(String body){
			StringBuffer sb = new StringBuffer();
			sb.append("DATA\n");
			for(String line : body.split("\n")){
				// In SMTP DATmA section, you must escape '.'(dot) at line head by prepending '.'
				if(line.startsWith(".")) sb.append("."); 
				sb.append(line + "\n");
			}
			sb.append(".\n");
			return sb.toString();
		}
		
		@Override
		public byte[] pack() {
			return this.encode(this.getValue());
		}
	}

	public static class QUIT extends Command.StringLine {
		public QUIT() {
			super("QUIT");
		}

		@Override
		public Message getType() {
			return Messages.QUIT;
		}
	}
	
	public static enum Messages implements Message {
		EHLO, RCPT_TO, MAIL_FROM, DATA, QUIT,
	}
}
