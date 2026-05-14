package silva.porto.guilherme.Screenmatch.DTO;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponse (String message, HttpStatus status, LocalDateTime when) { }