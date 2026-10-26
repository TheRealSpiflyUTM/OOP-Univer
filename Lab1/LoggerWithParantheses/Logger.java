// %t Current time
// %m The log message
// %n The name of the logger
// %l The level of the message
package LoggerWithParantheses;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

enum LogLevel{
    TRACE, INFO , WARN , ERROR
}

public class Logger {

    private String name;
    static LogLevel level = LogLevel.INFO;
    static String format = "[%l] %n: %m";
    static final char msgSymbol = '%';

    Logger(String name){
        this.name = name;
    }

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
    public static void trace(String message)  {
    }
    public static void info(String message)  {
    }
    public static void warn(String message)  {
    }
    public static void error(String message)  {
    }

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
        return response;
    }
}