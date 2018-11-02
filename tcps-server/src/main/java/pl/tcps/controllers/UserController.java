package pl.tcps.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.tcps.pojo.UserDetailsResponse;
import pl.tcps.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/me/info", produces = "application/json")
    public ResponseEntity<UserDetailsResponse> getLoggedUserDetails(Authentication authentication){

        return new ResponseEntity<>(userService.getLoggedUserDetails(authentication.getName()), HttpStatus.OK) ;
    }
}
