package example.Logger;

import example.Logger.Formatter.PatternFormatter;
import example.Logger.Formatter.Formatter;
import example.Logger.Formatter.JsonFormatter;
import example.Logger.Sinker.ConsoleSink;
import example.Logger.Sinker.FileSink;
import example.Logger.Sinker.MultiSink;
import example.Logger.Sinker.Sink;

import example.Logger.Configurator.Config;

import java.util.ArrayList;
import java.util.List;


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

    public static Logger initLoggerFromConfig(String name, Config config){
        Logger.setLevel(LogLevel.valueOf(config.getOr("log.level", "INFO")));
        String filePath = config.getOr("log.file", "app.log");
        List<Sink> sinks = new ArrayList<>();

        for(String s : config.getOr("log.sinks", "console").split(",")){
            switch (s.trim()){
                case "console": sinks.add(new ConsoleSink()); break;
                case "file":    sinks.add(new FileSink(filePath)); break;
                default: throw new IllegalArgumentException("Unknown sink: " + s.trim());
            }
        }

        Formatter formatter;
        if(config.getOr("log.format", "pattern").equals("json")){
            formatter = new JsonFormatter();
        }else{
            formatter = new PatternFormatter(config.getOr("log.pattern", "[%l] %n:  %m"));
        }

        return new Logger(name, formatter , new MultiSink(sinks));

    }
    
    public static void setLevel(LogLevel level)  {
        Logger.level = level;
    }

    public void log(LogLevel msgLevel, String message , Object... args)  {
        
        if(msgLevel.ordinal() < level.ordinal()) return ;
        sink.write(formatter.getMsg(msgLevel , name , message , args));
    }
}