package ge.games.gegames.exception;

public class EntityMappingException extends RuntimeException {

    public EntityMappingException(String message) {
        super(message);
    }

    public static EntityMappingException noEntity(String className) {
        return new EntityMappingException("Mapping error: entity '" + className + "' is null. Unable to map to DTO.");
    }

    public static EntityMappingException blankField(String fieldName, String className) {
        return new EntityMappingException("Mapping error: field '" + fieldName + "' is blank in '" + className + "'.");
    }

    public static EntityMappingException custom(String message) {
        return new EntityMappingException("Mapping error: " + message);
    }
}
