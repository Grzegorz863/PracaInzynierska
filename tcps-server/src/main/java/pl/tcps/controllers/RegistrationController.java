package pl.tcps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.services.RegistrationService;

@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/user")
    public UsersEntity registerUser(@RequestHeader("user_name") String userName,
                                  @RequestHeader("password") String password,
                                  @RequestHeader("role") String role,
                                  @RequestHeader("enable") String enable,
                                  @RequestHeader("first_name") String firstName,
                                  @RequestHeader("last_name") String lastName,
                                  @RequestHeader("email") String email){

        return registrationService.registerNewUser(userName, password, role, Boolean.parseBoolean(enable), firstName, lastName, email);
    }
}
