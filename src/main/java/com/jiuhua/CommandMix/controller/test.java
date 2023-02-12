package com.jiuhua.CommandMix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuhua.CommandMix.service.MyMqttClient;

@Controller
public class test {

    @Autowired
    private MyMqttClient myMqttClient = null;

    @GetMapping("/test")
    @ResponseBody
    public String TestOk() {

        if (myMqttClient.getClient() == null) {
            myMqttClient.start();
            System.out.println("myMqttClient.start()，重新连接.......");
        }
        myMqttClient.publish();

        return "test--OK!";
    }
}
