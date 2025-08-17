import java.util.UUID;

public class InvalidClientException extends Exception {
    public InvalidClientException(UUID userID) {
        super("Invalid Client UUID: " + userID);
    }
}
