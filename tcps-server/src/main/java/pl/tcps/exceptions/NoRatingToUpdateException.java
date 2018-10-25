package pl.tcps.exceptions;

public class NoRatingToUpdateException extends Exception {

    private String message;

    public NoRatingToUpdateException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
