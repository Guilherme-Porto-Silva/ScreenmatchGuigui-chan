package silva.porto.guilherme.Screenmatch.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import silva.porto.guilherme.Screenmatch.repository.SeriesRepository;

@SpringBootApplication  public class ScreenmatchApplication implements CommandLineRunner {

    @Autowired private static final SeriesRepository REPOSITORY = null;

    private static final Core CORE = new Core(REPOSITORY);



    @Override public void run(String... args) throws Exception {

        do {

            CORE.useSeriesData();

            System.out.print("\nIf you want to do another search, type \"more\":");

        } while (CORE.scanf.nextLine().toLowerCase().contains("more"));
    }

    public static void main(String[] args) {

        while (true) {

            try{ SpringApplication.run(ScreenmatchApplication.class, args); }

            catch (Exception e) {

                System.out.println("\nThe problem " + e.getMessage() + " happened,");

                System.out.println("at " + e.getCause());

                System.out.println("\nHave you titled the series name right? You might want to type it again.");

                System.out.print("\nIf you don't, type 'quit': ");

                if (CORE.scanf.nextLine().strip().equalsIgnoreCase("quit")) break;
            }
        }
    }
}