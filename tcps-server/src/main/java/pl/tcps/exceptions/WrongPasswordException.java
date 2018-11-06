package pl.tcps.exceptions;

public class WrongPasswordException extends Exception {

    private String message;

    public WrongPasswordException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
