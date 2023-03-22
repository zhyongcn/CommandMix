package com.jiuhua.CommandMix.service;

public class MqttSub {
    
            //TODO 把订阅单独列出来作为一个bean，使用单独生成的客户端，不同的id，也许可以解决不断重连的问题。
            //TODO 好像真的是断开，不是销毁，那么为什么执行test的网页请求才能开始订阅？ 与懒加载有关？  try 用代码来试验。
            // 订阅消息 TODO：本程序目前只publish发布 不 subscribe订阅
            int[] Qos = { 1 };
            // 设置订阅的topic，TODO：将来按照城市来布置服务器
            String[] subTopics = { SUB_TOPIC };
            client.subscribe(subTopics, Qos);
}
