package com.jf.swift.push.listener;

import com.jf.swift.push.bean.MessageBean;
import com.jf.swift.push.inter.impl.PlatStatus;

/**
 * Created by jf on 2017/8/31.
 */

public interface IPushListener {
    /**
     * 获取push返回的数据
     * @param data
     * @param pushType  代表推送消息的类型，
     *                  PushType：1. 为CMD时，消息为透传消息，消息并未展示在通知栏
     *                            2. 为NOTIFIY时，消息已经显示在通知栏了，业务端不用在通知栏展示消息了
     */
    public void onPushMessageReceive(MessageBean data,PushType pushType);


    /**
     * 返回推送是否在线，在线才能收到推送，否则收不到
     * 如果无其他需求可以不处理
     *
     * @param online
     */
    public void isOnline(boolean online);

    /**
     * 返回推送是开启还是关闭，当业务端设置推送开启/关闭成功时会调用
     * 设置推送的开启/关闭参考{@PushInterface#switchPush(boolean)}
     *
     * @param toggle 开启或者关闭push的回调
     */
    public void onPushToggle(boolean toggle);

    /**
     *
     * @param token 注册到服务端的token
     * @param status  注册到服务端的 platStatus
     * @param success true 注册push成功，false 注册失败
     *                如果返回失败，业务端需要考虑重新登录push。
     */
    public void onPushRegister(String token,PlatStatus status,boolean success);

    /**
     * 将注册的push token 和push 渠道返回给业务端
     * @param token
     * @param status
     */
    public void onPushTokenReceive(String token,PlatStatus status);


}
