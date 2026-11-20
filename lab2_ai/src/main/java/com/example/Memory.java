package com.example;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import com.example.Logger.LogLevel;
import com.example.Logger.Logger;

public class Memory {
    private final Deque<String> messages = new ArrayDeque<>();
    private int capacity;
    private StringBuilder resume = new StringBuilder("");
    private static Logger log = new Logger("Memory");

    public static Memory tier1() {return new Memory(5);}
    public static Memory tier2() {return new Memory(10);}
    public static Memory tier3() {return new Memory(15);}

    private static String resumePrompt = 
    """
    - You must resume all of the conents in this prompt for it yourself so in the future you know what is speaking in the conversation.
    - The resume limit is 200 - 250 words never go above this limit, bellow is accetable

    """;
    public Memory(int capacity){
        this.capacity = capacity;
    }

    public void add(String newMemory){
        
        if(messages.size() > capacity){
            log.log(LogLevel.INFO, "Removing from memory");            
            messages.removeFirst();
            messages.removeFirst();
        }
        
        while(messages.size() > capacity){
            messages.removeFirst();
            messages.removeFirst();
        }
        
        log.log(LogLevel.INFO, "Adding new memory");
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
        HttpResponse<String> response = HttpConector.sendMessage(getMemoryString() , resumePrompt );
        messages.clear();
        resume = new StringBuilder(response.body());
    }
    public String getResume(){
        return resume.toString();
    }
}
