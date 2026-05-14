package silva.porto.guilherme.Screenmatch.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import silva.porto.guilherme.Screenmatch.DTO.ErrorResponse;
import java.time.LocalDateTime;

public class MyBad extends Exception {

    public MyBad (String message) {
        super(message);
    }

    public ResponseEntity<ErrorResponse> apologize (){

        ErrorResponse body = new ErrorResponse(getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}