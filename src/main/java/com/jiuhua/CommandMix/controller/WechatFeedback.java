package com.jiuhua.CommandMix.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class WechatFeedback {

    @PostMapping("/wxOnLogin")
    @ResponseBody
    public String WxOnLoginFeedback(@RequestBody RequestBody requestBody) {
        String url = "https://api.weixin.qq.com/sns/jscode2session/";
        // 请求客户端
        RestTemplate client = new RestTemplate();
        // 发起请求
        String body = client.getForEntity(url, String.class).getBody();
        System.out.println("******** Get请求 *********");
        assert body != null;
        System.out.println(body);

        String s = "I am testing";
        return s ;
    }

}
