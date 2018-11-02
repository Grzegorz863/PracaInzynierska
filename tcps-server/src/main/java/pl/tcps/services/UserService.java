package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.pojo.UserDetailsResponse;

import javax.persistence.EntityNotFoundException;

@Service
public interface UserService {

    Boolean isEnable(String userName);

    @PreAuthorize("hasAuthority('android_user')")
    Long getUserIdByName(String userName) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    UserDetailsResponse getLoggedUserDetails(String userName) throws EntityNotFoundException;
}
