package com.jf.swift.push.log;

/**
 * @author jf
 * @version V1.0
 * @Title:
 * @Description:
 * @date 2018/12/13 下午2:34
 */

public class LogMsg {
    private int _id=-1;
    private String content;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LogMsg(int _id, String content) {
        this._id = _id;
        this.content = content;
    }
}
