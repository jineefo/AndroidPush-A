package com.jf.swift.push.inter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.jf.swift.push.bean.MessageBean;
import com.jf.swift.push.listener.PushType;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

/**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。<br/>
 * 2、需要将自定义的 MiMessageReceiver 注册在 AndroidManifest.xml 文件中：
 * <pre>
 * {@code
 *  <receiver
 *      android:name="com.xiaomi.mipushdemo.MiMessageReceiver"
 *      android:exported="true">
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.RECEIVE_MESSAGE" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.MESSAGE_ARRIVED" />
 *      </intent-filter>
 *      <intent-filter>
 *          <action android:name="com.xiaomi.mipush.ERROR" />
 *      </intent-filter>
 *  </receiver>
 *  }</pre>
 * 3、MiMessageReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。<br/>
 * 4、MiMessageReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。<br/>
 * 5、MiMessageReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。<br/>
 * 6、MiMessageReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。<br/>
 * 7、MiMessageReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。<br/>
 * 8、以上这些方法运行在非 UI 线程中。
 *
 * @author jf
 */
public class MiMessageReceiver extends PushMessageReceiver {

    private String mRegId;
    private String mTopic;
    private String mAlias;
    private String mAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        }
        if (PushInterfaceImpl.getInstance() != null) {
            MessageBean messageBean = new MessageBean(message.getAlias(), message.getMessageId(), message.getContent());
            if (PushInterfaceImpl.getInstance() != null) {
                PushInterfaceImpl.getInstance().handMessage(messageBean, PushType.CMD);
            }
        }
    }

    /**
     * 通知消息到达客户端后会在通知栏弹出notification，
     * 这时候消息已经传到PushMessageReceiver继承类的onNotificationMessageArrived方法，
     * 但这时候消息还没有通过PushMessageReceiver继承类的的onNotificationMessageClicked方法传到客户端。
     * 当用户需要点击了自定义通知消息，消息会通过onNotificationMessageClicked方法传到客户端。
     *
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        //
        if (message != null && message.getExtra() != null) {
            Map map = message.getExtra();
            if (map.containsKey("pushMsg")) {
                String content = (String) map.get("pushMsg");
                if (null!=content) {
                    try {
                        MessageBean messageBean = new MessageBean("", "", URLDecoder.decode(content, "utf-8"));
                        if (PushInterfaceImpl.getInstance() != null) {
                            PushInterfaceImpl.getInstance().handMessage(messageBean, PushType.CMD);
                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {

    }

    /**
     * 用来接收客户端向服务器发送命令消息后返回的响应
     * ，onReceiveRegisterResult用来接受客户端向服务器发送注册命令消息后返回的响应。
     *
     * @param context
     * @param message
     */
    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {
        //TODO
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
        Log.i("test", mRegId + "=onCommandResult");
    }

    /**
     * 用来接受客户端向服务器发送注册命令消息后返回的响应
     *
     * @param context
     * @param message
     */
    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        String command = message.getCommand();

        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        PushInterfaceImpl.setCurrentPlat(null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                if (PushInterfaceImpl.getInstance() != null) {
                    PushInterfaceImpl.getInstance().handOnline(true);
                }
                if (PushInterfaceImpl.getInstance() != null) {
                    PushInterfaceImpl.getInstance().handPushToken(mRegId, PlatStatus.MI);
                }
            } else {
                if (PushInterfaceImpl.getInstance() != null) {
                    PushInterfaceImpl.getInstance().handOnline(false);
                }
            }
        }
    }
}
