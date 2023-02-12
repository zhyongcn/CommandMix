package com.jiuhua.CommandMix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

import com.jiuhua.CommandMix.service.MyMqttClient;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class CommandMixApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommandMixApplication.class, args);

        //TODO 下面两句注销之后也可以执行，不再重复读表了，但好像仍然不是全部的表。
        //TODO 有121张表，才读取了20多张
        //同一张表读取了30次  20230120
        MyMqttClient mqttClient = new MyMqttClient();
        mqttClient.start();
	}

}
