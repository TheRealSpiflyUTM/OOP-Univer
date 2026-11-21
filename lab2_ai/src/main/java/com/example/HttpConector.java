package com.example;

import java.io.IOException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import  com.example.Logger.LogLevel;
import  com.example.Logger.Logger;

import org.json.JSONObject;

public class HttpConector{
    static private Logger log = new Logger("HttpConnector");
    static String modelsAIURL = "/api/v1/models";  
    static String chatAIURL = "/api/v1/chat";  
    static HttpClient client = HttpClient.newHttpClient();


    public static HttpResponse<String>  getModels(){
        HttpRequest.Builder builder = HttpRequest.newBuilder()
        .uri(URI.create(Values.APIURL + modelsAIURL))
        .GET();

        return proceedFromBuilder(builder);
    }
    
    
    public static HttpResponse<String> sendMessage(String model , String message){
        JSONObject body = new JSONObject();
        body.put("model", model);
        // body.put("type", "text");
        body.put("input", message);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
        .uri(URI.create(Values.APIURL + chatAIURL))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body.toString()));
        return proceedFromBuilder(builder);
    }
    public static HttpResponse<String> sendMessage(String model , String message , String masterPropmt){
        JSONObject body = new JSONObject();
        body.put("model", model);
        body.put("system_prompt", masterPropmt);
        // body.put("type", "text");
        body.put("input", message);

        HttpRequest.Builder builder = HttpRequest.newBuilder()
        .uri(URI.create(Values.APIURL + chatAIURL))
        .header("Content-Type", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body.toString()));
        return proceedFromBuilder(builder);
    }


    private static HttpResponse<String> proceedFromBuilder(HttpRequest.Builder builder) {
        if(!Values.APIKEY.isEmpty()){
            builder.header("Authorization", "Bearer " + Values.APIKEY);
        }
        HttpRequest request = builder.build();
        try{
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            log.log(LogLevel.INFO, "Status: " + response.statusCode());
            // System.out.println(response.body());
            return response;
        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }
        return null;
    }

}