package silva.porto.guilherme.Screenmatch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import silva.porto.guilherme.Screenmatch.DTO.DataTrasferEpisode;
import silva.porto.guilherme.Screenmatch.DTO.DataTrasferSeries;
import silva.porto.guilherme.Screenmatch.service.SeriesService;

@RestController

@RequestMapping("/series")

public class SeriesController {

    @Autowired private SeriesService service;

    @GetMapping public List<DataTrasferSeries> getSeries(){

        return service.getSeries();
    }

    @GetMapping("/lancamentos") public List<DataTrasferSeries> getRelizesSeries(){

        return service.getRelizesSeries();
    }

    @GetMapping("/top5") public List<DataTrasferSeries> getBestFive(){

        return service.getBestFive();
    }

    @GetMapping("/{id}") public DataTrasferSeries getById (@PathVariable Long id) {

        return service.getSeriesById(id);
    }

    @GetMapping("/{id}/temporadas/todas") public List<DataTrasferEpisode> getAllSeasons (@PathVariable Long id) {

        return service.getAllSeasons(id);
    }

    @GetMapping("/{id}/temporadas/todas/{seasonOrderPosition}") public List<DataTrasferEpisode> getSeason (@PathVariable Long id, @PathVariable byte seasonOrderPosition) {

        return service.getSeason(id, seasonOrderPosition);
    }

    @GetMapping("/categoria/{theme}") public List<DataTrasferSeries> getSeriesByTheme (@PathVariable String theme) {

        return service.getSeriesByTheme(theme);
    }

    @GetMapping("id/temporadas/top") public List<DataTrasferEpisode> seriesBestFive (@PathVariable Long id) {

        return service.seriesBestFive(id);
    }
}