package silva.porto.guilherme.Screenmatch.DTO;

import silva.porto.guilherme.Screenmatch.models.episode.Episode;
import silva.porto.guilherme.Screenmatch.models.series.SeriesCategory;
import java.util.List;

public record DataTrasferSeries(

                                Long id,

                                String title,

                                String script,

                                byte howManySeasons,

                                Double rate,

                                SeriesCategory themes,

                                SeriesCategory actors,

                                List<Episode> episodes)

{ }