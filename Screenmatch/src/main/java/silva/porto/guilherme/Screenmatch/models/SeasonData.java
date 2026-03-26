package silva.porto.guilherme.Screenmatch.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import silva.porto.guilherme.Screenmatch.models.episode.EpisodeData;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)

public record SeasonData(

        @JsonAlias("Season") Integer orderPosition,

        @JsonAlias("Episode") ArrayList<EpisodeData> episodes

) { }