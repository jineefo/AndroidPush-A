package com.jf.swift.push.log;

/**
 * @author jd
 * @version V1.0
 * @Title:
 * @Package com.jf.swift.emchat.utils
 * @Description:
 * @date 2018/9/13 上午10:25
 */

public abstract class APPPushLogBean {
    public String clientId;

    protected String os="android";
    public  String osVersion="";
    protected String userId="";
    protected String uuid="";
    protected String device;
    protected String time;
    protected String buss="push";
    protected String appVersion;
    protected String ip;
    protected String pushevent;
    public String getBuss() {
        return buss;
    }

    public void setBuss(String buss) {
        this.buss = buss;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPushevent() {
        return pushevent;
    }

    public void setPushevent(String pushevent) {
        this.pushevent = pushevent;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }



    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }
}

