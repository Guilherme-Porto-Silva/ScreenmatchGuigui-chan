package silva.porto.guilherme.Screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import silva.porto.guilherme.Screenmatch.models.series.Series;
import silva.porto.guilherme.Screenmatch.models.series.SeriesCategory;
import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    Optional<Series> findByTitleContainingIgnoreCase(String seriesName);

    List<Series> findByActorsContainingIgnoreCaseAndGreaterThanEqual(String actorName, double rate);

    List<Series> findTop5ByOrderByRateDesc();

    List<Series> findByThemes(SeriesCategory themes);

    List<Series> findByHowManySeasonsLessThanEqualAndRateGreaterThanEqual(double maximumSeasons, double minimumRate);

    @Query("select s from Series s WHERE s.howManySeasons <= :maximumSeasons AND s.rate >= :minimumRate")
    List<Series> findSeriesMaxSeasonsMinRate(double maximumSeasons, double minimumRate);
}