package pl.tcps.controllers.exceptions_controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.WrongPasswordException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class UserExceptionController {

    @ExceptionHandler(value = EntityNotFoundException.class)
    public void handleEntityNotFoundException(HttpServletResponse response, EntityNotFoundException error) throws IOException {
        response.sendError(HttpStatus.NOT_FOUND.value(), error.getMessage());
    }

    @ExceptionHandler(value = WrongPasswordException.class)
    public void handleWrongPasswordException(HttpServletResponse response, WrongPasswordException error) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), error.getMessage());
    }
}
