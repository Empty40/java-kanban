package server;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final HttpClient kvServerClient = HttpClient.newHttpClient();

    private final String url;

    private final String API_TOKEN;

    public KVTaskClient(String url) throws IOException, InterruptedException {
        this.url = url;

        HttpClient kvServerClient = HttpClient.newHttpClient();

        URI uri = URI.create("http://localhost:8078/register");

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("Content-type", "application/json")
                .uri(uri)
                .build();

            HttpResponse<String> apiToken = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
            API_TOKEN = apiToken.body();
    }

    public void put(String key, String json) {
        URI uri = URI.create(url + "/save/" + key + "?API_TOKEN=" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .uri(uri)
                .build();

        try {
            kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String load(String key) {
        URI uri = URI.create(url + "/load/" + key + "?API_TOKEN=" + API_TOKEN);

        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(uri)
                .build();

        try {
            HttpResponse<String> apiToken = kvServerClient.send(request, HttpResponse.BodyHandlers.ofString());
            return apiToken.body();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}
