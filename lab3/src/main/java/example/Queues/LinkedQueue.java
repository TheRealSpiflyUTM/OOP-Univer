package example.Queues;

import example.CarStation.Exceptions.EmptyQueueException;

public class LinkedQueue<T> implements Queue<T> {
    private static class Node<T> { T val; Node<T> next; Node(T v) { val = v; } }
    private Node<T> head, tail;
    private int size = 0;
    public void enqueue(T item) {
        Node<T> n = new Node<>(item);
        if (tail == null) head = tail = n; 
        else { 
            tail.next = n; 
            tail = n; 
        }
        size++;
    }
    public T dequeue() {
        if (head == null) throw new EmptyQueueException();
        T v = head.val;
        head = head.next;
        if (head == null) tail = null;
        size--;
        return v;
    }
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
}