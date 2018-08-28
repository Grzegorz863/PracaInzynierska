package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.repositories.ConsortiumRepository;

@Service
public class ConsortiumServiceImpl implements ConsortiumService {

    @Autowired
    ConsortiumRepository consortiumRepository;

    @Override
    public ConsortiumsEntity getConsortium(String consortiumName) {
        return consortiumRepository.findByConsortiumName(consortiumName);
    }

    @Override
    public String getConsortium(Long consortiumId) {
        //return "fdsac";
        return consortiumRepository.findByConsortiumId(consortiumId).getConsortiumName();
    }
}

