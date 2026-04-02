package silva.porto.guilherme.Screenmatch.service.json;

import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConvertData implements IConvertData {

    private static final ObjectMapper mapper = new ObjectMapper();

    private static final Pattern DATE = Pattern.compile("(\\d{2})/(\\d{2})/(\\d{4})");



    @Override public <T> T convertData(String json, Class<T> type) {

        try{ return mapper.readValue(json, type); }

        catch (JacksonException e) {

            System.out.println("\nThe problem " + e + " happened.");

            System.out.println("Maybe, \"" + json + "\" was not a valid JavaScript object notation.");

            throw e;
        }
    }



    public LocalDate brazilianDate (String typedDate) {

        Matcher dateMatcher = DATE.matcher(typedDate);

        if (dateMatcher.find()) try {

            int day = Integer.parseInt(dateMatcher.group(1));

            int month = Integer.parseInt(dateMatcher.group(2));

            int year = Integer.parseInt(dateMatcher.group(3));

            return LocalDate.of(year, month, day);
        }

        catch (DateTimeException e) {

            throw new ClassCastException("Failed to convert what the user typed (\"" + typedDate + "\") into a LocalDate class because they typed and invalid number.");
        }

        else throw new ClassCastException("Failed to convert what the user typed (\"" + typedDate + "\") into a LocalDate class using regular expression.");
    }
}