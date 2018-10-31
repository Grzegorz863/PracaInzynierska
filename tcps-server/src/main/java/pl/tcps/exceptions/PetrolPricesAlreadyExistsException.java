package pl.tcps.exceptions;

public class PetrolPricesAlreadyExistsException extends Exception {

    private String message;

    public PetrolPricesAlreadyExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
