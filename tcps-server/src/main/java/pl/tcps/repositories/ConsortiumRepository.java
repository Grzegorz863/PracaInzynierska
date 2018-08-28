package pl.tcps.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.tcps.dbEntities.ConsortiumsEntity;

public interface ConsortiumRepository extends JpaRepository<ConsortiumsEntity, Long> {
    ConsortiumsEntity findByConsortiumId(Long consortiumId);
    ConsortiumsEntity findByConsortiumName(String consortiumName);
}
