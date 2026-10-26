// %t Current time
// %m The log message
// %n The name of the logger
// %l The level of the message
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

enum LogLevel{
    TRACE, INFO , WARN , ERROR
}

public class Logger {

    private String name;
    static LogLevel level = LogLevel.INFO;
    static String format = "[%l] %n: %m";
    static private char msgSymbol = '%';

    Logger(String name){
        this.name = name;
    }
    // Reseters

    public static void resetFormatToDefault(){
        setFormat("[%l] %n: %m");
    }
    public static void changeTheLogSymbol(char symbol){
        msgSymbol = symbol;
    }

    // Setters

    public static void setLevel(LogLevel level)  {
        Logger.level = level;
    }
    public static void setFormat(String format)  {
        Logger.format = format;
    }

    public void log(LogLevel level, String message)  {
        
        String outputMsg = "";
        int msgLength = format.length();
        for(int i = 0 ; i < msgLength ; i++){

            if(format.charAt(i) == msgSymbol){
                //check if it is the last i
                if(i + 1 >= msgLength){
                    outputMsg += format.charAt(i);
                    break;
                } 
                // Logic follows after checking if it is the last i

                String outputElement = checkForFormat(format.charAt( i+1 ) , message, level);
                outputMsg += outputElement;
                i++;

            }
            else {

                outputMsg += format.charAt(i);
            }
        }

        System.out.println(outputMsg);
    }

    // #region Random Text Creating Region

    private String returnRandomFromPrases(String[] prases){
        Random random = new Random();        
        String word = prases[random.nextInt(prases.length)];
        return (word);
    }
    private String returnRandomTrace()  {
        String[] prases = 
        {
        "Entering a method. Send snacks if I don't return.",
        "Following the code. It also looks lost.",
        "Loop iteration 42: morale is declining.",
        "Checking a variable that was fine five milliseconds ago.",
        "Leaving breadcrumbs through the spaghetti."
        };
        return returnRandomFromPrases(prases);

    }
    private String returnRandomInfo()  {
        String[] prases = {
        "Everything works. Nobody touch anything.",
        "Application started. Coffee dependency satisfied.",
        "Task completed. Taking full credit.",
        "Successfully did the thing.",
        "All systems operational. Suspicious."
        };
        return returnRandomFromPrases(prases);
    }
    private String returnRandomWarn()  {
        String[] prases = {
        "This is fine. Probably.",
        "Memory is getting tight. Try forgetting your mistakes.",
        "Something smells wrong, and it isn't the CPU.",
        "You are one questionable decision away from an ERROR."
        };
        return returnRandomFromPrases(prases);
    }
    private String returnRandomError()  {
        String[] prases = {
        "Task failed successfully.",
        "NullPointerException: your hopes pointed to nothing.",
        "Something broke. Blaming the last person who committed.",
        "FAHHHHHHHHHHHHHHHHHH!",
        "Unexpected error. The expected errors were busy.",
        "Womp Womp"
        };
        return returnRandomFromPrases(prases);
    }

    private String theRandomInformation(){
        String response = "";
        if(level == LogLevel.TRACE){
            response = returnRandomTrace();
        }
        else if(level == LogLevel.INFO){
            response = returnRandomInfo();
        }
        else if(level == LogLevel.WARN){
            response = returnRandomWarn();
        }
        else if(level == LogLevel.ERROR){
            response = returnRandomError();
        }
        return response;
    }

    // #endregion
    
    private static String getCurrentTimne(){
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return currentTime;
    }
    private String checkForFormat(char elem, String message, LogLevel meesageLevel){

        String response = String.valueOf(msgSymbol + elem);
        if(elem == 't'){
            response = getCurrentTimne();
        }
        else if(elem == 'm'){
            response = message;
        }
        else if(elem == 'n'){
            response = name;
        }
        else if(elem == 'l'){
            response = meesageLevel.toString();
        }
        else if(elem == 'r'){
            response = theRandomInformation();
        }
        return response;
    }
}