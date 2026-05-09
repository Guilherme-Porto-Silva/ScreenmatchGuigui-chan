package silva.porto.guilherme.Screenmatch.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  public class ScreenmatchApplication {

    public static void main(String[] args) {

        try{ SpringApplication.run(ScreenmatchApplication.class, args); }

        catch (Exception e) {

            System.out.println("\nThe problem " + e.getMessage() + " happened,");

            System.out.println("at " + e.getCause());
        }
    }
}