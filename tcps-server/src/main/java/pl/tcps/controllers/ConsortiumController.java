package pl.tcps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.tcps.dbEntities.ConsortiumsEntity;
import pl.tcps.services.ConsortiumService;

@RestController
public class ConsortiumController {

    ConsortiumService consortiumService;

    @Autowired
    public ConsortiumController(ConsortiumService consortiumService) {
        this.consortiumService = consortiumService;
    }

    @GetMapping("/consortiums/id/{consortiumId}")
    public String consortiumsById (@PathVariable Long consortiumId){
        return consortiumService.getConsortium(consortiumId);
    }

    @GetMapping("/consortiums/name/{consortiumName}")
    public ConsortiumsEntity consortiumsByName (@PathVariable String consortiumName){
        return consortiumService.getConsortium(consortiumName);
    }
}
