package com.jf.swift.push.log;

import java.util.LinkedHashMap;

/**
 * @author jf
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2018/12/13 下午3:00
 */

public class PushBean extends APPPushLogBean {
    public PushBean.TokenData data=new PushBean.TokenData();
    public static class TokenData{
        public LinkedHashMap<String,String> info=new LinkedHashMap<>();
        public LinkedHashMap<String, String> getInfo() {
            return info;
        }
    }
}
