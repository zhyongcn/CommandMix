package com.jiuhua.CommandMix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class test {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/test")
    @ResponseBody
    public String TestOk() {
        
        // 1、建表 DDL (data definition language)
        // String createUserSQL = "create table if not exists wxuserinfo (" +
        // "id integer primary key autoincrement," +
        // "openid          text," +
        // "chipid          text," +       
        // "unionid         text," +
        // "router_ssid     text," +       
        // "router_bssid    text," +       
        // "router_passwd   text," +       
        // "phone_mac       text," +       
        // "namecard_mac    text" +       
        // ")";

        String insertSQL = "insert into wxuserinfo (openid, router_ssid, router_bssid, router_passwd, "
                             +"namecard_mac, phone_mac) values('987654321','qwertyuuof','sdfasdjgsdgfk',"
                             +"'dsalkfgjhgklfhflk','fglkgfjklshjlk','sdfsdgsdfg')";
        jdbcTemplate.update(insertSQL);:wait(0, 0);

        System.out.println("I am TestOK class");

        return "test--OK!";
    }

    // FIXME: 申请HTTPs的认证，这里要求返回一个文件。
    @GetMapping("/test.well-known/acme-challenge/WOf00WZBkCyHK1qLqelwJi48KexGwsEG_2Tximb0Hyg")
    @ResponseBody
    public String CertbotCheck() {
        return "WOf00WZBkCyHK1qLqelwJi48KexGwsEG_2Tximb0Hyg.WjO8IvQjwr-UBvT9T7E8Je2TfRifgOSbrj2BxI2odPA";
    }
}
