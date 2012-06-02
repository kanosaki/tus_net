package udpchat;

import java.util.EventListener;

public interface MessageArrivedListener extends EventListener{
	void messageArrived(ChatMessage msg);
}
