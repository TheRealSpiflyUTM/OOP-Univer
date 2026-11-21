package com.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.http.HttpResponse;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.Logger.LogLevel;
import com.example.Logger.Logger;

public class Agent {
    private Logger log;
    private HttpResponse<String> modelsResponse;
    private String model;
    private final Memory memory;
    private final String masterPrompt;

    public Agent(File myFile , Memory memory, String masterPrompt){

        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        this.memory = memory;
        this.masterPrompt = masterPrompt;
        // read the API_KEY.env file if its not created create it
        getConstFromFile(myFile);
        log = new Logger("Cheker");

        log.log(LogLevel.TRACE, "Trying to get a response");
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
        Memory.setModel(model);
        System.out.println("--------------------------------------------------------------------------------------");
    }

    public void chat(){
        Scanner in = new Scanner(System.in);
        String message = "s" ;
        System.out.println("God: Whats on your mind");
        HttpResponse<String> respnoseFromAI = null;
        while(true){

            System.out.print("Monkey: ");
            message = in.nextLine().trim();

            //#region message Cheker
            String lowered = message.toLowerCase();
            if(lowered.equals("q") || lowered.equals("e") || lowered.equals("quit") || lowered.equals("exit")) break;
            if(lowered.isEmpty()) continue;
            //#endregion

            respnoseFromAI = getResponseFromAI(message);

            //#region respnoseFromAI Checker
            if(respnoseFromAI == null || respnoseFromAI.statusCode() != 200){
                log.log(LogLevel.ERROR, "No ai response womp womp");
                System.out.println("God: Some idiot doesn't know how to code, try again");
                continue;
            }
            //#endregion
            
            printResponseStats(respnoseFromAI);
            String AIMessage = getAiMessage(respnoseFromAI);
            System.out.println("God: " + AIMessage);
            
            addToMemory(message, AIMessage);
        }
    }
    
    private String getAiMessage(HttpResponse<String> respnoseFromAI){
        JSONObject reply = new JSONObject(AI.getMessage(respnoseFromAI.body()));
        String message = reply.getString("message");
        JSONArray toolCalls = reply.getJSONArray("tools");

        for(int i = 0 ; i < toolCalls.length() ; i++){
            JSONObject toolCall = toolCalls.getJSONObject(i);
            
            String name = toolCall.getString("name");
            String id = toolCall.getString("id");
            JSONObject args = toolCall.optJSONObject("args");
            if(args == null) args = new JSONObject();
            
            String result = Tools.run(name, args);
            message = message.replace("{" + id + "}" , result);
        }

        return message;
    }

    private void addToMemory(String humanMessage , String AIMessage){
        memory.addWithResume(
            "Human Question: " + humanMessage + "\n" + 
            "Ai Response: " + AIMessage
        );
    }
    private HttpResponse<String> getResponseFromAI(String message){
        log.log(LogLevel.TRACE, "Sending your prayer to the GOD");;
        return HttpConector.sendMessage(
            model, 
            "Resume: " + memory.getResumeString() + "\n" + 
            "Memory: " + memory.getMemoryString() + "\n" +
            "Curent message: " + message, 
            masterPrompt
        );
    }


    private void printResponseStats(HttpResponse<String> response){
        JSONObject body = new JSONObject(response.body()).getJSONObject("stats");
        // System.out.println(body);
        System.out.println("--------------------------------------------------------------------------------------");

        log.log(
            LogLevel.INFO, 
            """
            \nInput Tokens: {0}
            Total Output Tokens {1}
            Reasoning Output Tokens {2}
            Tokens Per Second {3}
            Time For The First Token Reads {4}
            """, 
            new Object[]{
                body.getInt("input_tokens"),
                body.getInt("total_output_tokens"),
                body.getDouble("reasoning_output_tokens"),
                body.getDouble("tokens_per_second"),
                body.getDouble("time_to_first_token_seconds"),
            }
        );

        System.out.println("--------------------------------------------------------------------------------------");

    }
    
    
    public String getModel(){
        return model;
    }


    // Helper Functions

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
