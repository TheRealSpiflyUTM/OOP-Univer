package WorkingLogger;
public class Main {
    public static void main(String[] args) {
        Logger logger = new Logger("Parentheses");
        logger.log(LogLevel.ERROR, "Invalid input");
    }
}