package pl.tcps.controllers.exceptions_controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.tcps.exceptions.UserAlreadyExistsException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestControllerAdvice
public class RegistrationExceptionController {

    @ExceptionHandler(value = UserAlreadyExistsException.class)
    public void handleUserAlreadyExistsException(HttpServletResponse response, UserAlreadyExistsException error) throws IOException{
        response.sendError(HttpStatus.SEE_OTHER.value(), error.getMessage());
    }
}
