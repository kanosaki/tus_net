package othello;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Signal<T> {
    private static ExecutorService _pool = Executors.newCachedThreadPool();
    private Queue<Listener<T>> _listeners;

    public Signal() {
        _listeners = new ConcurrentLinkedQueue<Listener<T>>();
    }

    public synchronized void fire(T value) {
        for (Listener<T> listener : _listeners) {
            listener.next(value);
        }
    }

    public synchronized void fireAsync(T value) {
        _pool.execute(new RunnableContainer<T>(value) {
            @Override
            void work(T arg) {
                fire(arg);
            }
        });
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
            if (!prev.equals(listener)) {
                newListeners.add(prev);
            }
        }
        _listeners = newListeners;
    }
}
