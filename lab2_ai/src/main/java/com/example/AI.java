package com.example;
import org.json.JSONArray;
import org.json.JSONObject;

import com.example.Logger.LogLevel;
import com.example.Logger.Logger;

public class AI {
    static  JSONArray getAvaliableModels(String body){
        JSONArray models = new JSONArray();
        JSONObject obj = new JSONObject(body);
        JSONArray arr = obj.getJSONArray("models");

        for(int i = 0 ; i < arr.length() ; i++){
            JSONObject m = arr.getJSONObject(i);

            if(!m.getString("type").equals("llm")) continue;
            if(m.getJSONArray("loaded_instances").length() == 0) continue;
            models.put(m);
        }

        return models;
    }
    static void printAvaliableModels(JSONArray avaliableModels){
        System.out.println("Avaliable Models: ");
        for(int i = 0 ; i < avaliableModels.length() ; i++){
            System.out.println((i+1) + ". " + avaliableModels.getJSONObject(i).getString("key"));
        }
    }
    // Maybe change this in the future to be better, maybe by parameters or something like that
    static String getModel(JSONArray avaliableModels , Logger log){
        if(avaliableModels == null || avaliableModels.length() == 0){
            log.log( LogLevel.ERROR, "No avaliable models");
            return null;
        };
        return avaliableModels.getJSONObject(0).getString("key");
    }

    static String getMessage(String body){
        return new JSONObject(body).getJSONArray("output").getJSONObject(0).getString("content");

    }
}
