package ge.games.gegames.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public static UserAlreadyExistsException forField(String fieldName, String value) {
        return new UserAlreadyExistsException("A user with " + fieldName + " '" + value + "' already exists. Please try " +
                "login with Gmail");
    }
}
