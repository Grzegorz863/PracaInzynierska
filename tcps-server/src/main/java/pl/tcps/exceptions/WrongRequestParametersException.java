package pl.tcps.exceptions;

public class WrongRequestParametersException extends Exception {

    private String message;

    public WrongRequestParametersException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
