package com.jiuhua.CommandMix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.jiuhua.CommandMix.pojo.Constants;
import com.jiuhua.CommandMix.pojo.JsCode;

@Controller
public class WechatFeedback {
    JsCode jsCode;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostMapping("/wxOnLogin")
    @ResponseBody
    public String WxOnLoginFeedback(@RequestBody JsCode jsCode) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="
                .concat(Constants.wxMiniAppId).concat("&secret=")
                .concat(Constants.wxSecret).concat("&js_code=")
                .concat(jsCode.getCode()).concat("&grant_type=authorization_code");
        // System.out.println(url);
        // 请求客户端
        RestTemplate client = new RestTemplate();
        // 发起请求
        String body = client.getForEntity(url, String.class).getBody();
        System.out.println("******** Get请求 *********");
        assert body != null;
        System.out.println(body);

        return body;
    }

    @PostMapping("/wxUploadMessage")
    @ResponseBody
    public String WxUploadMessage(@RequestBody ) {
        // TODO: insert or update data and return chipid.

        return null;
    }

}
