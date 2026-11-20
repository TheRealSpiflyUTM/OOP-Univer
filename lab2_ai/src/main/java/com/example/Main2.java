package com.example;

import com.example.Logger.LogLevel;
import com.example.Logger.Logger;
import java.io.File;                  
import java.io.FileNotFoundException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Scanner;
import org.json.JSONObject;
import org.json.JSONArray;
public class Main2 {
    public static void main(String[] args) {

        File myFile = new File("API_KEY.env");

        // read the API_KEY.env file if its not created create it
        getConstFromFile(myFile);
        Logger log = new Logger("Cheker");

        log.log(LogLevel.INFO, "Trying to get a response");
        HttpResponse<String> response = HttpConector.getModels();
        
        if(response == null || response.statusCode() != 200){
            log.log( LogLevel.ERROR, "This connection failed");
            return;
        };

        // System.out.println(response.body());
        JSONArray avaliableModels = AI.getAvaliableModels(response.body());
        AI.printAvaliableModels(avaliableModels);

        String model = AI.getModel(avaliableModels , log);
        if(model == null) return ;
        
        Scanner in = new Scanner(System.in);
        String message = "s" ;
        System.out.println("God: Whats on your mind");
        
        while(true){
            System.out.print("Monkey: ");
            message = in.nextLine().trim();
            
            
            log.log(LogLevel.INFO, "Sending your prayer to the GOD");;
            HttpResponse response2 =  HttpConector.sendMessage(model, message);
            
            System.out.println("God: Whats on your mind");
            // Final Checker
            message.toLowerCase();
            if(message.equals("q") || message.equals("e") || message.equals("quit") || message.equals("exit")) break;
            if(message.isEmpty()) continue;
        }


       /*curl http://localhost:1234/api/v1/chat \
  -H "Authorization: Bearer $LM_API_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "ibm/granite-4-micro",
    "input": "Tell me the top trending model on hugging face and navigate to https://lmstudio.ai",
    "integrations": [
      {
        "type": "ephemeral_mcp",
        "server_label": "huggingface",
        "server_url": "https://huggingface.co/mcp",
        "allowed_tools": [
          "model_search"
        ]
      },
      {
        "type": "plugin",
        "id": "mcp/playwright",
        "allowed_tools": [
          "browser_navigate"
        ]
      }
    ],
    "context_length": 8000,
    "temperature": 0
  }'
 */
    }    
    
    // file reader
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
