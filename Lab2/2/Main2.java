import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.File;                  
import java.io.FileNotFoundException; 
import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {

        String APIURL = "";
        HttpClient client = HttpClient.newHttpClient();
        File myFile = new File("API_KEY.env");

        // read the API_KEY.env file if its not created create it
        try(Scanner myScanner = new Scanner(myFile)){
            while(myScanner.hasNextLine()){
                String data = myScanner.nextLine().trim();
                String[] parts = data.split("=" , 2);

                if(parts[0].equals("LLM_KEY")){
                    APIURL = parts[1];
                }
            }
        }catch(FileNotFoundException e){
            System.out.println("No env file probably");
        }

        String modelsAIURL = APIURL + "/api/v1/models" ;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(modelsAIURL))
                .GET()
                .build();

        HttpResponse<String> response = null;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch(IOException | InterruptedException e){
            e.printStackTrace();
        }

        
        response.headers().map()
                    .forEach((name , value) -> 
                        System.out.println(name + ": " + value));
    }

}
