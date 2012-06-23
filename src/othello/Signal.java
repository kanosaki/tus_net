package othello;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Signal<T> {

    private Queue<Listener<T>> _listeners;

    public Signal() {
        _listeners = new ConcurrentLinkedQueue<Listener<T>>();
    }

    public synchronized void fire(T value) {
        for (Listener<T> listener : _listeners) {
            listener.next(value);
        }
    }

    public synchronized void addListener(Listener<T> listener) {
        if (listener != null) {
            _listeners.add(listener);
            listener.onAttatch();
        }
    }

    public synchronized void removeListener(Listener<T> listener) {
        if (listener == null)
            throw new IllegalArgumentException();
        Queue<Listener<T>> newListeners = new ConcurrentLinkedQueue<Listener<T>>();
        for (Listener<T> prev : _listeners) {
            if(!prev.equals(listener)){
                newListeners.add(prev);
            }
        }
        _listeners = newListeners;
    }
}
