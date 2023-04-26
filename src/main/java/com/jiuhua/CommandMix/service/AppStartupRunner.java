package com.jiuhua.CommandMix.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Order(1)
@Component
public class AppStartupRunner implements ApplicationRunner {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public void run(ApplicationArguments args) throws Exception {
        // 1、建表 DDL (data definition language)
        String createUserSQL = "create table if not exists wxuserinfo (" +
                "id integer primary key autoincrement," +
                "openid         text," +
                "chipid         text," +
                "unionid        text," +
                "router_ssid    text," +
                "router_bssid   text," +
                "router_passwd  text," +
                "phone_mac      text," +
                "namecard_mac   text" +
                ")";

        jdbcTemplate.update(createUserSQL);

        System.out.println("AppStartupRunner has create the table");

    }

}
