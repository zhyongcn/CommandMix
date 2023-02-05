package com.jiuhua.CommandMix.pojo;

public class TimeStampForMqtt {
    private int roomId;
    private int deviceType;
    private String deviceId;
    private long timestamp;

    public TimeStampForMqtt(int roomId, int deviceType, String deviceId, long timestamp) {
        this.roomId = roomId;
        this.deviceType = deviceType;
        this.deviceId = deviceId;
        this.timestamp = timestamp;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(int deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }



}
