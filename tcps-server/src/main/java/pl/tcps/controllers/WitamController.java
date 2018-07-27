package pl.tcps;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WitamController {

    @GetMapping("/")
    @RequestMapping("/")
    public String costam (){
        return "teścik";
    }
}
