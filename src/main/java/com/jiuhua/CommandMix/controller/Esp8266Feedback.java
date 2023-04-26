package com.jiuhua.CommandMix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Esp8266Feedback {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/esp8266uploadmessage")
    @ResponseBody
    public String Esp8266uploadMessage(@RequestParam(value = "chipid") String chipid,
            @RequestParam(value = "router_ssid") String router_ssid,
            @RequestParam(value = "router_bssid") String router_bssid,
            @RequestParam(value = "router_passwd") String router_passwd,
            @RequestParam(value = "namecard_mac") String namecard_mac) {

        // insert or update data and return openid.
        String selectIdSQL = "select id from wxuserinfo where namecard_mac='" + namecard_mac + "'";
        String insertSQL = "insert into wxuserinfo (chipid, router_ssid, router_bssid, router_passwd, "
                + "namecard_mac) values('"
                + chipid + "','" + router_ssid + "','" + router_bssid + "','" + router_passwd + "','" + namecard_mac
                + "')";
        String updateSQL = "update wxuserinfo set chipid=" + chipid + " where namecard_mac='" + namecard_mac + "'";
        String selectOpenidSQL = "select openid from wxuserinfo where chipid= '" + chipid + "'";

        // 用户注册次数少，不需要考虑效率，采用多次数据库操作，不用考虑资源。
        if (jdbcTemplate.queryForList(selectIdSQL) == null) {
            jdbcTemplate.update(insertSQL);
        } else {
            jdbcTemplate.update(updateSQL);
        }

        jdbcTemplate.update(selectOpenidSQL);

        return null;// 返回openid

    }
}
