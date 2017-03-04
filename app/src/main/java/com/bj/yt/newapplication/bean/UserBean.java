package com.bj.yt.newapplication.bean;

/**
 * Created by wxixis on 2017/3/2.
 */

public class UserBean {
    /**
     * {"id":"0",
     * "list":[
     * {"id":7,
     * "submitTime":"2017-03-02T03:18:23.152+0000",
     * "sendTime":null,
     * "submitUserId":5,
     * "acceptUserId":0,
     * "context":"15"},
     * {"id":8,"submitTime":"2017-03-02T03:18:23.152+0000",
     * "sendTime":null,
     * "submitUserId":55,
     * "acceptUserId":0,"context":"4654"},
     * {"id":5,"submitTime":"2017-03-02T03:18:23.152+0000",
     * "sendTime":null,
     * "submitUserId":5,
     * "acceptUserId":0,
     * "context":"c"}
     * ]
     * }
     */
    private int id;
    private String submitTime;
    private String sendTime;
    private int submitUserId;
    private int acceptUserId;
    private String context;

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

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
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
