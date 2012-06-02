package control;

import java.net.*;

public abstract class Protocol {
	
	CommandHandler _nextHandle;
	InetAddress _remoteAddr;
	public abstract void dispatch(String line);
	
	
	
	public InetAddress getRemoteAddr(){
		return _remoteAddr;
	}
	
}
