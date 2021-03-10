package com.jf.swift.push;

import com.jf.swift.push.inter.PushInterface;
import com.jf.swift.push.inter.impl.PushInterfaceImpl;

/**
 * @author jd
 * @version V1.0
 * @Title:
 * @Package com.jf.swift.push
 * @Description:
 * @date 2017/9/12 上午10:15
 */

public interface LPPush {
     PushInterface pushInterface= PushInterfaceImpl.getInstance();
}
