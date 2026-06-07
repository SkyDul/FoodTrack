import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestGemini {
    public static void main(String[] args) throws Exception {
        String key = "AIzaSyA73rKLWhVqKK3BhaxMuYZB_av8Mek-h0s";
        String[] models = {"gemini-2.0-flash", "gemini-2.5-flash"};
        
        String json = "{\"contents\": [{\"parts\": [{\"text\": \"Halo!\"}]}]}";
        
        for (String m : models) {
            String url = "https://generativelanguage.googleapis.com/v1/models/" + m + ":generateContent?key=" + key;
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
                
            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            System.out.println("Model: " + m + " -> Status: " + response.statusCode());
            if (response.statusCode() != 200) {
                System.out.println(response.body());
            } else {
                System.out.println("Success!");
                break;
            }
        }
    }
}
