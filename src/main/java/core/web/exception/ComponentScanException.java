package core.web.exception;

public class ComponentScanException extends RuntimeException {

    public ComponentScanException(String message) {
        super("Failed to scan components: " + message);
    }
}
