package example.Runner;

import java.io.IOException;

import example.Logger.Logger;
import example.Logger.LogLevel;

import example.Logger.Configurator.Config;
import example.Logger.Configurator.ConfigException;

import example.Logger.Formatter.JsonFormatter;

public class App 
{
    public static void main( String[] args )
    {
        Logger log;
        Config config = null;
        try{
            config = new Config("LoggerConfig.conf");
        }catch(IOException e){
            System.err.println("Error: Sorry the path 'LoggerConfig.conf' for the logger config is wrong and wasnt found");
        }catch(ConfigException e){
            System.err.println("Config error: " + e.getMessage() + " Taking the default log configuration");
        }
        if(config != null){
            log = Logger.initLoggerFromConfig("App", config);
        }else{
            log = new Logger("App-Class");
        }   
        log.log(LogLevel.INFO, "Fahghhhh");
    }

    


    
}
