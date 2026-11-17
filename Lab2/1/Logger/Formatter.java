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

    // Helper Functions
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

    // Main Functions
    public StringBuilder getArgs(String message, Object... args){
        if(args == null || args.length == 0) return  new StringBuilder(message);

        StringBuilder outMsg = new StringBuilder();
        for(int i = 0 ; i < message.length() ; i++){              
            char c = message.charAt(i);
            if(c == '{'){
                StringBuilder insideChecker = new StringBuilder("");
                i++;
                for( ; i < message.length() ; i++ ){
                    if(message.charAt(i) == '}') {
                        break;
                    }
                    insideChecker.append(message.charAt(i));
                }
                try{
                    int idx = Integer.parseInt(insideChecker.toString());
                    if(idx >= 0 && idx < args.length){
                        outMsg.append(String.valueOf(args[idx]));
                    }
                    else{
                        outMsg.append('{').append(insideChecker).append('}');
                    }
                }catch(NumberFormatException e){
                    outMsg.append("{" + insideChecker.toString() + "}");
                    // Print an error if you want, I dont want to 
                }

            }
            else{
                outMsg.append(message.charAt(i));
            }
        }
        return outMsg;
    }

    public StringBuilder getFinalMessage(LogLevel msgLevel, String name, String message){
        StringBuilder outputMsg = new StringBuilder("");
        int msgLength = format.length();
        for(int i = 0 ; i < msgLength ; i++){

            if(format.charAt(i) == msgSymbol){
                //check if it is the last i
                if(i + 1 >= msgLength){
                    outputMsg.append(format.charAt(i));
                    break;
                } 
                // Logic follows after checking if it is the last i

                String outputElement = checkForFormat(msgLevel, format.charAt(i+1) , message, name);
                outputMsg.append(outputElement);
                i++;
            }
            else {

                outputMsg.append(format.charAt(i));
            }
        }
        return outputMsg;
    }

    public StringBuilder getFormatMessage(LogLevel msgLevel, String name, String message , Object... args){
        String preFinalMsg = getArgs(message , args).toString();
        return getFinalMessage(msgLevel, name, preFinalMsg);
    }
}
