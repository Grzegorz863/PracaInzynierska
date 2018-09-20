package pl.tcps.services;

import org.springframework.stereotype.Service;

@Service
public interface UserService {

    Boolean isEnable(String userName);

}
