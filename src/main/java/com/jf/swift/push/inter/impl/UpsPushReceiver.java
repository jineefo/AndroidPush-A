package com.jf.swift.push.inter.impl;

import android.content.Context;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;

/**
 * @author jf
 * @version V1.0
 * @Title:
 * @Package com.jf.swift.push.inter.impl
 * @Description:
 * @date 2019/1/30 上午11:05
 */

public class UpsPushReceiver extends MzPushMessageReceiver {
    /**
     * @param context
     * @param s
     * @deprecated
     */
    @Override
    public void onRegister(Context context, String s) {

    }

    /**
     * @param context
     * @param b
     * @deprecated
     */
    @Override
    public void onUnRegister(Context context, boolean b) {

    }

    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
    }

    @Override
    public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
        PushInterfaceImpl.setCurrentPlat(null);
        if (PushInterfaceImpl.getInstance() != null) {
            PushInterfaceImpl.getInstance().handPushToken(registerStatus.getPushId(), PlatStatus.FLYME);
        }
    }

    @Override
    public void onNotificationClicked(Context var1, MzPushMessage var2) {
    }

    @Override
    public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {

    }

    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    @Override
    public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {

    }
}
