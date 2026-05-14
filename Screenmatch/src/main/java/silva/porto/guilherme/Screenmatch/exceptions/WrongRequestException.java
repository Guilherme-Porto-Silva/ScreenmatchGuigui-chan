package silva.porto.guilherme.Screenmatch.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import silva.porto.guilherme.Screenmatch.DTO.ErrorResponse;
import java.time.LocalDateTime;

public class WrongRequestException extends IllegalArgumentException {

    public WrongRequestException (String message) {
        super(message);
    }

    public ResponseEntity<ErrorResponse> claimWrongRequest (){

        ErrorResponse body = new ErrorResponse(getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}