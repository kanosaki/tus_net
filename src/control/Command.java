package control;

import java.nio.charset.Charset;

public abstract class Command {
	public abstract Message getType();

	public abstract byte[] pack();

	public static class StringLine extends Command {
		String _value;
		Charset _charset;
		public StringLine(String line){
			_value = line;
			_charset = Charset.defaultCharset();
		}
		@Override
		public Message getType() {
			return Message.Default.StringLine;
		}

		@Override
		public byte[] pack() {
			return _charset.encode(_value).array();
		}
		
		protected void setValue(String line){
			_value = line;
		}
		protected String getValue(){
			return _value;
		}
		protected Charset getCharset(){
			return _charset;
		}
		protected void setCharset(Charset charset){
			_charset = charset;
		}
		protected byte[] encode(String value){
			return this.getCharset().encode(value).array();
		}
	}
}
