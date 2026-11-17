import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Plain API calls using only the Java standard library (Java 11+ HttpClient).
 * Test API: https://jsonplaceholder.typicode.com (fake REST API, free to use).
 */
public class ApiCalls {

    static final String BASE = "https://jsonplaceholder.typicode.com";
    static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) throws Exception {
        get();
        post();
        put();
        delete();
    }

    // 1. GET - read data
    static void get() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/posts/1"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("=== GET /posts/1 ===");
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());
        System.out.println();
    }

    // 2. POST - create data
    static void post() throws Exception {
        String json = """
                {
                  "title": "hello",
                  "body": "this is a test post",
                  "userId": 1
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/posts"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("=== POST /posts ===");
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());
        System.out.println();
    }

    // 3. PUT - update data
    static void put() throws Exception {
        String json = """
                {
                  "id": 1,
                  "title": "updated title",
                  "body": "updated body",
                  "userId": 1
                }
                """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/posts/1"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("=== PUT /posts/1 ===");
        System.out.println("Status: " + response.statusCode());
        System.out.println("Body: " + response.body());
        System.out.println();
    }

    // 4. DELETE - remove data
    static void delete() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/posts/1"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("=== DELETE /posts/1 ===");
        System.out.println("Status: " + response.statusCode());
        System.out.println();
    }
}
