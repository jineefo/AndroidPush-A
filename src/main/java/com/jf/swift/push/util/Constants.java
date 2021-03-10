package com.jf.swift.push.util;


public final class Constants {
    public static final String EXCEPTION = "至少注册一个个推平台";
    public static final String EXCEPTION1 = "请传值";
    public static final String EXCEPTION2 = "host不能不空";
    public static final String EXCEPTION3 = "context不能不空";
    public static final String EXCEPTION4 = "请先调用init方法";
    public static final int REPEAT_COUNT = 2;

    public final static String VIVO = "vivo";
    public final static String HUAWEI = "huawei";
    public final static String XIAOMI = "xiaomi";
    public final static String MEIZU = "meizu";
    public final static String OPPO = "oppo";
    public final static String GETTUI = "getui";

    private static String host = "";
    private static String baseUrl;
    public static final String REGISTER = "/t/p/register-token.json";
    public static final String LOGOUT = "/t/p/logout-token.json";
    public static final String STATUS = "/t/p/channel-status.json";
    public static final String SWITCH = "/t/p/register-switch.json";

    public static void setMobileMonitorHost(String host){
        Constants.host = host;
    }

    public static void setPushBaseUrl(String baseUrl) {
        Constants.baseUrl = baseUrl;
    }

    public static String getLogBase() {
        return "https://mobile-monitor." + host;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

}
