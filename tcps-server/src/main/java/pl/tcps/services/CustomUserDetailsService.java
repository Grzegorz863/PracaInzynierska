package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.principal.LoggedUserPrincipal;
import pl.tcps.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        UsersEntity usersEntity = userRepository.findByUserName(userName);
        if(userName == null)
            throw new UsernameNotFoundException(userName);

        return new LoggedUserPrincipal(usersEntity);
    }
}
