package example.Queues;

import example.CarStation.Exceptions.EmptyQueueException;

public class ArrayQueue<T> implements Queue<T> {
    private Object[] data = new Object[5];
    private int head = 0, count = 0;
    public void enqueue(T item) {
        if (count == data.length) {
            Object[] bigger = new Object[data.length * 2];
            for (int i = 0; i < count; i++) bigger[i] = data[(head + i) % data.length];
            data = bigger; head = 0;
        }
        data[(head + count) % data.length] = item;
        count++;
    }

    public T dequeue() {
        if (count == 0) throw new EmptyQueueException();
        T v = (T) data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        count--;
        return v;
    }
    public int size() { return count; }
    public boolean isEmpty() { return count == 0; }
}