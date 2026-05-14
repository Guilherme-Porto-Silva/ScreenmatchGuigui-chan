package silva.porto.guilherme.Screenmatch.exceptions;

public class DataConvertionException extends WrongRequestException {

    public DataConvertionException (String json) {

        super("Maybe, \"" + json + "\" was not a valid JavaScript object notation.");
    }
}