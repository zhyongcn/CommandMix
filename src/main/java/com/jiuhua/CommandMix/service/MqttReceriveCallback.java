package com.jiuhua.CommandMix.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.jiuhua.CommandMix.pojo.Boiler;
import com.jiuhua.CommandMix.pojo.Constants;
import com.jiuhua.CommandMix.pojo.Fancoil;
import com.jiuhua.CommandMix.pojo.Heatpump;
import com.jiuhua.CommandMix.pojo.Sensor;
import com.jiuhua.CommandMix.pojo.TimeStampForMqtt;
import com.jiuhua.CommandMix.pojo.Watershed;

@Service
public class MqttReceriveCallback implements MqttCallback {

    private final HashSet<String> sensorsSet;
    private final HashSet<String> boilersSet;
    private final HashSet<String> fancoilsSet;
    private final HashSet<String> heatpumpsSet;
    private final HashSet<String> watershedsSet;

    private String timeStampTopic; 
    private TimeStampForMqtt timeStampForMqtt;

    public MqttReceriveCallback() {
        sensorsSet = new HashSet<>();
        boilersSet = new HashSet<>();
        fancoilsSet = new HashSet<>();
        heatpumpsSet = new HashSet<>();
        watershedsSet = new HashSet<>();

        try {
            Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
            // String jdbcUrl =
            // "jdbc:TAOS-RS://jiuhua-hvac.top:6041/homedevice?user=zz&password=700802";
            // String jdbcUrl = "jdbc:TAOS-RS://jiuhua-hvac.top:6041/homedevice?user=zz&password=700802";
            Connection conn = DriverManager.getConnection(Constants.jdbcUrl);
            Statement stmt = conn.createStatement();

            // 提取现有数据表的表名  //TODO:为什么这段代码被执行了好几次，甚至30多次，不同的环境次数不同。
            String sql;
            ResultSet resultSet;
            sql = "select tbname from homedevice.sensors";
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String tablename = resultSet.getString(1);
                System.out.println("表名是： " + tablename + "");
                sensorsSet.add(tablename);
            }

            sql = "select tbname from homedevice.boilers";
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String tablename = resultSet.getString(1);
                System.out.println("表名是： " + tablename + "");
                boilersSet.add(tablename);
            }

