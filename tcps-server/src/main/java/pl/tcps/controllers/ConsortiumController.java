package pl.tcps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.dbEntities.PetrolStationEntity;
import pl.tcps.dbEntities.UsersEntity;
import pl.tcps.principal.LoggedUserPrincipal;
import pl.tcps.services.ConsortiumService;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/consortiums")
public class ConsortiumController {

    private ConsortiumService consortiumService;

    @Autowired
    public ConsortiumController(ConsortiumService consortiumService) {
        this.consortiumService = consortiumService;
    }


    @GetMapping("/id/{consortiumId}") //do usuniecia
    public String consortiumsById (@PathVariable Long consortiumId, Principal principal){
        return consortiumService.getConsortium(consortiumId);
    }

    @GetMapping("/name/{consortiumName}") //do usuniecia
    public ConsortiumsEntity consortiumsByName (@PathVariable String consortiumName){
        return consortiumService.getConsortium(consortiumName);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<ConsortiumsEntity>> allConrsortiums(){

        Collection<ConsortiumsEntity> consortiumsEntities = consortiumService.getAllConsortiums();

        return new ResponseEntity<>(consortiumsEntities, HttpStatus.OK);
    }
}
