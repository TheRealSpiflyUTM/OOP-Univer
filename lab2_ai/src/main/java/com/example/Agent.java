package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpResponse;
import java.util.Scanner;

import org.json.JSONArray;

import com.example.Logger.LogLevel;
import com.example.Logger.Logger;

public class Agent {
    private Logger log;
    private HttpResponse<String> modelsResponse;
    private String model;

    static final String godMasterPromp = 
        """
        - You have the personality of a GOD
        - You are speaking with a monkey he doesn't know a lot of stuff so you need to explain him to undestand
        - You must represent a diety having the dielect and final words of one
        - Every message you explain must be met at the end with "Did you get that monkey?"
        - Dont use the md or lateh format just plain text.
        - Use at least 2 in the whole response not one afther anothe
        - You respond just in english no other language is allowed with the exception of terms from other languages
        - The maximum ammount of characters for your response shouldn't be bigger then 200
        """;

    public Agent(File myFile){
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

        // read the API_KEY.env file if its not created create it
        getConstFromFile(myFile);
        log = new Logger("Cheker");

        log.log(LogLevel.INFO, "Trying to get a response");
        modelsResponse = HttpConector.getModels();
        
        if(modelsResponse == null || modelsResponse.statusCode() != 200){
            log.log( LogLevel.ERROR, "This connection failed");
            return;
        };

        // System.out.println(modelsResponse.body());
        JSONArray avaliableModels = AI.getAvaliableModels(modelsResponse.body());
        AI.printAvaliableModels(avaliableModels);

        model = AI.getModel(avaliableModels , log);
        if(model == null) return ;
        
    }

    public void chat(){
        Scanner in = new Scanner(System.in);
        String message = "s" ;
        System.out.println("God: Whats on your mind");
        HttpResponse<String> respnoseFromAI = null;
        Memory memory = Memory.tier1();
        while(true){

            System.out.print("Monkey: ");
            message = in.nextLine().trim();

            //#region Exit Cheker
            String lowered = message.toLowerCase();
            if(lowered.equals("q") || lowered.equals("e") || lowered.equals("quit") || lowered.equals("exit")) break;
            if(lowered.isEmpty()) continue;
            //#endregion

            log.log(LogLevel.INFO, "Sending your prayer to the GOD");;
            respnoseFromAI =  HttpConector.sendMessage(
                model, 
                "Resume: " + memory.getResume() + "\n" + "Memory: " + memory.getMemoryString() + "Curent message: " + message, 
                godMasterPromp
            );

            if(respnoseFromAI == null || respnoseFromAI.statusCode() != 200){
                log.log(LogLevel.ERROR, "No ai response womp womp");
                System.out.println("God: Some idiot doesn't know how to code, try again");
                continue;
            }
            
            // System.out.println(respnoseFromAI.body().getClass());
            String AIMessage = AI.getMessage(respnoseFromAI.body());
            System.out.println("God: " + AIMessage);
            
            memory.add("Human Question: " + message);
            memory.add("Ai Response: " + AIMessage);
        }
    }





    static void getConstFromFile(File file){
        try(Scanner myScanner = new Scanner(file)){
            while(myScanner.hasNextLine()){
                String data = myScanner.nextLine().trim();

                if(data.isEmpty() || data.startsWith("#")) continue;
                String[] parts = data.split("=" , 2);

                if(parts.length < 2) continue;
                String name = parts[0].trim();
                String value = parts[1].trim();

                if(name.equals("LLM_URL")){
                    Values.APIURL = value;
                }
                else if(name.equals("API_KEY")){
                    Values.APIKEY = value;
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("No env file found at: " + file.getAbsolutePath());
            return;
        }
        
        if(Values.APIURL.isEmpty()){
            System.out.println("LLM_KEY (the cloudflare link) is empty in API_KEY.env");
            return;
        }
        while(Values.APIURL.endsWith("/")) {
            Values.APIURL = Values.APIURL.substring(0, Values.APIURL.length() - 1);
        }
    }
}
