package example.Logger.Configurator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private final Map<String, String> values = new HashMap<>();

    public Config (String path) throws IOException{
        try (BufferedReader fileReader = new BufferedReader(new FileReader(path))){
            String line;
            while((line = fileReader.readLine()) != null){
                line = line.trim();
                if(line.isEmpty() || line.startsWith("#")) continue;

                int eqIndex = line.indexOf('=');
                if(eqIndex < 0 ) continue ;

                String key = line.substring(0,eqIndex).trim();
                String value = line.substring(eqIndex + 1).trim();

                values.put(key, value);
            }
        }
    }
    public String get(String key) {
        return values.get(key);
    }
    // used for my case for default values
    public String getOr(String key , String defaultValue){
        String value = values.get(key);
        return value != null ? value : defaultValue;
    }

}
