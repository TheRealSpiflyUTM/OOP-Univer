package com.example.Logger;

public class Logger {

    private String name;
    private Formatter formatter;
    private ConsoleSink sink;
    static LogLevel level = LogLevel.INFO;

    public Logger(String name) {
        this.name = name;
        this.formatter = new Formatter("");
        this.sink = new ConsoleSink();
    }

    public Logger(String name, Formatter formatter, ConsoleSink sink) {
        this.name = name;
        this.formatter = formatter;
        this.sink = sink;
    }

    public static void setLevel(LogLevel level)  {
        Logger.level = level;
    }

    public void log(LogLevel msgLevel, String message , Object... args)  {
        
        if(msgLevel.ordinal() < level.ordinal()) return ;
        sink.write(formatter.getFormatMessage(level , name , message , args));
    }
}