package silva.porto.guilherme.Screenmatch.exceptions;

public class NotInDataBank extends WrongRequestException {

    public NotInDataBank (String lookedFor, Long id) {

        super("Could not find a " + lookedFor + " identified as " + id);
    }
}