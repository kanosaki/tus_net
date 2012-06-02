package control;

import java.nio.charset.Charset;

public abstract class Channel {
	Charset _charset;

	public void setCharset(String charsetName) {
		_charset = Charset.forName(charsetName);
	}

	public abstract void write(byte[] data);

	public abstract byte[] read();

}
