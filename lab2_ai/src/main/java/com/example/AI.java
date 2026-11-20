package com.example;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class AI {
    static List<String> getAvaliableModels(String body){
        List<String> models = new ArrayList<>();
        JSONObject obj = new JSONObject(body);
        JSONArray arr = obj.getJSONArray("models");

        for(int i = 0 ; i < arr.length() ; i++){
            JSONObject m = arr.getJSONObject(i);

            if(!m.getString("type").equals("llm")) continue;
            if(m.getJSONArray("loaded_instances").length() == 0) continue;
            models.add(m.getString("key"));
        }

        return models;
    }
    static void printAvaliableModels(List<String> avaliableModels){
        System.out.println("Avaliable Models: ");
        for(int i = 0 ; i < avaliableModels.size() ; i++){
            System.out.println((i+1) + ". " + avaliableModels.get(i));
        }
    }

}
