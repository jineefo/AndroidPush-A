package com.jf.swift.push;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.jf.swift.push.bean.MessageBean;
import com.jf.swift.push.inter.impl.PushInterfaceImpl;
import com.jf.swift.push.listener.PushType;

import java.net.URLDecoder;

public class BaseOppoPushActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        if (intent != null)
            try {
                String message=intent.getStringExtra(PushReceiver.BOUND_KEY.pushMsgKey);
                if (message!=null) {
                    message = URLDecoder.decode(message, "utf-8");
                    MessageBean messageBean = new MessageBean("", "", message);
                    if (PushInterfaceImpl.getInstance() != null) {
                        PushInterfaceImpl.getInstance().handMessage(messageBean, PushType.NOTIFIY);
                    }
                }
            } catch (Exception e) {

            }
    }
}
