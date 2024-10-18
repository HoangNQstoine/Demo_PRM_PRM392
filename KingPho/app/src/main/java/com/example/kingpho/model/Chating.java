package com.example.kingpho.model;

public class Chating {
    private String content;
    private String time;
    private boolean isSend;

    public Chating(String content, String time, boolean isSend) {
        this.content = content;
        this.time = time;
        this.isSend = isSend;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSend() {
        return isSend;
    }

    public void setSend(boolean send) {
        isSend = send;
    }
}

