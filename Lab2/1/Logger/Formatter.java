package Logger;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Formatter {
    
    private String format;
    private char msgSymbol = '%';

    public Formatter(String format){
        // set default value 
        if(format == "") this.format = "[%l] %n:  %m | Random: %r";
        else this.format = format;
    }
    public void setSymbol(char sym){
        msgSymbol = sym;
    }
    public void setFormat(String format)  {
        this.format = format;
    }

    public String getFormat(LogLevel msgLevel, String name, String message){
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

                String outputElement = checkForFormat(msgLevel, format.charAt(i+1) , message, name);
                outputMsg += outputElement;

                i++;
            }
            else {

                outputMsg += format.charAt(i);
            }
        }
        return outputMsg;
    }

    private static String getCurrentTimne(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    
    private String checkForFormat(LogLevel msgLevel, char elem, String message, String name){

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
}