            sql = "select tbname from homedevice.fancoils";
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String tablename = resultSet.getString(1);
                System.out.println("表名是： " + tablename + "");
                fancoilsSet.add(tablename);
            }

            sql = "select tbname from homedevice.heatpumps";
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String tablename = resultSet.getString(1);
                System.out.println("表名是： " + tablename + "");
                heatpumpsSet.add(tablename);
            }

            sql = "select tbname from homedevice.watersheds";
            resultSet = stmt.executeQuery(sql);
            while (resultSet.next()) {
                String tablename = resultSet.getString(1);
                System.out.println("表名是： " + tablename + "");
                watershedsSet.add(tablename);
            }

            stmt.close();
            conn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @Override
    public void connectionLost(Throwable cause) {
        // 连接丢失后，一般在这里面进行重连
        System.out.println("连接断开，重新连接.......");
        // 为什么不使用原来的客户端？？
        // 没有订阅，我断开以后为什么要重连？？
        MyMqttClient client = new MyMqttClient(); // 是单例吗？
        client.startReconnect();
        // 一定成功了吗，不需要判断一下？
        System.out.println("重连成功..........");

    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        // 获取负载，再转为字符串
        String str = new String(message.getPayload());
        System.out.println("接收消息内容 : " + str);
        String sql = null;

        if (str.startsWith("{") && str.endsWith("}")) {
            // TODO 还是先解析json的字符串比较好。
            // FIXME 修改为依据devicetype 的INT 值来分别各个方法路径
            JSONObject jsonObject = JSONObject.parseObject(str);
            int deviceType = jsonObject.getIntValue("deviceType");
            switch (deviceType) {
                case Constants.deviceType_RequestTimeStamp:
                    System.out.println("接收到 Request Timestamp 消息");
                    // 字符串深拷贝，防止这个函数结束丢失。
                    timeStampTopic = String.valueOf(topic.toCharArray(), 0, topic.length());
                    timeStampForMqtt = JSONObject.parseObject(str, TimeStampForMqtt.class);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            // 使用原来的mqttclient的方法，暂时没有走通。
                            // MqttTopic mqttTopic = myMqttClient.getTopic(topic);
                            // MqttDeliveryToken token = mqttTopic.publish("message this is a test message
                            // about publish in messagearrived");

                            try { // try()是java9新语法，括号里的资源会自动关闭
                                try (MqttClient timestampClient = new MqttClient("tcp://175.24.33.56:1883",
                                        "MqttTimeStampId")) {
                                    MqttConnectOptions options = new MqttConnectOptions();
                                    // 设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，
                                    // 这里设置为true表示每次连接到服务器都以新的身份连接
                                    options.setCleanSession(true);
                                    // 设置连接的用户名
                                    options.setUserName("userName");
                                    // // 设置连接的密码 //密码需要toCharArray()
                                    options.setPassword("passWord".toCharArray());
                                    // 设置超时时间 单位为秒
                                    options.setConnectionTimeout(60);
                                    // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，
                                    // 但这个方法并没有重连的机制
                                    options.setKeepAliveInterval(300);
                                    // 设置回调
                                    timestampClient.setCallback(new UselessMqttCallback());
                                    // MqttTopic 不是普通的String，需要过一下
                                    MqttTopic lastwillTopic = timestampClient.getTopic("Timestamp_LASTWILL_TOPIC");

                                    // setWill方法，如果项目中需要知道客户端是否掉线可以调用该方法。设置最终端口的通知消息
                                    // 消息或者负载的字符串需要转换为bytes
                                    options.setWill(lastwillTopic, "86518#timestamp-mqtt-client-close".getBytes(), 0,
                                            false);

                                    timestampClient.connect(options);

                                    Thread.sleep(2000);// TODO 需要进化的方法。模块请求之后马上会发布信息，所以延时一秒方便模块接收。
                                    timeStampForMqtt.setDeviceType(Constants.deviceType_ResponseTimeStamp);
                                    timeStampForMqtt.setTimestamp(System.currentTimeMillis()/1000);
                                    String message = JSONObject.toJSONString(timeStampForMqtt);
                                    timestampClient.publish(timeStampTopic, message.getBytes(), 0, false);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    break;
            }

            // deviceType 5 || 6 传感器的消息
            if (str.contains("deviceType\":5,") || str.contains("deviceType\":6,")) {
                // json字符串返回值反序列化为实体类
                Sensor sensor = JSONObject.parseObject(str, Sensor.class);
                String tablename = "sensor" + sensor.getDeviceId();
                if (sensorsSet.contains(tablename)) {
                    if (sensor.getTimestamp() < 1600000000) {
                        sql = "insert into " + tablename
                                + " (ts, currenttemperature, currenthumidity, adjustingtemperature,"
                                + "adjustinghumidity) values(now, " + sensor.getCurrentlyTemperature()
                                + "," + sensor.getCurrentlyHumidity()
                                + "," + sensor.getAdjustingTemperature()
                                + "," + sensor.getAdjustingHumidity()
                                + ");";
                    } else {
                        sql = "insert into " + tablename
                                + " (ts, currenttemperature, currenthumidity, adjustingtemperature,"
                                + "adjustinghumidity) values(" + sensor.getTimestamp()
                                // 这里要加 000 TDengine的时间戳毫秒级的。
                                + "000," + sensor.getCurrentlyTemperature()
                                + "," + sensor.getCurrentlyHumidity()
                                + "," + sensor.getAdjustingTemperature()
                                + "," + sensor.getAdjustingHumidity()
                                + ");";
                    }
                } else {
                    sensor.setLocation(topic);
                    sql = "create table " + tablename + " using homedevice.sensors tags(\""
                            + sensor.getLocation() + "\", "
                            + sensor.getRoomId() + ","
                            + sensor.getDeviceType() + ","
                            + sensor.getDeviceId() + ")";
                    sensorsSet.add(tablename);
                }
            }

            // deviceType 3 锅炉的消息
            if (str.contains("deviceType\":3,")) {
                // json字符串返回值反序列化为实体类
                Boiler boiler = JSONObject.parseObject(str, Boiler.class);
                String tablename = "boiler" + boiler.getDeviceId();
                if (boilersSet.contains(tablename)) {
                    if (boiler.getTimestamp() < 1600000000) {
                        sql = "insert into " + tablename + " (ts, currenttemperature, settingtemperature, boilerstate,"
                                + "roomstate) values(now, " + boiler.getCurrentlyTemperature()
                                + "," + boiler.getSettingTemperature()
                                + "," + boiler.isBoilerstate()
                                + "," + boiler.getRoomState()
                                + ");";
                    } else {
                        sql = "insert into " + tablename + " (ts, currenttemperature, settingtemperature, boilerstate,"
                                + "roomstate) values(" + boiler.getTimestamp()
                                // 这里要加 000 TDengine的时间戳毫秒级的。
                                + "000," + boiler.getCurrentlyTemperature()
                                + "," + boiler.getSettingTemperature()
                                + "," + boiler.isBoilerstate()
                                + "," + boiler.getRoomState()
                                + ");";
                    }
                } else {
                    boiler.setLocation(topic);
                    sql = "create table " + tablename + " using homedevice.boilers tags(\""
                            + boiler.getLocation() + "\", "
                            + boiler.getRoomId() + ","
                            + boiler.getDeviceType() + ","
                            + boiler.getDeviceId() + ")";
                    boilersSet.add(tablename);
                }
            }

            // deviceType 0 风机盘管的消息
            if (str.contains("deviceType\":0,")) {
                // json字符串返回值反序列化为实体类
                Fancoil fancoil = JSONObject.parseObject(str, Fancoil.class);
                String tablename = "fancoil" + fancoil.getDeviceId();
                if (fancoilsSet.contains(tablename)) {
                    if (fancoil.getTimestamp() < 1600000000) {
                        sql = "insert into " + tablename + " (ts, roomstate, settingtemperature, settinghumidity,"
                                + "currenttemperature, currenthumidity, settingfanspeed, currentfanspeed,"
                                + " coilvalve) values(now, " + fancoil.getRoomState()
                                + "," + fancoil.getSettingTemperature()
                                + "," + fancoil.getSettingHumidity()
                                + "," + fancoil.getCurrentlyTemperature()
                                + "," + fancoil.getCurrentlyHumidity()
                                + "," + fancoil.getSettingFanSpeed()
                                + "," + fancoil.getCurrentFanSpeed()
                                + "," + fancoil.isCoilvalve()
                                + ");";
                    } else {
                        sql = "insert into " + tablename + " (ts, roomstate, settingtemperature, settinghumidity,"
                                + " currenttemperature, currenthumidity, settingfanspeed, currentfanspeed,"
                                + " coilvalve) values(" + fancoil.getTimestamp()
                                // 这里要加 000 TDengine的时间戳毫秒级的。
                                + "000," + fancoil.getRoomState()
                                + "," + fancoil.getSettingTemperature()
                                + "," + fancoil.getSettingHumidity()
                                + "," + fancoil.getCurrentlyTemperature()
                                + "," + fancoil.getCurrentlyHumidity()
                                + "," + fancoil.getSettingFanSpeed()
                                + "," + fancoil.getCurrentFanSpeed()
                                + "," + fancoil.isCoilvalve()
                                + ");";
                    }
                } else {
                    fancoil.setLocation(topic);
                    sql = "create table " + tablename + " using homedevice.fancoils tags(\""
                            + fancoil.getLocation() + "\", "
                            + fancoil.getRoomId() + ","
                            + fancoil.getDeviceType() + ","
                            + fancoil.getDeviceId() + ")";
                    fancoilsSet.add(tablename);
                }
            }

            // Heatpump devicetype 4 热泵主机的消息
            if (str.contains("deviceType\":4,")) {
                // json字符串返回值反序列化为实体类
                Heatpump heatpump = JSONObject.parseObject(str, Heatpump.class);
                String tablename = "heatpump" + heatpump.getDeviceId();
                if (heatpumpsSet.contains(tablename)) {
                    if (heatpump.getTimestamp() < 1600000000) {
                        sql = "insert into " + tablename
                                + " (ts, currenttemperature, settingtemperature, currenthumidity,"
                                + " settinghumidity, heatpumpstate, roomstate) values(now, "
                                + heatpump.getCurrentlyTemperature()
                                + "," + heatpump.getSettingTemperature()
                                + "," + heatpump.getCurrentlyHumidity()
                                + "," + heatpump.getSettingHumidity()
                                + "," + heatpump.isHeatpumpstate()
                                + "," + heatpump.getRoomState()
                                + ");";
                    } else {
                        sql = "insert into " + tablename
                                + " (ts, currenttemperature, settingtemperature, currenthumidity,"
                                + " settinghumidity, heatpumpstate, roomstate) values(" + heatpump.getTimestamp()
                                // 这里要加 000 TDengine的时间戳毫秒级的。
                                + "000," + heatpump.getCurrentlyTemperature()
                                + "," + heatpump.getSettingTemperature()
                                + "," + heatpump.getCurrentlyHumidity()
                                + "," + heatpump.getSettingHumidity()
                                + "," + heatpump.isHeatpumpstate()
                                + "," + heatpump.getRoomState()
                                + ");";
                    }
                } else {
                    heatpump.setLocation(topic);
                    sql = "create table " + tablename + " using homedevice.heatpumps tags(\""
                            + heatpump.getLocation() + "\", "
                            + heatpump.getRoomId() + ","
                            + heatpump.getDeviceType() + ","
                            + heatpump.getDeviceId() + ")";
                    heatpumpsSet.add(tablename);
                }
            }

            // Watershed deviceType 1 指地暖的分水器，应该空调的分水器也适用。
            if (str.contains("deviceType\":1,")) {
                // json字符串返回值反序列化为实体类
                Watershed watershed = JSONObject.parseObject(str, Watershed.class);
                String tablename = "watershed" + watershed.getDeviceId();
                if (watershedsSet.contains(tablename)) {
                    if (watershed.getTimestamp() < 1600000000) {
                        sql = "insert into " + tablename + " (ts, roomid, roomstate, floorvalve, coilvalve,"
                                + "currenttemperature, currenthumidity,  settingtemperature, settinghumidity) values(now, "
                                + watershed.getRoomId()
                                + "," + watershed.getRoomState()
                                + "," + watershed.isFloorvalve()
                                + "," + watershed.isCoilvalve()
                                + "," + watershed.getCurrentlyTemperature()
                                + "," + watershed.getCurrentlyHumidity()
                                + "," + watershed.getSettingTemperature()
                                + "," + watershed.getSettingHumidity()
                                + ");";
                    } else {
                        sql = "insert into " + tablename + " (ts, roomid, roomstate, floorvalve, coilvalve,"
                                + "currenttemperature, currenthumidity,  settingtemperature, settinghumidity) values("
                                + watershed.getTimestamp()
                                // 这里要加 000 TDengine的时间戳毫秒级的。
                                + "000," + watershed.getRoomId()
                                + "," + watershed.getRoomState()
                                + "," + watershed.isFloorvalve()
                                + "," + watershed.isCoilvalve()
                                + "," + watershed.getCurrentlyTemperature()
                                + "," + watershed.getCurrentlyHumidity()
                                + "," + watershed.getSettingTemperature()
                                + "," + watershed.getSettingHumidity()
                                + ");";
                    }
                } else {
                    watershed.setLocation(topic);
                    sql = "create table " + tablename + " using homedevice.watersheds tags(\""
                            + watershed.getLocation() + "\","
                            + watershed.getDeviceType() + ","
                            + watershed.getDeviceId() + ")";
                    watershedsSet.add(tablename);
                }
            }

        }


        // Class.forName("com.taosdata.jdbc.TSDBDriver");
        // String jdbcUrl =
        // "jdbc:TAOS://175.24.33.56:6030/homedevice?user=zz&password=700802";
        Class.forName("com.taosdata.jdbc.rs.RestfulDriver");
        // String jdbcUrl =
        // "jdbc:TAOS-RS://175.24.33.56:6041/homedevice?user=zz&password=700802";
        // // String jdbcUrl = "jdbc:TAOS-RS://81.68.136.113:6041/homedevice?user=zz&password=700802";
        Connection conn = DriverManager.getConnection(Constants.jdbcUrl);
        Statement stmt = conn.createStatement();

        if (!(sql == null)) {
            try {// 数据不一定合规矩，try保护一下，偷懒 :) it work!
                 // 执行SQL命令
                stmt.executeUpdate(sql);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            // int affectedRows = stmt.executeUpdate(sql);
            // System.out.println("insert " + affectedRows + " rows");
            System.out.println("access databases");
        }

        stmt.close();
        conn.close();

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());

    }
}
