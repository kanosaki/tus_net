package othello;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class CyclicIterator<T> implements Iterator<T> {
    private final Iterable<T> l;
    private Iterator<T> it;
    private boolean _empty;
    private T _last;

    public CyclicIterator(Iterable<T> l) {
        _empty = !l.iterator().hasNext();
        this.l = l;
        this.it = l.iterator();
    }

    @Override
    public boolean hasNext() {
        return !_empty;
    }

    public T peek() {
        return _last;
    }

    public T next() {
        T ret;

        if (!hasNext()) {
            throw new NoSuchElementException();
        } else if (it.hasNext()) {
            ret = it.next();
        } else {
            it = l.iterator();
            ret = it.next();
        }
        _last = ret;
        return ret;
    }

    public void remove() {
        it.remove();
    }
}