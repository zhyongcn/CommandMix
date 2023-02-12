package com.jiuhua.CommandMix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CommandMixController {

    @GetMapping("/test.well-known/acme-challenge/WOf00WZBkCyHK1qLqelwJi48KexGwsEG_2Tximb0Hyg")
    @ResponseBody
    public String CertbotCheck() {
        return "WOf00WZBkCyHK1qLqelwJi48KexGwsEG_2Tximb0Hyg.WjO8IvQjwr-UBvT9T7E8Je2TfRifgOSbrj2BxI2odPA";
    }

    
}
