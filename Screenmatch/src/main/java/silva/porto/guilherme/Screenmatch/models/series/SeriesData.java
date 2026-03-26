package silva.porto.guilherme.Screenmatch.models.series;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)// do not try to get what you don't find

public record SeriesData(

        @JsonAlias("Title") String title,// locate "Title", but save as "title"

        @JsonAlias("totalSeasons") byte howManySeasons,

        @JsonAlias("imdbRating") String rate,

        @JsonAlias("Plot") String script,

        @JsonProperty("Genre") String themes,// locate "Genre", and use as "themes" here, but save as "Genre" as well

        @JsonAlias("Actors") String actors
) { }