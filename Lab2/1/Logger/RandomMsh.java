package Logger;

import java.util.Random;

final class RandomMsh {

    private static final Random random = new Random();

    private RandomMsh() {}

    private static final String[] TRACE = {
        "Entering a method. Send snacks if I don't return.",
        "Following the code. It also looks lost.",
        "Loop iteration 42: morale is declining.",
        "Checking a variable that was fine five milliseconds ago.",
        "Leaving breadcrumbs through the spaghetti."
    };

    private static final String[] INFO = {
        "Everything works. Nobody touch anything.",
        "Application started. Coffee dependency satisfied.",
        "Task completed. Taking full credit.",
        "Successfully did the thing.",
        "All systems operational. Suspicious."
    };

    private static final String[] WARN = {
        "This is fine. Probably.",
        "Memory is getting tight. Try forgetting your mistakes.",
        "Something smells wrong, and it isn't the CPU.",
        "You are one questionable decision away from an ERROR."
    };

    private static final String[] ERROR = {
        "Task failed successfully.",
        "NullPointerException: your hopes pointed to nothing.",
        "Something broke. Blaming the last person who committed.",
        "FAHHHHHHHHHHHHHHHHHH!",
        "Unexpected error. The expected errors were busy.",
        "Womp Womp"
    };

    private static String pick(String[] phrases) {
        return phrases[random.nextInt(phrases.length)];
    }

    static String forLevel(LogLevel level) {
        switch (level) {
            case TRACE: return pick(TRACE);
            case INFO:  return pick(INFO);
            case WARN:  return pick(WARN);
            case ERROR: return pick(ERROR);
            default:    return "";
        }
    }
}
