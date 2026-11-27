package example.CarStation;

import example.CarStation.Exceptions.EmptyQueueException;
import example.CarStation.Exceptions.QueueFullException;

public class CircularQueue<T> implements Queue<T> {
    private final Object[] data;
    private int head = 0, count = 0;
    public CircularQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        data = new Object[capacity];
    }
    public void enqueue(T item) {
        if (isFull()) throw new QueueFullException("queue full (" + data.length + ")");
        data[(head + count) % data.length] = item;
        count++;
    }
    public T dequeue() {
        if (isEmpty()) throw new EmptyQueueException();
        T item = (T) data[head];
        data[head] = null;
        head = (head + 1) % data.length;
        count--;
        return item;
    }
    public int size() { return count; }
    public boolean isEmpty() { return count == 0; }
    @Override public boolean isFull() { return count == data.length; }

}
