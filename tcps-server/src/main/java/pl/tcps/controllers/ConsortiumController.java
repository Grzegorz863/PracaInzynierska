package pl.tcps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.principal.LoggedUserPrincipal;
import pl.tcps.services.ConsortiumService;

import java.security.Principal;
import java.util.HashSet;

@RestController
public class ConsortiumController {

    private ConsortiumService consortiumService;

    @Autowired
    public ConsortiumController(ConsortiumService consortiumService) {
        this.consortiumService = consortiumService;
    }


    @GetMapping("/consortiums/id/{consortiumId}")
    public String consortiumsById (@PathVariable Long consortiumId, Principal principal){
        return consortiumService.getConsortium(consortiumId);
    }



    @GetMapping("/consortiums/name/{consortiumName}")
    public ConsortiumsEntity consortiumsByName (@PathVariable String consortiumName){
        return consortiumService.getConsortium(consortiumName);
    }
}
