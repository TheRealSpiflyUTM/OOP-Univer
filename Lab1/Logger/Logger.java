package Logger;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Logger {

    private String name;
    static LogLevel level = LogLevel.INFO;
    static String format = "[%l] %n:  %m | Random: %r";
    static private char msgSymbol = '%';

    public Logger(String name ){
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

    public void log(LogLevel msgLevel, String message)  {
        
        if(msgLevel.ordinal() < level.ordinal()){
            return ;
        }

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

                String outputElement = checkForFormat(msgLevel, format.charAt(i+1) , message);
                outputMsg += outputElement;
                i++;

            }
            else {

                outputMsg += format.charAt(i);
            }
        }

        System.out.println(outputMsg);
    }

    private static String getCurrentTimne(){
        String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        return currentTime;
    }
    private String checkForFormat(LogLevel msgLevel, char elem, String message){

        String response = "" + msgSymbol + elem;
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
            response = msgLevel.toString();
        }
        else if(elem == 'r'){
            response = RandomMsh.forLevel(msgLevel);
        }
        return response;
    }
    // This line has a reasone, check the number of lines 
}