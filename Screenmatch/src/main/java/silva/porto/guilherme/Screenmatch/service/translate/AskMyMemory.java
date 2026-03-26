package silva.porto.guilherme.Screenmatch.service.translate;

import java.net.URLEncoder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import silva.porto.guilherme.Screenmatch.service.json.DataGET;
import silva.porto.guilherme.Screenmatch.service.json.MyMemory.DataTranslation;

public class AskMyMemory {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String translate (String... args) {

        String texto = URLEncoder.encode(args[0]);

        String langpair = URLEncoder.encode("en|pt-br");

        if (args.length > 1) langpair = URLEncoder.encode("en|" + args[1]);

        String json = DataGET.getData("https://api.mymemory.translated.net/get?q=" + texto + "&langpair=" + langpair);

        try {

            DataTranslation translator = MAPPER.readValue(json, DataTranslation.class);

            return translator.responseData().translatedText();
        }

        catch (JsonProcessingException e) { throw new RuntimeException("Failed to map the translation into a DataTranslation record.", e); }
    }
}