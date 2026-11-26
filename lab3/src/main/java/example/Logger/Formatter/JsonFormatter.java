package example.Logger.Formatter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONObject;

import example.Logger.LogLevel;

public class JsonFormatter implements Formatter {
    private final char msgSymbol = '%';
    private String format = "%t %l %n %m";
    public JsonFormatter(){
    }
    public JsonFormatter(String format){
        this.format = format;
    }

    // Helper Functions
    private static String getCurrentTime(){
        return LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }
    // This function was done by AI
    private static String escapeJson(String s) {
    if (s == null) return "";
    StringBuilder sb = new StringBuilder(s.length() + 8);
    for (int i = 0; i < s.length(); i++) {
        char c = s.charAt(i);
        switch (c) {
            case '"':  sb.append("\\\""); break;
            case '\\': sb.append("\\\\"); break;
            case '\n': sb.append("\\n");  break;
            case '\r': sb.append("\\r");  break;
            case '\t': sb.append("\\t");  break;
            case '\b': sb.append("\\b");  break;
            case '\f': sb.append("\\f");  break;
            default:
                if (c < 0x20) sb.append(String.format("\\u%04x", (int) c));
                else sb.append(c);
        }
    }
    return sb.toString();
}
    // Main Functions
    public StringBuilder getArgsMsg(String message, Object... args){
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

    public JSONObject convertToJsonFormat(LogLevel msgLevel, String name, String message){
        JSONObject jsonObj = new JSONObject();

        int formatLength = format.length();
        for(int i = 0 ; i < formatLength; i++){

            if(format.charAt(i) == msgSymbol){
                //check if it is the last i
                if(i + 1 >= formatLength){
                    break;
                } 
                char elem = format.charAt(i+1);
                i++;
                
                // Logic follows after checking if it is the last i
                if(elem == 't'){
                    jsonObj.put("time", getCurrentTime());
                }
                else if(elem == 'm'){
                    jsonObj.put("message", escapeJson(message));
                }
                else if(elem == 'n'){
                    jsonObj.put("logger", name);
                }
                else if(elem == 'l'){
                    jsonObj.put("level", msgLevel.toString());
                }
                else if(elem == 'r'){
                    jsonObj.put("random", RandomMsh.getRandomMsg(msgLevel));
                }
            }
        }
        return jsonObj;
    }

    public String getMsg(LogLevel msgLevel, String name, String message , Object... args){
        String preFinalMsg = getArgsMsg(message , args).toString();
        return convertToJsonFormat(msgLevel, name, preFinalMsg).toString();
    }

    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }

}
