package com.jf.swift.push.inter.impl;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.huawei.hms.support.api.push.PushReceiver;
import com.jf.swift.push.bean.MessageBean;
import com.jf.swift.push.listener.PushType;
import com.jf.swift.push.util.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 应用需要创建一个子类继承com.huawei.hms.support.api.push.PushReceiver，
 * 实现onToken，onPushState ，onPushMsg，onEvent，这几个抽象方法，用来接收token返回，push连接状态，透传消息和通知栏点击事件处理。
 * onToken 调用getToken方法后，获取服务端返回的token结果，返回token以及belongId
 * onPushState 调用getPushState方法后，获取push连接状态的查询结果
 * onPushMsg 推送消息下来时会自动回调onPushMsg方法实现应用透传消息处理。本接口必须被实现。 在开发者网站上发送push消息分为通知和透传消息
 *           通知为直接在通知栏收到通知，通过点击可以打开网页，应用 或者富媒体，不会收到onPushMsg消息
 *           透传消息不会展示在通知栏，应用会收到onPushMsg
 * onEvent 该方法会在设置标签、点击打开通知栏消息、点击通知栏上的按钮之后被调用。由业务决定是否调用该函数。
 */
public class HuaweiPushReceiver extends PushReceiver {

   public static final String TAG = "HuaweiPushRevicer";

   public static final String ACTION_UPDATEUI = "action.updateUI"; 
    @Override
    public void onToken(Context context, String token, Bundle extras) {
        PushInterfaceImpl.setCurrentPlat(null);
        if (Utils.isString(token)) {
            try {
                token = URLEncoder.encode(token, "utf-8");
            } catch (UnsupportedEncodingException e) {
            }
            if(PushInterfaceImpl.getInstance()!=null) {
                if(null!=token) {
                    PushInterfaceImpl.getInstance().handPushToken(token, PlatStatus.HUAWEI);
                }
            }
        }
    }

    @Override
    public boolean onPushMsg(Context context, byte[] msg, Bundle bundle) {
        try {
           //透传消息开发者可以自己解析消息内容，然后做相应的处理
            if(msg!=null&&msg.length>0){
                String content = new String(msg, "UTF-8");
                if(null!=content){
                    MessageBean messageBean = new MessageBean("", "", content);
                    if (PushInterfaceImpl.getInstance() != null) {
                        PushInterfaceImpl.getInstance().handMessage(messageBean,PushType.CMD);
                    }
                }
            }else{
                if(bundle!=null) {
                    String message = bundle.getString(BOUND_KEY.pushMsgKey);
                    if(null!=message){
                        MessageBean messageBean = new MessageBean("", "", message);
                        if (PushInterfaceImpl.getInstance() != null) {
                            PushInterfaceImpl.getInstance().handMessage(messageBean,PushType.CMD);
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void onPushState(Context context, boolean pushState) {
        //push连接状态
        if(PushInterfaceImpl.getInstance()!=null){
            PushInterfaceImpl.getInstance().handOnline(pushState);
        }
    }
}