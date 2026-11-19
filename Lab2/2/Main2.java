import java.io.File;                  
import java.io.FileNotFoundException; 
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {

        File myFile = new File("API_KEY.env");

        // read the API_KEY.env file if its not created create it
        getConstFromFile(myFile);

        HttpConector.getModels();
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
