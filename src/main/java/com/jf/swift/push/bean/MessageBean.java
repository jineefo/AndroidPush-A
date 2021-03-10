package com.jf.swift.push.bean;

/**
 * Created by jf on 2017/8/31.
 */

public class MessageBean {
    private String taskId;
    private String messageId;
    private String payload;

    public MessageBean(String taskId, String messageId, String payload) {
        this.taskId = taskId;
        this.messageId = messageId;
        this.payload = payload;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "taskId='" + taskId + '\'' +
                ", messageId='" + messageId + '\'' +
                ", payload='" + payload + '\'' +
                '}';
    }
}
