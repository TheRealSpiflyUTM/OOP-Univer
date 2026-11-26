package example.Runner;


import example.Logger.Sinker.ConsoleSink;
import example.Logger.Sinker.FileSink;
import example.Logger.Sinker.MultiSink;
import example.Logger.Sinker.Sink;

import java.io.IOException;
import java.util.List;


import example.Logger.Logger;
import example.Logger.LogLevel;
import example.Logger.Configurator.Config;

import example.Logger.Formatter.JsonFormatter;

public class App 
{
    public static void main( String[] args )
    {
        Config config = null;
        try{
            config = new Config("LoggerConfig.conf");
        }catch(IOException e){
            System.out.println("Sorry the path for the logger config is wrong");
            return ;
        }

        Logger log = Logger.initLoggerFromConfig("App", config);
        log.log(LogLevel.INFO, "Fahghhhh");

    }


    
}
