package silva.porto.guilherme.Screenmatch.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import silva.porto.guilherme.Screenmatch.DTO.ErrorResponse;

import java.time.LocalDateTime;

public class TranslationProcessingException extends RuntimeException {

    public TranslationProcessingException (String msg) { super(msg); }

    public TranslationProcessingException() {

        super("Failed to map the translation into a DataTranslation record.");
    }

    public ResponseEntity<ErrorResponse> apologize () {

        ErrorResponse body = new ErrorResponse(getMessage(), HttpStatus.EXPECTATION_FAILED, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(body);
    }
}