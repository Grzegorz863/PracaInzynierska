package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import pl.tcps.dbEntities.UsersEntity;

public interface UserRepository extends JpaRepository<UsersEntity, Long> {

    UsersEntity findByUserName(String userName);

    Boolean existsByUserName(String userName);

    //Boolean existsByEmail(String email);

    @Modifying
    @Transactional
    @Query("update UsersEntity u set u.userPassword = :new_password where u.userId = :user_id")
    void updateUserPassword(@Param("user_id") Long userId, @Param("new_password") String newPassword);
}
