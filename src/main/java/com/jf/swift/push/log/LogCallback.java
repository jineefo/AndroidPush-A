package com.jf.swift.push.log;

/**
 * @author jd
 * @version V1.0
 * @Title:
 * @Package com.jf.swift.emchat.listener
 * @Description:
 * @date 2018/12/13 下午5:26
 */

public interface LogCallback {
    public void onSuccess(LogMsg logMsg);
    public void onFail();
}
