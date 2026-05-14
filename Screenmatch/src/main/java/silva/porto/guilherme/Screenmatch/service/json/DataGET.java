package silva.porto.guilherme.Screenmatch.service.json;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class DataGET {

    private static final Scanner scanf = new Scanner(System.in);

    public static String getData (String uniformResourceId) throws IOException, InterruptedException {

        HttpClient meRequesting = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uniformResourceId)).build();

        HttpResponse<String> json = meRequesting.send(request, HttpResponse.BodyHandlers.ofString());

        return json.body();// the given response
    }
}