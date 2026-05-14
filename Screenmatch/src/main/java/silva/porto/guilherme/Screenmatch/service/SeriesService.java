package silva.porto.guilherme.Screenmatch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import silva.porto.guilherme.Screenmatch.DTO.DataTrasferEpisode;
import silva.porto.guilherme.Screenmatch.DTO.DataTrasferSeries;
import silva.porto.guilherme.Screenmatch.exceptions.NotInDataBank;
import silva.porto.guilherme.Screenmatch.models.episode.Episode;
import silva.porto.guilherme.Screenmatch.models.series.Series;
import silva.porto.guilherme.Screenmatch.models.series.SeriesCategory;
import silva.porto.guilherme.Screenmatch.repository.SeriesRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class SeriesService {

    @Autowired private SeriesRepository repository;



    private List<DataTrasferSeries> filter (List<Series> filteredSeries) {

        return filteredSeries.stream().map(

          s -> new DataTrasferSeries(s.getId(), s.getTitle(), s.getScript(), s.getHowManySeasons(), s.getRate(), s.getThemes(), s.getActors(), s.getEpisodes()))

           .collect(Collectors.toList());
    }



    private DataTrasferSeries filter (Series filteredSeries) {

        return new DataTrasferSeries(filteredSeries.getId(), filteredSeries.getTitle(), filteredSeries.getScript(), filteredSeries.getHowManySeasons(), filteredSeries.getRate(), filteredSeries.getThemes(), filteredSeries.getActors(), filteredSeries.getEpisodes());
    }



    private List<DataTrasferEpisode> filterEpisode (List<Episode> filteredEpisodes) {

        return filteredEpisodes.stream().map(

                        e -> new DataTrasferEpisode(e.getTitle(), e.getOrderPosition(), e.getSeason()))

                .collect(Collectors.toList());
    }

    private Series tryGettingById (Long id) {

        Optional<Series> possiblyFoundSeries = repository.findById(id);

        if (possiblyFoundSeries.isPresent())

            return possiblyFoundSeries.get();

        throw new NotInDataBank("series", id);
    }



    public List<DataTrasferSeries> getSeries(){

        return filter(repository.findAll());
    }



    public List<DataTrasferSeries> getOldest(){

        return filter(repository.findTop5ByOrderByEpisodesReleaseDate());
    }



    public List<DataTrasferSeries> getRelizesEpisodes(){

        return filter(repository.findTop5ByOrderByEpisodesReleaseDateDesc());
    }



    public List<DataTrasferSeries> getRelizesSeries(){

        return filter(repository.lastEpisodes());
    }



    public List<DataTrasferSeries> getBestFive(){

        return filter(repository.findTop5ByOrderByRateDesc());
    }



    public DataTrasferSeries getSeriesById (Long id) {

        try{
            Series actuallyFoundSeries = tryGettingById(id);

            return filter(actuallyFoundSeries);
        }

        catch (Exception e){ return null; }
    }



    public List<DataTrasferEpisode> getAllSeasons (Long id) {

        try{
            Series actuallyFoundSeries = tryGettingById(id);

            return filterEpisode(actuallyFoundSeries.getEpisodes());
        }

        catch (Exception e) { return null; }
    }



    public List<DataTrasferEpisode> getSeason (Long id, byte seasonOrderPosition) {

        return filterEpisode(repository.getEpisodesBySeason(id, seasonOrderPosition));
    }



    public List<DataTrasferSeries> getSeriesByTheme (String theme) {

        return filter(repository.findByThemes(SeriesCategory.parseSeriesCategory(theme)));
    }



    public List<DataTrasferEpisode> seriesBestFive (Long id) {

        try{
             return filterEpisode(

               tryGettingById(id)

                 .getEpisodes()

                   .stream()

                     .sorted(Comparator.comparing(Episode::getRate))

                       .limit(5)

                         .toList());
                                    }

        catch (Exception e) { return null; }
    }
}