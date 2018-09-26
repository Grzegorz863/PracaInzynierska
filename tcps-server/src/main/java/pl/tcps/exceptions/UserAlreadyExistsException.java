package pl.tcps.exceptions;

public class UserAlreadyExistsException extends Exception {

    private String message;

    public UserAlreadyExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
