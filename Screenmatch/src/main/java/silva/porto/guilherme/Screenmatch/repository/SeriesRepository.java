package silva.porto.guilherme.Screenmatch.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import silva.porto.guilherme.Screenmatch.models.series.Series;
import java.util.List;
import java.util.Optional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    Optional<Series> findByTitleContainingIgnoreCase(String seriesName);

    List<Series> findByActorsContainingIgnoreCase(String actorName);
}