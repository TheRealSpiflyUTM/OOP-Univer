package example.CarStation.Exceptions;

public class QueueFullException extends RuntimeException {
    public QueueFullException(String m) { super(m); }
}