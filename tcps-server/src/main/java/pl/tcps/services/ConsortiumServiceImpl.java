package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.repositories.ConsortiumRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;

@Service
public class ConsortiumServiceImpl implements ConsortiumService {

    private ConsortiumRepository consortiumRepository;

    @Autowired
    public ConsortiumServiceImpl(ConsortiumRepository consortiumRepository) {
        this.consortiumRepository = consortiumRepository;
    }

    @Override
    public ConsortiumsEntity getConsortium(String consortiumName) throws EntityNotFoundException{
        ConsortiumsEntity consortiumsEntity = consortiumRepository.findByConsortiumName(consortiumName);

        if (consortiumsEntity == null)
            throw new EntityNotFoundException("Searched consortium not found!");
        return consortiumsEntity;
    }

    @Override
    public String getConsortiumName(Long consortiumId) {
        return consortiumRepository.findByConsortiumId(consortiumId).getConsortiumName();
    }

    @Override
    public Collection<ConsortiumsEntity> getAllConsortiums() {
        return consortiumRepository.findAll();
    }
}

