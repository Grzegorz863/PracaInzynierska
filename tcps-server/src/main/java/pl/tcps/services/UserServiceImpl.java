package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.repositories.UserRepository;

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
    public Long getUserIdByName(String userName) {
        UsersEntity usersEntity = userRepository.findByUserName(userName);
        return usersEntity.getUserId();
    }
}
