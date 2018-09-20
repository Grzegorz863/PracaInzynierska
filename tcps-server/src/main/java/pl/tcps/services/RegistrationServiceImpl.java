package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.repositories.UserRepository;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    private UserRepository userRepository;



    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UsersEntity registerNewUser(String username, String password, String userRole, Boolean isEnabled, String firstName, String lastName, String email) {
        UsersEntity usersEntity = new UsersEntity();

        usersEntity.setUserName(username);
        usersEntity.setUserPassword(hashPassword(password));
        usersEntity.setUserRole(userRole);
        usersEntity.setIsEnabled(isEnabled);
        usersEntity.setUserFirstName(firstName);
        usersEntity.setUserLastName(lastName);
        usersEntity.setUserEmail(email);

        userRepository.save(usersEntity);

        return usersEntity;
    }

    private String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(4);
        return BCrypt.hashpw(password_plaintext, salt);
    }
}
