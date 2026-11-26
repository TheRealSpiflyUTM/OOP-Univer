package example.Logger;

import example.Logger.Formatter.PatternFormatter;
import example.Logger.Formatter.Formatter;

import example.Logger.Sinker.ConsoleSink;
import example.Logger.Sinker.Sink;

public class Logger {

    private String name;
    private Formatter formatter;
    private Sink sink;
    static LogLevel level = LogLevel.INFO;
    
    public Logger(String name, Formatter formatter, Sink sink) {
        this.name = name;
        this.formatter = formatter;
        this.sink = sink;
    }
    public Logger(String name, Formatter formatter) {
        this.name = name;
        this.formatter = formatter;
        this.sink = new ConsoleSink();
    }
    public Logger(String name, Sink sink) {
        this.name = name;
        this.formatter = new PatternFormatter("");
        this.sink = sink;
    }
    public Logger(String name) {
        this.name = name;
        this.formatter = new PatternFormatter("");
        this.sink = new ConsoleSink();
    }

    public static void setLevel(LogLevel level)  {
        Logger.level = level;
    }

    public void log(LogLevel msgLevel, String message , Object... args)  {
        
        if(msgLevel.ordinal() < level.ordinal()) return ;
        sink.write(formatter.getMsg(msgLevel , name , message , args));
    }
}