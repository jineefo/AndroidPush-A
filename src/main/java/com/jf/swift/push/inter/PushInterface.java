package com.jf.swift.push.inter;

import android.app.Application;
import android.content.Context;

import com.jf.swift.push.bean.AppModel;
import com.jf.swift.push.bean.GetuiAppModel;
import com.jf.swift.push.exception.PushPlatformRegisterException;
import com.jf.swift.push.inter.impl.PlatStatus;
import com.jf.swift.push.listener.IPushListener;
import com.jf.swift.push.util.Constants;
import com.jf.swift.push.util.SpUtils;

import static com.jf.swift.push.util.Constants.EXCEPTION2;

/**
 * Created by jf on 2017/8/31.
 */

public abstract class PushInterface {
    public static boolean  init(Context context) {
        SpUtils.init(context);
        return true;
    }
    /**
     * @param context
     * @param getuiAppModel 个推appmoedl必须传的，如果不传需要在后面appModel里面加上个推的model否则抛出异常
     * @param appModel      除个推平台，其他平台的pushmodel参数
     * @throws PushPlatformRegisterException
     */
    public abstract void loginPushPlatform(Context context, GetuiAppModel getuiAppModel, AppModel... appModel) throws PushPlatformRegisterException;

    /**
     * 退出push, 如果为true：1.本地的push长链接断开，2.通知服务端退出token。
     */
    public abstract void logoutPushPlatForm(Context context, AppModel appModel);

    /**
     * 设置push监听
     *
     * @param iPushListener
     */
    public abstract void setPushListener(IPushListener iPushListener);

    /**
     * 获取pushToken
     *
     * @return
     */
    public abstract String getPushToken();


}