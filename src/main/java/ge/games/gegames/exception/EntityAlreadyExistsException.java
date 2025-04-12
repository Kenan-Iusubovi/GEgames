package ge.games.gegames.exception;

public class EntityAlreadyExistsException extends RuntimeException {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }

    public static EntityAlreadyExistsException forField(String fieldName, String value) {
        return new EntityAlreadyExistsException("A user with " + fieldName + " '" + value + "' already exists.");
    }
}
