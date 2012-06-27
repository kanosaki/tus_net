package othello;

public abstract class RunnableContainer<T> implements Runnable {
    private T _arg;

    public RunnableContainer(T arg) {
        _arg = arg;
    }

    @Override
    public void run() {
        work(_arg);
    }

    abstract void work(T arg);

    public abstract static class Two<T1, T2> implements Runnable {
        private T1 _arg1;
        private T2 _arg2;

        public Two(T1 arg1, T2 arg2) {
            _arg1 = arg1;
            _arg2 = arg2;
        }

        @Override
        public void run() {
            work(_arg1, _arg2);
        }

        abstract void work(T1 arg1, T2 arg2);
    }
}
