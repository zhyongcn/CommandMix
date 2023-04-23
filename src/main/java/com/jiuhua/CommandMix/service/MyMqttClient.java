package com.jiuhua.CommandMix.service;

import com.jiuhua.CommandMix.pojo.CommandFromPhone;
import com.jiuhua.CommandMix.pojo.Constants;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

//note: this file is in order to publish mqtt message.
//the command form phone need to be transport.
@Service
public class MyMqttClient { 

    public static final String HOST = Constants.MQTTHOST;
    public static final String SUB_TOPIC = Constants.SUB_TOPIC;
    public static final String LASTWILL_TOPIC = Constants.LASTWILL_TOPIC;
    private static final String clientid = Constants.clientid;
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = Constants.client_userName;
    private String passWord = Constants.client_passWord;
    private ScheduledExecutorService scheduler;

    // 重新链接
    public void startReconnect() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (!client.isConnected()) {
                    try {
                        client.connect(options);
                    } catch (MqttSecurityException e) {
                        e.printStackTrace();
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0 * 1000, 10 * 1000, TimeUnit.MILLISECONDS);

    }

    public void start() {
        try {
            // host为主机名，test为 clientid 即连接MQTT的客户端ID，一般以客户端唯一标识符表示，
            // MemoryPersistence设置clientid的保存形式，默认为以内存保存
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的连接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
            // 这里设置为true表示每次连接到服务器都以新的身份连接
            options.setCleanSession(true);
            // 设置连接的用户名
            options.setUserName(userName);
            // 设置连接的密码 //密码需要toCharArray()
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(60);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，
            // 但这个方法并没有重连的机制
            options.setKeepAliveInterval(300);
            // 设置回调
            client.setCallback(new MqttReceriveCallback());
            // MqttTopic 不是普通的String，需要过一下
            MqttTopic lastwillTopic = client.getTopic(LASTWILL_TOPIC);

            // setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
            // 消息或者负载的字符串需要转换为bytes
            options.setWill(lastwillTopic, "86518#command_transmiter-close".getBytes(), 0, false);

            client.connect(options);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subsribe() throws MqttException {
        // 订阅消息：本程序目前只publish发布 不订阅(subscribe)
        int[] Qos = { 1 };
        // 设置订阅的topic，这里是一个数组。
        String[] subTopics = { SUB_TOPIC };
        client.subscribe(subTopics, Qos);
    }

    public void publish(CommandFromPhone commandFromPhone) {
        try {
            client.publish(commandFromPhone.getTopic(),
                    commandFromPhone.getMessage().getBytes(StandardCharsets.UTF_8),
                    commandFromPhone.getQos(),
                    commandFromPhone.isRetained());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish() {
        try {
            client.publish("topicTest!",
                    "I am test message".getBytes(StandardCharsets.UTF_8),
                    1,
                    false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            client.disconnect();
            startReconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public MqttClient getClient() {
        return client;
    }

}
