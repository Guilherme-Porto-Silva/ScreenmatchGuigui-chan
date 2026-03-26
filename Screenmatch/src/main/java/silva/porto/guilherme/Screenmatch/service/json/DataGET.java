package silva.porto.guilherme.Screenmatch.service.json;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class DataGET {

    private static final Scanner scanf = new Scanner(System.in);

    public static String getData (String uniformResourceId) {

        HttpClient meRequesting = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(uniformResourceId)).build();

        try {
            HttpResponse<String> json = meRequesting.send(request, HttpResponse.BodyHandlers.ofString());

            return json.body();// the given response
        }

        catch (IOException e) {

            System.out.println("\nYou might have typed something wrong, for I detected a " + e);

            return (e.getMessage() + " - " + e.getCause());
        }

        catch (InterruptedException e) {

            System.out.println("\nThe system's work was interrupted: " + e.getCause());

            return (e.getMessage() + " - " + e.getCause());
        }

        catch (Exception e) {

            System.out.printf("""
                         The following problem happened:
                        
                         %s - %s""", e.getMessage(), e.getCause());

            return (e.getMessage() + " - " + e.getCause());
        }
    }
}