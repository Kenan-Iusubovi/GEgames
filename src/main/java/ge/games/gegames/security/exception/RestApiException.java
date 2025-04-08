package ge.games.gegames.security.exception;

import org.springframework.http.HttpStatus;

public class RestApiException extends RuntimeException{

    private final HttpStatus httpStatus;

    public RestApiException(HttpStatus httpStatus, String message) {
        super(message);
        this.httpStatus = httpStatus;
    }
}
