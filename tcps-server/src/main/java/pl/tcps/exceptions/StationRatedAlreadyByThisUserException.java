package pl.tcps.exceptions;

public class StationRatedAlreadyByThisUserException extends Exception {

    private String message;

    public StationRatedAlreadyByThisUserException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
