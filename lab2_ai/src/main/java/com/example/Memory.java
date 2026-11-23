package com.example;

import java.net.http.HttpResponse;
import java.util.ArrayDeque;
import java.util.Deque;

import com.example.Logger.LogLevel;
import com.example.Logger.Logger;

public class Memory {
    private final Deque<String> messages = new ArrayDeque<>();
    private int capacity;
    private StringBuilder resume = new StringBuilder("");
    private static Logger log = new Logger("Memory");
    private static String model;  
    private static String reasoning;  

    public static final int TIER1_SIZE = 2;
    public static final int TIER2_SIZE = 4;
    public static final int TIER3_SIZE = 6;

    public static Memory tier1() {return new Memory(TIER1_SIZE);}
    public static Memory tier2() {return new Memory(TIER2_SIZE);}
    public static Memory tier3() {return new Memory(TIER3_SIZE);}

    private static String resumePrompt = 
    """
    - You must resume all of the conents in this prompt for it yourself so in the future you know what is speaking in the conversation.
    - The resume limit is 200 - 250 words never go above this limit, bellow is accetable

    """;

    public Memory(int capacity){
        this.capacity = capacity;
    }
    // Adders
    public void add(String newMemory){   
        if(messages.size() > capacity - 1){
            log.log(LogLevel.WARN, "Removing from memory");            
            messages.removeFirst();
        }
        
        while(messages.size() > capacity - 1){
            messages.removeFirst();
        }

        log.log(LogLevel.TRACE, "Adding new memory");
        messages.addLast(newMemory);
    }
    public void addWithResume(String newMemory){
        if(messages.size() > capacity - 1 ){
            log.log(LogLevel.WARN , "Creating a resume on the {0} capacity, deleting all other messages" , new Object[]{capacity});
            createResume();
            messages.clear();
        }
        log.log(LogLevel.TRACE, "Adding new memory");
        messages.addLast(newMemory);
    }

    public String getMemoryString(){
        StringBuilder sb = new StringBuilder("Memory: \"");
        for (String line : messages) {
            sb.append(line).append("\n");
        }
        sb.append("\"");
        return sb.toString();
    }

    public void createResume(){
        HttpResponse<String> response = HttpConector.sendMessage(
            model ,
            "Resume: " + resume.toString() + "\n" 
            + "New Memory: " + getMemoryString()  
            , resumePrompt,
            reasoning 
        );
        resume = new StringBuilder(AI.getMessage(response.body()));
    }
    
    public String getResumeString(){
        return resume.toString();
    }


    public static void setModel(String tempModel){
        model = tempModel;
    }
    public static void setReasoning(String tempReasoning){
        reasoning = tempReasoning;
    }
}
