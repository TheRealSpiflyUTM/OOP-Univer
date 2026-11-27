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

import example.Logger.Configurator.ConfigException;

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
        // level: unknown value -> ConfigException
        String levelStr = config.getOr("log.level", "INFO").trim().toUpperCase();
        try {
            Logger.setLevel(LogLevel.valueOf(levelStr));
        } catch (IllegalArgumentException e) {
            throw new ConfigException("Invalid log.level: '" + levelStr + "'");
        }

        // Choice for "file sink but no log.file": fall back to app.log (explain in report)
        String filePath = config.getOr("log.file", "app.log");
        List<Sink> sinks = new ArrayList<>();

        for (String s : config.getOr("log.sinks", "console").split(",")) {
            switch (s.trim()) {
                case "console": sinks.add(new ConsoleSink()); break;
                case "file":    sinks.add(new FileSink(filePath)); break;
                default: throw new ConfigException("Unknown sink: '" + s.trim() + "'");
            }
        }

        // format: only "pattern" or "json" allowed
        String format = config.getOr("log.format", "pattern").trim();
        Formatter formatter;
        switch (format) {
            case "json":    formatter = new JsonFormatter(); break;
            case "pattern": formatter = new PatternFormatter(config.getOr("log.pattern", "[%l] %n:  %m")); break;
            default: throw new ConfigException("Unknown log.format: '" + format + "'");
        }

        return new Logger(name, formatter, new MultiSink(sinks));

    }
    
    public static void setLevel(LogLevel level)  {
        Logger.level = level;
    }

    public void log(LogLevel msgLevel, String message , Object... args)  {
        
        if(msgLevel.ordinal() < level.ordinal()) return ;
        sink.write(formatter.getMsg(msgLevel , name , message , args));
    }
}