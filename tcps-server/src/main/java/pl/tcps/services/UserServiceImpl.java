package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.pojo.UserDetailsResponse;
import pl.tcps.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Boolean isEnable(String userName) {
        return userRepository.findByUserName(userName).getIsEnabled();
    }

    @Override
    public Long getUserIdByName(String userName) throws EntityNotFoundException {

        UsersEntity usersEntity = userRepository.findByUserName(userName);
        if (usersEntity == null)
            throw new EntityNotFoundException(String.format("User %s not found", userName));

        return usersEntity.getUserId();
    }

    @Override
    public UserDetailsResponse getLoggedUserDetails(String userName) throws EntityNotFoundException {

        UsersEntity usersEntity = userRepository.findByUserName(userName);
        if (usersEntity == null)
            throw new EntityNotFoundException(String.format("User %s not found", userName));

        return new UserDetailsResponse(usersEntity.getFirstName(), usersEntity.getLastName(), usersEntity.getEmail());
    }
}
