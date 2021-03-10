package com.jf.swift.push.inter.impl;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.jf.swift.push.bean.MessageBean;
import com.jf.swift.push.listener.PushType;
import com.jf.swift.push.util.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class GetuiIntentService extends GTIntentService {
    public GetuiIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        if(msg!=null) {
            byte[] payload = msg.getPayload();
            String taskid = msg.getTaskId();
            String messageid = msg.getMessageId();
            MessageBean messageBean=new MessageBean(taskid,messageid,new String(payload));
            if (com.jf.swift.push.inter.impl.PushInterfaceImpl.getInstance() != null) {
                com.jf.swift.push.inter.impl.PushInterfaceImpl.getInstance().handMessage(messageBean, PushType.CMD);
            }
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        if (Utils.isString(clientid)) {
            try {
                clientid = URLEncoder.encode(clientid, "utf-8");
            } catch (UnsupportedEncodingException e) {
            }
            if(PushInterfaceImpl.getCurrentPlat()!=null&& PushInterfaceImpl.getCurrentPlat()==2) {
                if (com.jf.swift.push.inter.impl.PushInterfaceImpl.getInstance() != null) {
                    com.jf.swift.push.inter.impl.PushInterfaceImpl.getInstance().handPushToken(clientid, com.jf.swift.push.inter.impl.PlatStatus.GETTUI);
                }
            }
            }
            com.jf.swift.push.inter.impl.PushInterfaceImpl.setCurrentPlat(null);
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        if(PushInterfaceImpl.getInstance()!=null){
            PushInterfaceImpl.getInstance().handOnline(online);
        }
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage msg) {
        //TODO
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage gtNotificationMessage) {

    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage gtNotificationMessage) {

    }
}