package pl.tcps.exceptions;

public class PetrolPricesNotExistsException extends Exception{

    private String message;

    public PetrolPricesNotExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
