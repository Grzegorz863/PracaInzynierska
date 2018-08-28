package pl.tcps.services;

import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.ConsortiumsEntity;

@Service
public interface ConsortiumService {

    String getConsortium(Long consortiumId);
    ConsortiumsEntity getConsortium(String consortiumId);
}

