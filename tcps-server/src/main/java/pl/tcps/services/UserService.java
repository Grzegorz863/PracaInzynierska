package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.exceptions.WrongPasswordException;
import pl.tcps.pojo.UserDetailsResponse;

import javax.persistence.EntityNotFoundException;

@Service
public interface UserService {

    Boolean isEnable(String userName);

    @PreAuthorize("hasAuthority('android_user')")
    Long getUserIdByName(String userName) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    UserDetailsResponse getLoggedUserDetails(String userName) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    void changeUserPassword(Long userId, String oldPassword, String newPassword) throws WrongPasswordException;

    @PreAuthorize("hasAuthority('android_user')")
    void deleteUser(Long userId, String password) throws WrongPasswordException;
}
