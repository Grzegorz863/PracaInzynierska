package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.exceptions.WrongPasswordException;
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

    @Override
    public void changeUserPassword(Long userId, String oldPassword, String newPassword) throws WrongPasswordException {
        UsersEntity usersEntity = userRepository.getOne(userId);

        final BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();

        if(!pwEncoder.matches(oldPassword, usersEntity.getUserPassword()))
            throw new WrongPasswordException("Wrong old password!");

        userRepository.updateUserPassword(userId, hashPassword(newPassword));
    }

    @Override
    public void deleteUser(Long userId, String password) throws WrongPasswordException {
        UsersEntity usersEntity = userRepository.getOne(userId);

        final BCryptPasswordEncoder pwEncoder = new BCryptPasswordEncoder();

        if(!pwEncoder.matches(password, usersEntity.getUserPassword()))
            throw new WrongPasswordException("Wrong password!");

        userRepository.delete(usersEntity);
    }

    private String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(4);
        return BCrypt.hashpw(password_plaintext, salt);
    }
}
