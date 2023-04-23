package com.jiuhua.CommandMix.pojo;

public interface Constants {
    String wxMiniAppId = "wxe1b41bcfa8dbf488";
    String wxSecret = "c7d5638233a5bef0d9bd0bcf0b124441";


    String baseDownloadUrl = "http://jiuhua-hvac.top/upgrade/"; 

    // mqtt 的相关参数
    String MQTTHOST = "tcp://175.24.33.56:1883";
    String SUB_TOPIC = "86518/#";//TODO：将来按照城市来布置服务器
    String LASTWILL_TOPIC = "86518/lastwill";
    String clientid = "commandtransmiter & TDengineWriter";
    String client_userName = "userName";
    String client_passWord = "password";


    int roomState_OFF = 0;
    int roomState_MANUAL = 1;
    int roomState_AUTO = 2;
    int roomState_DEHUMIDITY = 3;
    int roomState_FEAST = 4;
    int roomState_SLEEP = 5;
    int roomState_OUTSIDE = 6;

    int fanSpeed_STOP = 0;
    int fanSpeed_LOW = 1;
    int fanSpeed_MEDIUM = 2;
    int fanSpeed_HIGH = 3;
    int fanSpeed_AUTO = 4;

    int deviceType_fancoil = 0;
    int deviceType_floorwatershed = 1;
    int deviceType_radiator = 2;
    int deviceType_boiler = 3;
    int deviceType_heatpump = 4;
    int deviceType_DHTsensor = 5;
    int deviceType_NTCsensor = 6;
    int deviceType_phone = 7;
    int deviceType_sendperiod = 8;
    int deviceType_mqttconfig = 9;
    int deviceType_toFancoil = 10;
    int deviceType_RequestTimeStamp = 101;
    int deviceType_ResponseTimeStamp = 102;

    int Sunday = 0;
    int Monday = 1;
    int Tuesday = 2;
    int Wednesday = 3;
    int Thursday = 4;
    int Friday = 5;
    int Saturday = 6;


}
