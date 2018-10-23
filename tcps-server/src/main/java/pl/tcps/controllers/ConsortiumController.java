package pl.tcps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.services.ConsortiumService;

import java.security.Principal;
import java.util.Collection;

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
        return consortiumService.getConsortiumName(consortiumId);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<Collection<ConsortiumsEntity>> allConrsortiums(){

        Collection<ConsortiumsEntity> consortiumsEntities = consortiumService.getAllConsortiums();

        return new ResponseEntity<>(consortiumsEntities, HttpStatus.OK);
    }
}
