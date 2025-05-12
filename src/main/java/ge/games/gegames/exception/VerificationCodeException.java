package ge.games.gegames.exception;

import ge.games.gegames.security.exception.RestApiException;
import org.springframework.http.HttpStatus;

public class VerificationCodeException extends RestApiException {

    private static final String MESSAGE = ". Please try resending the verification code by " +
            "logging in and clicking 'Resend Verification Email'.If the problem persists, " +
            "please contact our support team at gamesgestudio@gmail.com.";

    private VerificationCodeException(HttpStatus status, String message) {
        super(status, message);
    }

    public static VerificationCodeException notFound(String email) {
        return new VerificationCodeException(HttpStatus.NOT_FOUND,
                "No verification code found for email: " + email + MESSAGE);
    }

    public static VerificationCodeException incorrectCode() {
        return new VerificationCodeException(HttpStatus.BAD_REQUEST,
                "The verification code is incorrect" + MESSAGE);
    }

    public static VerificationCodeException expired() {
        return new VerificationCodeException(HttpStatus.GONE,
                "The verification code has expired. Please request a new one" + MESSAGE);
    }
}
