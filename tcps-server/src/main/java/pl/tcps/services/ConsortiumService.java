package pl.tcps.services;

import org.springframework.stereotype.Service;

@Service
public interface ConsortiumService {

    String getConsortium(Long consortiumId);
}

