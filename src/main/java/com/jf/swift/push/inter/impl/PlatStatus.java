package com.jf.swift.push.inter.impl;

/**
 * @author jf
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2017/7/14 下午1:06
 */
public enum PlatStatus {
    GETTUI(2),
    HUAWEI(3),
    MI(4),
    FLYME(6),
    VIVO(7),
    OPPO(5);
    int value=2;
    private PlatStatus(int value) {
        this.value=value;
    }
    public int getValue(){
        return value;
    }
}
