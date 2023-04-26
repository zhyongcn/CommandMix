package com.jiuhua.CommandMix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.jiuhua.CommandMix.pojo.Constants;
import com.jiuhua.CommandMix.pojo.WXLoginCode;
import com.jiuhua.CommandMix.pojo.WXUploadMessage;

@Controller
public class WechatFeedback {
    WXLoginCode wxLoginCode;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/wxOnLogin")
    @ResponseBody
    public String WxOnLoginFeedback(@RequestBody WXLoginCode wxLoginCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=".concat(Constants.wxMiniAppId)
                .concat("&secret=").concat(Constants.wxSecret)
                .concat("&js_code=").concat(wxLoginCode.getCode())
                .concat("&grant_type=authorization_code");
        // System.out.println(url);
        // 请求客户端
        RestTemplate client = new RestTemplate();
        // 发起请求
        String body = client.getForEntity(url, String.class).getBody();
        System.out.println("******** wechatOnLogin请求 *********");
        assert body != null;
        System.out.println(body);

        return body;
    }

    @PostMapping("/wxuploadmessage")
    @ResponseBody
    public String WxUploadMessage(@RequestBody WXUploadMessage wxUploadMessage) {
        // TODO: insert or update data and return chipid.
        String selectIdSQL = "select id from wxuserinfo where namecard_mac='" + wxUploadMessage.getNamecard_mac()
                + "'";
        String insertSQL = "insert into wxuserinfo (openid, router_ssid, router_bssid, router_passwd, "
                + "namecard_mac, phone_mac) values('"
                + wxUploadMessage.getOpenid() + "','"
                + wxUploadMessage.getRouter_ssid() + "','"
                + wxUploadMessage.getRouter_bssid() + "','"
                + wxUploadMessage.getRouter_passwd() + "','"
                + wxUploadMessage.getPhone_mac() + "','"
                + wxUploadMessage.getNamecard_mac() + "')";
        String updateSQL = "update wxuserinfo set openid='" + wxUploadMessage.getOpenid()
                + "', phone_mac='" + wxUploadMessage.getPhone_mac()
                + "'  where namecard_mac='" + wxUploadMessage.getNamecard_mac() + "'";
        String selectChipidSQL = "select chipid from wxuserinfo where openid= '" + wxUploadMessage.getOpenid() + "'";

        if (jdbcTemplate.queryForList(selectIdSQL)==null) {
            jdbcTemplate.update(insertSQL);
        } else {
            jdbcTemplate.update(updateSQL);
        }

        jdbcTemplate.update(selectChipidSQL);

        return null;
    }

}
