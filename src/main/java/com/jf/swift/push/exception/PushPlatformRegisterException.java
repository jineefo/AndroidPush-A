package com.jf.swift.push.exception;

/**
 * @author jd
 * @version V1.0
 * @Title:
 * @Package com.jf.swift.push.exception
 * @Description:
 * @date 2017/9/7 下午5:38
 */

public class PushPlatformRegisterException extends  Exception{
    /**
     * Constructs a new exception with the specified detail message.  The
     * cause is not initialized, and may subsequently be initialized by
     * a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public PushPlatformRegisterException(String message) {
        super(message);
    }
}
