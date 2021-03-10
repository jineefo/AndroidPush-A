package com.jf.swift.push.bean;

import com.jf.swift.push.inter.impl.PlatStatus;

/**
 * @author jf
 * @version V1.0
 * @Title:
 * @Package com.jf.swift.push.bean
 * @Description: 小米实体model
 * @date 2017/9/7 下午5:28
 */

public class MiAppModel extends AppModel{
    private String appId;
    private String appKey;

    public MiAppModel(String appId, String appKey) {
        this.appId = appId;
        this.appKey = appKey;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }
    @Override
    public boolean isCurrentPlatform(PlatStatus platStatus) {
        if(platStatus==PlatStatus.MI) {
            return true;
        }else{
            return false;
        }
    }
}
