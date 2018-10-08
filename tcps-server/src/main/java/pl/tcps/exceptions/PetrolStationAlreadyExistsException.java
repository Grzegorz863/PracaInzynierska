package pl.tcps.exceptions;

public class PetrolStationAlreadyExistsException extends Exception {

    private String message;

    public PetrolStationAlreadyExistsException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
