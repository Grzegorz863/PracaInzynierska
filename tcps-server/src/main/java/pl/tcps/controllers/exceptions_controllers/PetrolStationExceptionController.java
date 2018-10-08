package pl.tcps.controllers.exceptions_controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.PetrolStationAlreadyExistsException;
import pl.tcps.exceptions.WrongPetrolStationAddressException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class PetrolStationExceptionController {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public void handleEntityNotFoundException(HttpServletResponse response, EntityNotFoundException error) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value(), error.getMessage());
    }

    @ExceptionHandler(value = PetrolStationAlreadyExistsException.class)
    public void handlePetrolStationAlreadyExistsException(HttpServletResponse response, PetrolStationAlreadyExistsException error) throws IOException{
        response.sendError(HttpStatus.SEE_OTHER.value(), error.getMessage());
    }

    @ExceptionHandler(value = WrongPetrolStationAddressException.class)
    public void handleWrongPetrolStationAddressException(HttpServletResponse response, PetrolStationAlreadyExistsException error) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value(), error.getMessage());
    }

    @ExceptionHandler(value = JsonProcessingException.class)
    public void handleJsonProcessingException(HttpServletResponse response, JsonProcessingException error) throws IOException{
        response.sendError(HttpStatus.NOT_FOUND.value(), error.getMessage());
    }
}
