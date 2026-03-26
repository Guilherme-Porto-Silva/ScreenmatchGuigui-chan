package silva.porto.guilherme.Screenmatch.models.episode;

import jakarta.persistence.*;
import silva.porto.guilherme.Screenmatch.models.series.Series;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity

@Table(name = "episodes") public class Episode {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;

    private static final DateTimeFormatter DATE_FORM = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private int season;

    public int getSeason() { return season; }

    private String title;

    public String getTitle() { return title; }

    private int orderPosition;

    private double rate;

    public double getRate() { return rate; }

    private LocalDate releaseDate;

    public LocalDate getReleaseDate() { return releaseDate; }

    @ManyToMany

    @JoinTable(name = "series_episode", joinColumns = @JoinColumn(name = "episode_id"), inverseJoinColumns = @JoinColumn(name = "series_id"))

    private Series series;

    public Episode(){}



    public Episode(EpisodeData data, int season, Series series) {

        this.season = season;

        this.title = data.title();

        this.orderPosition = data.orderPosition();

        this.series = series;



        try{ this.rate = Double.parseDouble(data.rate()); }

        catch (NumberFormatException e) {

            System.out.println("\nFailed to parse the rate from imdb into a Double.");

            System.out.println(e.getCause());

            this.rate = 0.0;
        }



        try{ this.releaseDate = LocalDate.parse(data.releaseDate()); }

        catch (DateTimeParseException e) {

            System.out.println("\nFailed to parse a date from imdb into a LocalDate.");

            System.out.println(e.getCause());

            System.out.println("The date was listed as \"" + data.releaseDate() + "\" in imdb.");

            this.releaseDate = null;
        }
    }

    public Episode(EpisodeData data, int season) { new Episode(data, season, null); }


    @Override public String toString() {

        return "season = " + season +
                "\ntitle = '" + title + '\'' +
                "\norder position = " + orderPosition +
                "\nrate = " + rate +
                "\nrelease date = " + releaseDate.format(DATE_FORM);
    }



    public void println() {

        System.out.println('\n' + "=".repeat(20));

        System.out.println("\nTitle: " + title);

        System.out.println("\nSeason: " + season);

        System.out.println("\nOrder of position in the episodes list and in time: " + orderPosition);

        System.out.println("\nRate: " + rate);

        System.out.println("\nDate of release: " + releaseDate.format(DATE_FORM));

        System.out.println('\n' + "=".repeat(20));
    }

//    @Override public int compareTo(Object o) {
//
//        try {
//
//            Episode compared = (Episode) o;
//
//            return rate.compareTo(compared.getRate());
//        }
//
//        catch (ClassCastException e) {
//
//            try { return rate.compareTo((Double) o); }
//
//            catch (ClassCastException cce) {
//
//                try { return rate.compareTo(((Integer) o) + 0.0); }
//
//                catch (ClassCastException classCastException) {
//
//                    throw new EpisodeCompareException(e, o);
//                }
//            }
//        }
//    }
}