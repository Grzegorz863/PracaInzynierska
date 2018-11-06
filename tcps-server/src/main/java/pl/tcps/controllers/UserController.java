package pl.tcps.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import pl.tcps.exceptions.EntityNotFoundException;
import pl.tcps.exceptions.WrongPasswordException;
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

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping(value = "/me/password", produces = "application/json")
    public void changeUserPassword(Authentication authentication, @RequestHeader("old_password") String oldPassword,
                                   @RequestHeader("new_password") String newPassword) throws EntityNotFoundException, WrongPasswordException{

        String userName = authentication.getName();
        Long userId = userService.getUserIdByName(userName);

        userService.changeUserPassword(userId, oldPassword, newPassword);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/me", produces = "application/json")
    public void deleteUser(Authentication authentication, @RequestHeader("password") String password)
            throws EntityNotFoundException, WrongPasswordException{

        String userName = authentication.getName();
        Long userId = userService.getUserIdByName(userName);

        userService.deleteUser(userId, password);
    }
}
