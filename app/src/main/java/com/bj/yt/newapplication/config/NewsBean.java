package com.bj.yt.newapplication.config;

/**
 * Created by admin on 2017/3/2.
 */

public class NewsBean {

    /**
     * id : 3
     * submitTime : 2017-03-02T09:05:56.331+0000
     * sendTime : null
     * submitUserId : 3
     * acceptUserId : 0
     * context : a
     */

    private int id;
    private String submitTime;
    private Object sendTime;
    private int submitUserId;
    private int acceptUserId;
    private String context;
    private boolean isme;

    public boolean getIsme() {
        return isme;
    }

    public void setIsme(boolean isme) {
        this.isme = isme;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(String submitTime) {
        this.submitTime = submitTime;
    }

    public Object getSendTime() {
        return sendTime;
    }

    public void setSendTime(Object sendTime) {
        this.sendTime = sendTime;
    }

    public int getSubmitUserId() {
        return submitUserId;
    }

    public void setSubmitUserId(int submitUserId) {
        this.submitUserId = submitUserId;
    }

    public int getAcceptUserId() {
        return acceptUserId;
    }

    public void setAcceptUserId(int acceptUserId) {
        this.acceptUserId = acceptUserId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }
}
