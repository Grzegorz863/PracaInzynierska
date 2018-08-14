package pl.tcps.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.tcps.services.ConsortiumService;

@RestController
public class WitamController {

    ConsortiumService consortiumService;

    @Autowired
    public WitamController(ConsortiumService consortiumService) {
        this.consortiumService = consortiumService;
    }

    @GetMapping("/consortiums/{consortiumId}")
    public String costam (@PathVariable Long consortiumId){

        return consortiumService.getConsortium(consortiumId);
    }
}
