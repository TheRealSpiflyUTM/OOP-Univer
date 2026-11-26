package example.Runner;


import example.Logger.Sinker.ConsoleSink;
import example.Logger.Sinker.FileSink;
import example.Logger.Sinker.MultiSink;
import example.Logger.Sinker.Sink;

import java.util.List;

import example.Logger.Logger;
import example.Logger.LogLevel;

import example.Logger.Formatter.JsonFormatter;

public class App 
{
    public static void main( String[] args )
    {
        Sink both = new MultiSink(List.of(new ConsoleSink(), new FileSink("app.log")));
        Logger log = new Logger("App-Class", new JsonFormatter(), both);

        log.log(LogLevel.INFO, "Yo gamer");

    }

}
