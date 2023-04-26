package com.jiuhua.CommandMix.pojo;

public class WXUploadMessage {
    private String openid;
    private String router_ssid;
    private String router_bssid;
    private String router_passwd;
    private String namecard_mac;
    private String phone_mac;


    public String getOpenid() {
        return openid;
    }
    public void setOpenid(String openid) {
        this.openid = openid;
    }
    public String getRouter_ssid() {
        return router_ssid;
    }
    public void setRouter_ssid(String router_ssid) {
        this.router_ssid = router_ssid;
    } 
    public String getRouter_passwd() {
        return router_passwd;
    }
    public void setRouter_passwd(String router_passwd) {
        this.router_passwd = router_passwd;
    }
    public String getNamecard_mac() {
        return namecard_mac;
    }
    public void setNamecard_mac(String namecard_mac) {
        this.namecard_mac = namecard_mac;
    }
    public String getPhone_mac() {
        return phone_mac;
    }
    public void setPhone_mac(String phone_mac) {
        this.phone_mac = phone_mac;
    }
    public String getRouter_bssid() {
        return router_bssid;
    }
    public void setRouter_bssid(String router_bssid) {
        this.router_bssid = router_bssid;
    }


    
}
