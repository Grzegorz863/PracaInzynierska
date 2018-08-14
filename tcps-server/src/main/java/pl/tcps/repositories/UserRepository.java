package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.UsersEntity;

public interface UserRepository extends JpaRepository<UsersEntity, Long> {
    UsersEntity findByUserName(String userName);
}
