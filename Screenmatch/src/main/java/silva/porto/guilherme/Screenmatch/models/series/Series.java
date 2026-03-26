package silva.porto.guilherme.Screenmatch.models.series;

import jakarta.persistence.*;
import silva.porto.guilherme.Screenmatch.models.episode.Episode;
import silva.porto.guilherme.Screenmatch.service.translate.AskChatGPT;
import silva.porto.guilherme.Screenmatch.service.translate.AskMyMemory;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;

@Entity

@Table(name = "series") public class Series {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private long id;

    @Column(unique = true) private final String title;

    private String script;// couldn't be final due to the try-catch mechanic

    private final byte howManySeasons;

    private final Double rate;

    @Enumerated(EnumType.STRING) private final SeriesCategory themes, actors;

    private static final AskChatGPT askChatGPT = new AskChatGPT(System.getenv("OpenAI_API_KEY"));

    @OneToMany(mappedBy = "series", cascade = CascadeType.ALL, fetch = FetchType.EAGER) private final List<Episode> episodes = new ArrayList<>();



    public Series() {

        themes = null;

        actors = null;

        title = "";

        howManySeasons = 0;

        rate = 0.0;
    }



    public Series (SeriesData data) {

        this.title = data.title();

        this.howManySeasons = (byte) data.howManySeasons();

        this.themes = SeriesCategory.parseSeriesCategory(data.themes().split(", ")[0].strip());

        this.rate = OptionalDouble.of(Double.parseDouble(data.rate())).orElse(0.0);

        this.actors = SeriesCategory.parseSeriesCategory(data.actors());



        try{ this.script = askChatGPT.translate(data.script()); }

        catch (Exception e) {

            try{ this.script = AskMyMemory.translate(data.script()); }

            catch (Exception ex) { this.script = data.title() + " does not have a sinopses yet."; }
        }
    }


    public Long getId() { return id; }

    public String getTitle() { return title; }

    public String getScript() { return script; }

    public byte getHowManySeasons() { return howManySeasons; }

    public SeriesCategory getActors() { return actors; }

    public SeriesCategory getThemes() { return themes; }

    public Double getRate() { return rate; }



    @Override public String toString() {

        return "The series \"" + title + "\"'s script is \"" + script + "\", it has " + howManySeasons +

                " seasons, was rated " + rate +  ", and is themed around " + themes + '.' + ' ' +

                actors + " make a role there.";
    }
}