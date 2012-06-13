package othello;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Signal<T> {

	private Queue<Listener<T>> _listeners;

	public Signal() {
		_listeners = new ConcurrentLinkedQueue<Listener<T>>();
	}

	public void fire(T value) {
		for (Listener<T> listener : _listeners) {
			listener.next(value);
		}
	}
	
	public void addListener(Listener<T> listener){
		_listeners.add(listener);
	}

	
}
