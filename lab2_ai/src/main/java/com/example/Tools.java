package com.example;

import java.lang.Thread.State;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.function.Function;

import com.example.Logger.LogLevel;
import com.example.Logger.Logger;

import org.json.JSONObject;

public class Tools {
    private static Logger log = new Logger("Tools");
    public static HashMap<String , Function<JSONObject , String>> toolMap = new HashMap<>();
    
    // Adding tools here
    static {
        toolMap.put("get_current_time" , args ->  {
            log.log(LogLevel.TRACE, "Performing getCurrentTime()");
            return getCurrentTime()
        ;});
        toolMap.put("pharanthesis_checker" , args -> {
            log.log(LogLevel.TRACE, "Performing Paranthesis.getAnswere ");
            return  Paranthesis.getAnswere(args.getString("pharamthesis")) == true
            ? "True"
            : "False";

        });
        toolMap.put("add", args -> {
            log.log(LogLevel.TRACE, "Performing Adder ");
            return (String.valueOf(args.getInt("a") + args.getInt("b")));
        });
    }
    
    public static String run(String name , JSONObject args){
        Function<JSONObject , String> tool = toolMap.get(name);
        
        log.log(LogLevel.TRACE, "Checking for tools ");
        if(tool == null ) return  "[unknown tool: " + name + "]";
        try{
            return tool.apply(args);
        }catch(Exception e){
            return "[tool error: " + e.getMessage() + "]";
        }
    }

    public static String getCurrentTime(){
        return LocalTime.now().toString();
    }
}
