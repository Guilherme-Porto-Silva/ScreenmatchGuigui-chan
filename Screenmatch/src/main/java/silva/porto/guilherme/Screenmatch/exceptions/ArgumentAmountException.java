package silva.porto.guilherme.Screenmatch.exceptions;

public class ArgumentAmountException extends WrongRequestException {

    public ArgumentAmountException() {

        super("Not preparade for that amount of arguments.");
    }
}