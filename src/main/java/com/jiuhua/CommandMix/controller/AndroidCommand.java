package com.jiuhua.CommandMix.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jiuhua.CommandMix.pojo.CommandFromPhone;
import com.jiuhua.CommandMix.service.MyMqttClient;

@Controller
public class AndroidCommand {
    @Autowired
    private MyMqttClient myMqttClient = null;

    // @RequestMapping("/command")
    @PostMapping("/command")
    @ResponseBody
    public CommandFromPhone toMqttBroker(@RequestBody CommandFromPhone commandFromPhone) {
        // 1 接收手机端传来的信息
        // 2 提取、解析信息,为MqttMessage
        // 3 转发出去
        // 4 需要向手机反馈信息吗？ headers？ 模块反馈信息？？

        // 重要：为什么没有这一句会空指针错误，实例还没有加载吗？？
        // 为什么现在还没有加载，最开始的程序不是已经运行了吗？？
        // *** 判断一下原来的客户端在不在？ 如果在，不要在生成了，springboot的bean不是单例吗？？ ***

        if (myMqttClient.getClient() == null) {
            myMqttClient.start();
            System.out.println("myMqttClient.start()，重新连接.......");
        }

        myMqttClient.publish(commandFromPhone);

        System.out.println(commandFromPhone.getTopic());
        System.out.println(commandFromPhone.getQos());
        System.out.println(commandFromPhone.getMessage());
        System.out.println(commandFromPhone.isRetained());

        return commandFromPhone;
    }

}
