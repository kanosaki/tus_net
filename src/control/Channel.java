package control;

import java.nio.charset.Charset;

public abstract class Channel {
	Charset _charset;

	public void setCharset(String charsetName) {
		_charset = Charset.forName(charsetName);
	}

	public abstract void write(byte[] data);

	public abstract byte[] read();

	public void write(String line) {
		this.write(line.getBytes(_charset));
	}

}
