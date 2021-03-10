package com.jf.swift.push.inter.impl;

import android.content.Context;

import com.vivo.push.model.UPSNotificationMessage;
import com.vivo.push.sdk.OpenClientPushMessageReceiver;

public class PushMessageReceiverImpl extends OpenClientPushMessageReceiver {
    /**
     * TAG to Log
     */
    public static final String TAG = PushMessageReceiverImpl.class.getSimpleName();

    @Override
    public void onNotificationMessageClicked(Context context, UPSNotificationMessage msg) {
    }

    @Override
    public void onReceiveRegId(Context context, String regId) {
        if(PushInterfaceImpl.getInstance()!=null) {
            if(regId!=null) {
                if(!PushInterfaceImpl.isUnregisterForVivo()) {
                    PushInterfaceImpl.getInstance().handPushToken(regId, PlatStatus.VIVO);
                }
            }
        }
        if(PushInterfaceImpl.isUnregisterForVivo()){
            PushInterfaceImpl.unreigsterForVivo(context);
        }
    }
}
