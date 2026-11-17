package Logger;

public class Logger {

    private String name;
    private Formatter formatter;
    static LogLevel level = LogLevel.INFO;

    public Logger(String name, Formatter formatter){
        this.name = name;
        this.formatter = formatter;
    }

    public static void setLevel(LogLevel level)  {
        Logger.level = level;
    }

    public void log(LogLevel msgLevel, String message)  {
        
        if(msgLevel.ordinal() < level.ordinal()) return ;
        
        String outputMsg = formatter.getFormatMessage(level , name , message);

        System.out.println(outputMsg);
    }
}