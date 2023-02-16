package com.jiuhua.CommandMix.service;


import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public class UselessMqttCallback implements MqttCallback {

    @Override
    public void connectionLost(Throwable cause) {
        // // 连接丢失后，一般在这里面进行重连
        // System.out.println("连接断开，重新连接.......");
        // // 为什么不使用原来的客户端？？
        // // 没有订阅，我断开以后为什么要重连？？
        // MyMqttClient client = new MyMqttClient(); // 是单例吗？
        // client.startReconnect();
        // // 一定成功了吗，不需要判断一下？
        // System.out.println("重连成功..........");

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        // 获取负载，再转为字符串
        String str = new String(message.getPayload());
        System.out.println("接收消息内容 : " + str);

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());

    }
}
