package com.jf.swift.push.bean;

import com.jf.swift.push.inter.impl.PlatStatus;

/**
 * @author jf
 * @version V1.0
 * @Title:
 * @Package com.jf.swift.push.bean
 * @Description: 个推实体Model
 * @date 2017/9/7 下午5:28
 */

public class GetuiAppModel extends AppModel{
    private String appId;
    private String appKey;
    private String appSercet;
    public GetuiAppModel(String appId, String appKey,String appSercet) {
        this.appId = appId;
        this.appKey = appKey;
        this.appSercet=appSercet;
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

    public String getAppSercet() {
        return appSercet;
    }

    public void setAppSercet(String appSercet) {
        this.appSercet = appSercet;
    }

    @Override
    public boolean isCurrentPlatform(PlatStatus platStatus) {
        if(platStatus==PlatStatus.GETTUI) {
            return true;
        }else{
            return false;
        }
    }
}
