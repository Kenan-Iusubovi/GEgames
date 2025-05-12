package ge.games.gegames.exception;

import org.springframework.http.HttpStatus;

public class EmailTemplateException extends RuntimeException {

    private final HttpStatus status;

    public EmailTemplateException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public EmailTemplateException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
