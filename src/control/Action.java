package control;

public interface Action<T> {
	void call(T obj);
}
