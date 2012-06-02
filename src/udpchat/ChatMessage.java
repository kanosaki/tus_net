package udpchat;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

public class ChatMessage {
	static InetAddress MY_ADDRES = null;
	static {
		try {
			MY_ADDRES = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	public ChatMessage(String msg, InetAddress addr, int port) {
		_message = msg;
		_fromAddr = addr;
		_fromPort = port;
	}

	private InetAddress _fromAddr;
	private int _fromPort;
	private String _message;

	public InetAddress getFromAddr() {
		return _fromAddr;
	}

	public int getFromPort() {
		return _fromPort;
	}

	public String getMessage() {
		return _message;
	}

	@Override
	public String toString() {
		String frm = _fromAddr == null || _fromAddr.equals(MY_ADDRES) ? "YOU"
				: _fromAddr.getHostAddress();
		return String.format("%15s > %s", frm, _message);
	}
	static Charset DecodeCharset = Charset.forName("utf-8");
	public static ChatMessage create(DatagramPacket udpPacket) {
		int port = udpPacket.getPort();
		InetAddress from = udpPacket.getAddress();
		ByteBuffer bbuf = ByteBuffer.wrap(udpPacket.getData(), 0,
				udpPacket.getLength());
		String msg = DecodeCharset.decode(bbuf).toString();
		return new ChatMessage(msg, from, port);
	}
}
