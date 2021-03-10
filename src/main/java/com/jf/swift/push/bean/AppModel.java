package com.jf.swift.push.bean;

import com.jf.swift.push.inter.impl.PlatStatus;

/**
 * 判断当前设备需要注册的平台
 * Created by jf on 2017/9/5.
 */

public abstract class AppModel {
 public abstract boolean isCurrentPlatform(PlatStatus platStatus);
}
