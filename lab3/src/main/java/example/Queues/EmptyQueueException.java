package example.CarStation.Exceptions;

public class EmptyQueueException extends RuntimeException {
    public EmptyQueueException() { super("queue is empty"); }
}