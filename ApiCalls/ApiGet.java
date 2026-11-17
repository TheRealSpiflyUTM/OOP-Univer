import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Fetches JSON from an API link and prints it.
 * Plain Java only - no external libraries.
 *
 * Usage:
 *   java ApiGet                     -> uses the default URL below
 *   java ApiGet <url>               -> uses your own URL
 */
public class ApiGet {

    public static void main(String[] args) throws Exception {
        String url = (args.length > 0)
                ? args[0]
                : "https://jsonplaceholder.typicode.com/posts/1";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("URL: " + url);
        System.out.println("Status: " + response.statusCode());
        System.out.println("JSON:");
        System.out.println(response.body());
    }
}
