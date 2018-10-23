package pl.tcps.services;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.exceptions.EntityNotFoundException;

import java.util.Collection;

@Service
public interface ConsortiumService {

    @PreAuthorize("hasAuthority('android_user')")
    String getConsortiumName(Long consortiumId);

    @PreAuthorize("hasAuthority('android_user')")
    ConsortiumsEntity getConsortium(String consortiumId) throws EntityNotFoundException;

    @PreAuthorize("hasAuthority('android_user')")
    Collection<ConsortiumsEntity> getAllConsortiums();
}

