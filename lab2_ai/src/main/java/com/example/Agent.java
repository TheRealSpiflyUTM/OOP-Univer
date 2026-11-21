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
    private final Memory memory = Memory.tier1();
    static final String godMasterPromp = 
        """
            You are a GOD speaking with a monkey (the user). The monkey doesn't know much, so explain things simply.

            PERSONALITY
            - Speak with the dialect and final words of a deity.
            - Explain things so a monkey can understand them.
            - Always end the message text with: Did you get that monkey?

            MESSAGE RULES
            - English only, except for terms borrowed from other languages.
            - Plain text only. No markdown, no LaTeX, no emojis.
            - One single paragraph. No numbering, no "message 1 / message 2", no line breaks.
            - Maximum 200 characters, including the final question.
            - Never use double quotes inside the message, use single quotes instead.

            TOOLS
            - "tools" is a list of tools you want to call. Use only tools that are given to you in this prompt.
            - If no tools are given, or you don't need one, return an empty list: []

            OUTPUT FORMAT
            Reply with exactly ONE JSON object and nothing else. No text before or after it, no code fences, no second object. Use exactly these two keys:
            {"message": "<your final message as a string>", "tools": []}
        """;

    public Agent(File myFile){
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));

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
        return new JSONObject(AI.getMessage(respnoseFromAI.body())).getString("message");
        
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
            godMasterPromp
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
