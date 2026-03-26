package silva.porto.guilherme.Screenmatch.models.episode;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record EpisodeData(

        @JsonAlias("Title") String title,

        @JsonAlias("Episode") Integer orderPosition,

        @JsonAlias("imdbRating") String rate,

        @JsonAlias("Released") String releaseDate

) { }