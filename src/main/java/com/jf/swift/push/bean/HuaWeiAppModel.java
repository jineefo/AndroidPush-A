package com.jf.swift.push.bean;

import com.jf.swift.push.inter.impl.PlatStatus;

/**
 * @author jf
 * @version V1.0
 * @Title:
 * @Package com.jf.swift.push.bean
 * @Description: 华为实体model
 * @date 2017/9/7 下午5:28
 */

public class HuaWeiAppModel extends AppModel{
    private String appId;
    public HuaWeiAppModel(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    @Override
    public boolean isCurrentPlatform(PlatStatus platStatus) {
        if(platStatus==PlatStatus.HUAWEI) {
            return true;
        }else{
            return false;
        }
    }
}
