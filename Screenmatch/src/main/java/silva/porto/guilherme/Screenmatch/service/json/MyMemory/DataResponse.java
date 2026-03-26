package silva.porto.guilherme.Screenmatch.service.json.MyMemory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DataResponse(String translatedText) { }