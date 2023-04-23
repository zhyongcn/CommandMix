package com.jiuhua.CommandMix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Esp8266Feedback {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/esp8266uploadMessage")
    @ResponseBody
    public String Esp8266uploadMessage() {
        //TODO: insert or update data and return openid.

        return null;
    }
}
