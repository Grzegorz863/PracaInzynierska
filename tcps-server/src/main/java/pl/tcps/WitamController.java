package pl.tcps;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WitamController {

    @RequestMapping("/")
    public String costam (){
        return "teścik";
    }
}
