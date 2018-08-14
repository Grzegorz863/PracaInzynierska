package pl.tcps.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.tcps.repositories.ConsortiumRepository;

@Service
public class ConsortiumServiceImpl implements ConsortiumService {

    @Autowired
    ConsortiumRepository consortiumRepository;


    @Override
    public String getConsortium(Long consortiumId) {
        //return "fdsac";
        return consortiumRepository.findByConsortiumId(consortiumId).getConsortiumName();
    }
}

