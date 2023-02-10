package com.jiuhua.CommandMix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class test {

    @GetMapping("/test")
    @ResponseBody
    public String TestOk() {

        // if (myMqttClient.getClient() == null) {
        //     myMqttClient.start();
        //     System.out.println("myMqttClient.start()，重新连接.......");
        // }
        // myMqttClient.publish();

        return "test--OK!";
    }
}
