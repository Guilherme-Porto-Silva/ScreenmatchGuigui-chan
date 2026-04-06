package silva.porto.guilherme.Screenmatch.main;

import silva.porto.guilherme.Screenmatch.models.episode.Episode;
import silva.porto.guilherme.Screenmatch.models.SeasonData;
import silva.porto.guilherme.Screenmatch.models.series.Series;
import silva.porto.guilherme.Screenmatch.models.series.SeriesCategory;
import silva.porto.guilherme.Screenmatch.models.series.SeriesData;
import silva.porto.guilherme.Screenmatch.repository.SeriesRepository;
import silva.porto.guilherme.Screenmatch.service.json.ConvertData;
import silva.porto.guilherme.Screenmatch.service.json.DataGET;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Core {

    private final ConvertData CONVERTER = new ConvertData();

    public final Scanner scanf = new Scanner(System.in);

    public final Pattern DOUBLE = Pattern.compile("\\d+([.,]\\d+)?");

    public final Pattern INTEGER = Pattern.compile("\\d+");

    private final String ADDRESS = "https://www.omdbapi.com/t=";

    private final String API_KEY = System.getenv("API_KEY_PATH");

    private final SeriesRepository REPOSITORY;

    public Core (SeriesRepository repository) { REPOSITORY = repository; }

// On Windows, environment variables are generally case-insensitive, but on Linux/Unix, they are case-sensitive.

// System.getenv("path") might return null on Linux, while System.getenv("PATH") works.



    private double extractDouble () {

        while (true) {

            Matcher doubleMatcher = DOUBLE.matcher(scanf.nextLine());

            if (doubleMatcher.find()) return Double.parseDouble(doubleMatcher.group());

            else System.out.print("\nType a decimal number, with ',' or '.': ");
        }
    }



    private int extractInteger () {

        while (true) {

            Matcher intMatcher = INTEGER.matcher(scanf.nextLine());

            if (intMatcher.find()) return Integer.parseInt(intMatcher.group());

            else System.out.print("\nType an integer number: ");
        }
    }



    private String askForSeries(){

        System.out.println("\nDo you remember how the series you are looking for is titled?");

        System.out.print("Type \"show\" if you want me to list all series in the data bank: ");

        if (scanf.nextLine().toLowerCase().contains("w"))

        REPOSITORY.findAll().stream().sorted(Comparator.comparing(Series::getThemes)).forEach(System.out::println);

        System.out.println("\nType the name of the series you want to search for:");

        return scanf.nextLine().strip();
    }



    private List<Episode> searchForSeries() {

        var seriesName = askForSeries();

        String json = DataGET.getData(ADDRESS + seriesName.toLowerCase().replaceAll(" ", "+") + "&apikey=" + API_KEY);

        System.out.println("\nSearching for " + seriesName + '.');

        Series dataSeries = new Series(CONVERTER.convertData(json, SeriesData.class));

        byte howManySeasons = dataSeries.getHowManySeasons();

        List<SeasonData> seasons = new ArrayList<>();

        for (int i = 1; i <= howManySeasons; i++) {

            json = DataGET.getData(ADDRESS + seriesName + "&season=" + i + "&apikey=" + API_KEY);

            SeasonData dataSeason = CONVERTER.convertData(json, SeasonData.class);

            seasons.add(dataSeason);
        }

        REPOSITORY.save(dataSeries);

        return seasons.stream().flatMap(season -> season.episodes().stream()

        .map(data -> new Episode(data, season.orderPosition()))).toList();
    }



    private void showAll(List<Episode> allEpisodes) {

        allEpisodes.forEach(Episode::println);
    }



    private LocalDate askForDate () {

        System.out.println("\nThen, type the date you want the search to start.");

        System.out.print("Type the day, a '/', the month, a '/' and the year:");

        while (true) {

            try { return CONVERTER.brazilianDate(scanf.nextLine().strip()); }

            catch (ClassCastException e) { System.out.print("\nPlease, type it like \"dd/MM/yyyy\": "); }
        }
    }



    private void showBasedOnDate(List<Episode> allEpisodes){

        LocalDate askedDate = askForDate();

        List<Episode> usefullEpisodes = allEpisodes.stream().filter(e -> e.getReleaseDate() != null).toList();

        System.out.println("\nWant to see the ones after or the ones before this date?");

        while (true) {

            if (scanf.nextLine().strip().equalsIgnoreCase("after")) {

                usefullEpisodes.stream().filter(e -> e.getReleaseDate()

                .isAfter(askedDate)).toList().forEach(Episode::println);

                break;
            }

            else if (scanf.nextLine().strip().equalsIgnoreCase("before")) {

                usefullEpisodes.stream().filter(e -> e.getReleaseDate()

                .isBefore(askedDate)).toList().forEach(Episode::println);

                break;
            }

            else System.out.print("\nType ONLY \"before\" or ONLY \"after\":");
        }
    }



    private void showBasedOnSeason(List<Episode> allEpisodes){

        System.out.print("\nType the season's number: ");

        while (true) {

            try{

                Integer season = Integer.parseInt(scanf.nextLine());

                allEpisodes.stream().filter(e -> Objects.equals(e.getSeason(), season))

                        .toList().forEach(Episode::println);

                break;
            }

            catch (NumberFormatException e) {

                System.out.print("\nType only the number: ");
            }
        }
    }



    private void showSeasonOfEpisode(List<Episode> allEpisodes){

        System.out.println("You are needing to type the episode's title. If you want me to show all titles, so you can see how the one you are looking for is called,");

        System.out.print("type \"show\": ");

        if (scanf.nextLine().toLowerCase().contains("w")) showAll(allEpisodes);

        System.out.print("\nType the episode's title: ");

        String title = scanf.nextLine().strip();

        var episode = allEpisodes.stream().filter(e -> e.getTitle().toLowerCase().contains(title.toLowerCase())).findFirst();

//  findFirst() sempre segue a mesma ordem, sempre retorna o mesmo resultado

//  findAny() funciona diferentemente quando múltiplas streams são rodadas em paralelo, mas ele mais rápido

        if(episode.isPresent()) System.out.println('\n' + '"' + title + "\" is in season " + episode.get().getSeason());

        else System.out.println("\nFailed to find \"" + title + '"' + '.');
    }



    private void bestFiveEpisodes(List<Episode> allEpisodes){

        allEpisodes.stream().sorted(Comparator.comparingDouble(Episode::getRate)

                        .reversed())//This process would not generate a list of the best films, as the ordering was in ascending order instead of descending.

                .limit(5).forEach(Episode::println);
    }



    private DoubleSummaryStatistics getSummaryStatistics (List<Episode> allEpisodes) {

        return allEpisodes.stream()

                .filter(episode -> episode.getRate() > 0.0)// not counting not rated episodes

                .collect(Collectors.summarizingDouble(Episode::getRate));
    }



    private void showSummaryStatistics (List<Episode> allEpisodes) {

        var statistics = getSummaryStatistics(allEpisodes);

        System.out.println('\n' + "=".repeat(20));

        System.out.println("\nAverage rate: " + statistics.getAverage());

        System.out.println("\nHighest rate: " + statistics.getMax());

        System.out.println("\nLowest rate: " + statistics.getMin());

        System.out.println("\nCount of rates: " + statistics.getCount());

        System.out.println("\nSum of all rates: " + statistics.getSum());

        System.out.println('\n' + "=".repeat(20));
    }



    private void showSeasonRates(List<Episode> allEpisodes){

        Map<Integer, Double> seasonRates = allEpisodes.stream()

                .filter(episode -> episode.getRate() > 0.0)// not counting not rated episodes

                .collect(Collectors.groupingBy(Episode::getSeason,// key, the Integer

                        Collectors.averagingDouble(Episode::getRate)));// value, the Double

        int size = seasonRates.size();

        for (int i = 0; i < size; i++) System.out.printf(

                "\nSeason %d: %.2f\n", i, seasonRates.get(i));

        int notRated = 0;

        size = allEpisodes.size();

        for (int i = 0; i < size; i++) if (allEpisodes.get(i).getRate() == 0.0) notRated++;

        System.out.println('\n' + notRated + " episodes had not been rated.");

        // Didn't use DoubleSummaryStatistics for this one because I wanted
        // just the amount of non-rated episodes.
    }



    public void printSeries (List<Series> printedSeries) {

        int howManySeries = printedSeries.size();

        for (int i = 0; i < howManySeries; i++) {

            System.out.println("\nDATA FOR " + (i + 1) + "TH SERIES");

            System.out.println(printedSeries.get(i));
        }
    }



    public void groupByGenre(List<Series> ungroupedSeries) {

        List<String> wishedGenres = new ArrayList<>();

        System.out.println("\nType the genres you want to see. Add them one by one.");

        do {
            System.out.print("\nType a genre: ");

            wishedGenres.add(scanf.nextLine().strip().toLowerCase());

            System.out.print("If you want to add another genre, type \"more\": ");

        } while (scanf.nextLine().toLowerCase().contains("more"));

        var grouped = ungroupedSeries.stream()

                .filter(series -> wishedGenres.stream()

                .anyMatch(genre -> series.getThemes().name().toLowerCase().contains(genre)))

                .toList();

        printSeries(grouped);
    }



    public void groupByActor(List<Series> ungroupedSeries) {

        List<String> wishedActors = new ArrayList<>();

        System.out.println("\nType the actors you want to see. Add them one by one.");

        do {
            System.out.print("\nType an actor: ");

            wishedActors.add(scanf.nextLine().strip().toLowerCase());

            System.out.print("If you want to add another actor, type \"more\": ");

        } while (scanf.nextLine().toLowerCase().contains("more"));

        var grouped = ungroupedSeries.stream()

        .filter(series -> wishedActors.stream().anyMatch(actor ->

        series.getActors().name().toLowerCase().contains(actor)))

        .toList();

        printSeries(grouped);
    }



    public void groupByRating(List<Series> ungroupedSeries) {

        System.out.print("\nType the minimum rating you want: ");

        try {
            double minRate = Double.parseDouble(scanf.nextLine());

            var grouped = ungroupedSeries.stream()
                    .filter(series -> series.getRate() >= minRate)
                    .toList();

            printSeries(grouped);

        } catch (NumberFormatException e) {
            System.out.println("\nPlease type a valid number.");
        }
    }



    public void groupBySeasonAmount(List<Series> ungroupedSeries) {

        System.out.println("\nWant to define a minimum number of seasons or a maximum number of seasons? ");

        String minmax = scanf.nextLine().strip().toLowerCase();



        if (minmax.contains("x")) {

            System.out.print("\nType the maximum number of seasons: ");

            while (true) {

                try {
                    int maxSeasons = Integer.parseInt(scanf.nextLine());

                    var grouped = ungroupedSeries.stream()

                            .filter(series -> series.getHowManySeasons() <= maxSeasons)

                            .toList();

                    printSeries(grouped);

                    return;

                }

                catch (NumberFormatException e) { System.out.println("\nPlease type a valid integer."); }
            }
        }



        System.out.print("\nType the minimum number of seasons: ");

        while (true) {

            try {
                int minSeasons = Integer.parseInt(scanf.nextLine());

                var grouped = ungroupedSeries.stream()

                        .filter(series -> series.getHowManySeasons() >= minSeasons)

                        .toList();

                printSeries(grouped);

                break;

            }

            catch (NumberFormatException e) { System.out.println("\nPlease type a valid integer: "); }
        }
    }



    public void alreadySearchedSeries () {

        int will;

        List<Series> series = REPOSITORY.findAll();

        printSeries(series);

        System.out.print("""
                You can group those series by:
                
                1 - genre
                
                2 - actors
                
                3 - rating
                
                4 - season amount
                
                Just type one of those numbers (or any other number, if you don't want to group them): """);

        try { will = Integer.parseInt(scanf.nextLine()); }

        catch (NumberFormatException e) { will = 0; }

        switch (will) {

            case 0 -> System.out.println("\nPlease, type ONLY the number on the option's left.");

            case 1 -> groupByGenre(series);

            case 2 -> groupByActor(series);

            case 3 -> groupByRating(series);

            case 4 -> groupBySeasonAmount(series);

            default -> System.out.println("\nThat option was not in the list...");
        }
    }



    private void showEpisodesBasedOnSeason () {

        String seriesName = askForSeries();

        Optional<Series> seriesOptional = REPOSITORY.findAll().stream().filter(s -> s.getTitle().toLowerCase().contains(seriesName.toLowerCase())).findFirst();

        if (seriesOptional.isPresent()) {

            Series foundSeries = seriesOptional.get();

            List<SeasonData> temporadas = new ArrayList<>();

            byte howManySeasons = foundSeries.getHowManySeasons();
            
           // for (int i = 1; i <= howManySeasons; i++) {
                
                //var json = CONVERTER.convertData(ADDRESS + foundSeries.getTitle().replac

                //SeasonData dadosTemporada = CONVERTER.convertData(json, SeasonData.class);
                
                //temporadas.add(dadosTemporada);
           // }
            
            //temporadas.forEach(System.out::println);
        }

        else System.out.println("\nCouldn't find a series titled \"" + seriesName + '"' + '.');
    }



    private void querySeriesByTitle() {

        System.out.print("\nType series title: ");

        String seriesTitle = scanf.nextLine().strip();

        Optional<Series> queriedSeries = REPOSITORY.findByTitleContainingIgnoreCase(seriesTitle);

        if (queriedSeries.isPresent()) System.out.println(queriedSeries.get());

        else System.out.println("\nNo series was titled \"" + seriesTitle + '"' + '.');
    }



    private void querySeriesByActor() {

        List<Series> queriedSeries = new ArrayList<Series>();// to make sure it starts empty

        System.out.print("\nType an actor's name: ");

        String actorName = scanf.nextLine().strip();

        System.out.println("\nType the minimum rate the series with that actor need to have");

        System.out.print("in order to apear in the search: ");

        queriedSeries = REPOSITORY.findByActorsContainingIgnoreCaseAndGreaterThanEqual(actorName, extractDouble());

        if (queriedSeries.isEmpty()) System.out.println('\n' + '"' + actorName + "\" did not act in any series present in my data bank.");

        else queriedSeries.forEach(System.out::println);
    }



    private void bestFiveSeries() {

        REPOSITORY.findTop5ByOrderByRateDesc().forEach(System.out::println);
    }



    private void querySeriesByCategory() {

        List<Series> foundSeries = new ArrayList<>();

        while (true) {

            System.out.print("\nType the theme: ");

            String typedTheme = scanf.nextLine().strip();

            try {
                REPOSITORY.findByThemes(SeriesCategory.parseSeriesCategory(typedTheme)).forEach(foundSeries::add);

                break;
            }

            catch (Exception e) {

                System.out.println("\nThe problem \"" + e + "\" was detected.");

                System.out.println("Enter \"quit\" if you do not want to try typing the theme again,");

                System.out.print("or \"continue\", if you do: ");

                if (scanf.nextLine().toLowerCase().contains("q")) break;
            }
        }

        foundSeries.forEach(System.out::println);
    }



    private void optimizeWeekend() {

        System.out.print("\nType maximum seasons amount: ");

        double maximumSeasons = extractDouble();

        System.out.print("\nType minimum rate: ");

        double minimumRate = extractDouble();

        List<Series> foundSeries = REPOSITORY.findSeriesMaxSeasonsMinRate(maximumSeasons, minimumRate);

        foundSeries.forEach(System.out::println);
    }



    private void queryEpisodesTitlePiece() {

        System.out.print("\nType a piece of the episode's title: ");

        String piece = scanf.nextLine().strip();

        List<Episode> foundEpisodes = REPOSITORY.episodesByTitlePiece(piece);

        foundEpisodes.forEach(System.out::println);
    }



    public void useSeriesData () {

        boolean active = true;

        List<Episode> allEpisodes = searchForSeries();

        while (active) {

            System.out.print("""
                    Now, you can:
                    
                    1 - Quit the application
                    
                    2 - Just see ALL episodes
                    
                    3 - See episodes based on a date
                    
                    4 - See all episodes from an specified season
                    
                    5 - See the best five rated series
                    
                    6 - See the best five rated episodes
                    
                    7 - See of which season an episode is
                    
                    8 - See how each season was rated
                    
                    9 - See the summary statistics
                    
                    10 - List already searched series
                    
                    11 - Query series by title
                    
                    12 - Query series by actor
                    
                    13 - Query series by category
                    
                    14 - See all series with a maximum seasons amount AND a minimum rate
                    
                    15 - Query episodes by title piece
                    
                    Type the number on the left of the option you wish:\s""");

            int will = extractInteger();

            switch (will) {

                case 1 -> {

                    System.out.println("\nHave a good day! :)");

                    active = false;
                }

                case 2 -> showAll(allEpisodes);

                case 3 -> showBasedOnDate(allEpisodes);

                case 4 -> showBasedOnSeason(allEpisodes);

                case 5 -> bestFiveSeries();

                case 6 -> bestFiveEpisodes(allEpisodes);

                case 7 -> showSeasonOfEpisode(allEpisodes);

                case 8 -> showSeasonRates(allEpisodes);

                case 9 -> showSummaryStatistics(allEpisodes);

                case 10 -> alreadySearchedSeries();

                case 11 -> querySeriesByTitle();

                case 12 -> querySeriesByActor();

                case 13 -> querySeriesByCategory();

                case 14 -> optimizeWeekend();

                case 15 -> queryEpisodesTitlePiece();

                default -> System.out.println("\nPlease, type one of the numbers on the left of an option, according to the provided menu.");
            }
        }
    }
}