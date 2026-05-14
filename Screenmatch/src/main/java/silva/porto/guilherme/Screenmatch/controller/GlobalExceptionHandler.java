package silva.porto.guilherme.Screenmatch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import silva.porto.guilherme.Screenmatch.DTO.ErrorResponse;
import silva.porto.guilherme.Screenmatch.exceptions.MyBad;
import silva.porto.guilherme.Screenmatch.exceptions.WrongRequestException;
import java.io.IOException;
import java.time.LocalDateTime;

@RestControllerAdvice

public class GlobalExceptionHandler {

    @ExceptionHandler(WrongRequestException.class) public ResponseEntity<ErrorResponse> wrongRequest

      (WrongRequestException doneWrongRequest) { return doneWrongRequest.claimWrongRequest(); }



    @ExceptionHandler(MyBad.class) public ResponseEntity<ErrorResponse> apologize

            (MyBad forMyBad) { return forMyBad.apologize(); }



    @ExceptionHandler(IOException.class) public ResponseEntity<ErrorResponse> wrongCastDeal (){

        ErrorResponse body = new ErrorResponse("You might have typed something wrong.", HttpStatus.BAD_REQUEST, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }



    @ExceptionHandler(InterruptedException.class) public ResponseEntity<ErrorResponse> interruption (){

        ErrorResponse body = new ErrorResponse("The system's work was interrupted.", HttpStatus.SERVICE_UNAVAILABLE, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(body);
    }



    @ExceptionHandler(ClassCastException.class) public ResponseEntity<ErrorResponse> wrongCastDeal

     (ClassCastException wrongCast) {

       ErrorResponse body = new ErrorResponse(wrongCast.getMessage(), HttpStatus.BAD_REQUEST, LocalDateTime.now());

       return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }



    @ExceptionHandler(JsonProcessingException.class) public ResponseEntity<ErrorResponse> jsonError (){

        ErrorResponse body = new ErrorResponse("I had an issue parsing a JSON.", HttpStatus.EXPECTATION_FAILED, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(body);
    }



    @ExceptionHandler(Exception.class) public ResponseEntity<ErrorResponse> mattress

      (Exception problam) {

        ErrorResponse body = new ErrorResponse(problam.getMessage(), null, LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }
}