package control;

import java.net.*;
import java.io.*;

public class TcpChannel extends Channel {
	Socket _sock;
	InputStream _socketInput;
	OutputStream _socketOutput;
	public TcpChannel(Socket sock){
		if(sock == null) throw new IllegalArgumentException("sock cannot be null");
		_sock = sock;
	}
	
	public Socket getSocket(){
		return _sock;
	}
	
	@Override
	public void write(byte[] data) {
		try {
			_socketOutput.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] read() {
		// TODO Auto-generated method stub
		return null;
	}

}
