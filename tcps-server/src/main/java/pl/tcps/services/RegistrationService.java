package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.exceptions.UserAlreadyExistsException;

@Service
public interface RegistrationService {

    @PreAuthorize("hasAuthority('registration_user')")
    UsersEntity registerNewUser(String username, String password, String userRole, Boolean isEnabled, String firstName, String lastName, String email) throws UserAlreadyExistsException;
}
