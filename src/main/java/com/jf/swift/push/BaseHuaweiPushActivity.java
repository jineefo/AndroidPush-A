package com.jf.swift.push;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.jf.swift.push.bean.MessageBean;
import com.jf.swift.push.inter.impl.PushInterfaceImpl;
import com.jf.swift.push.listener.PushType;

public class BaseHuaweiPushActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        if (intent != null)
            try {
                Uri uri = intent.getData();
                String message = null;
                if (uri != null) {
                    message = uri.getQueryParameter(PushReceiver.BOUND_KEY.pushMsgKey);
                }
                if (message!=null) {
                    MessageBean messageBean = new MessageBean("", "", message);
                    if (PushInterfaceImpl.getInstance() != null) {
                        PushInterfaceImpl.getInstance().handMessage(messageBean, PushType.NOTIFIY);
                    }
                }
            } catch (Exception e) {

            }
    }
}
