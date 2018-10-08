package pl.tcps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.exceptions.UserAlreadyExistsException;
import pl.tcps.services.RegistrationService;

import java.net.URI;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/user", produces = "application/json")
    public ResponseEntity<UsersEntity> registerUser(@RequestHeader("user_name") String userName,
                                  @RequestHeader("password") String password,
                                  @RequestHeader("role") String role,
                                  @RequestHeader("enable") String enable,
                                  @RequestHeader("first_name") String firstName,
                                  @RequestHeader("last_name") String lastName,
                                  @RequestHeader("email") String email,
                                    UriComponentsBuilder uriComponentsBuilder ) throws UserAlreadyExistsException{


        UsersEntity usersEntity = registrationService.registerNewUser(userName, password, role, Boolean.parseBoolean(enable), firstName, lastName, email);


        HttpHeaders httpHeaders = new HttpHeaders();
        URI locationUri = uriComponentsBuilder
                .path("/registration/user")
                .path(String.valueOf(usersEntity.getUserId()))
                .build()
                .toUri();
        httpHeaders.setLocation(locationUri);

        return new ResponseEntity<>(usersEntity, httpHeaders, HttpStatus.CREATED);
    }

}