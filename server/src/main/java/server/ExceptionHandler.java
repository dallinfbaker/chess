package server;

public class ExceptionHandler {
    private String message;

    public ExceptionHandler(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
