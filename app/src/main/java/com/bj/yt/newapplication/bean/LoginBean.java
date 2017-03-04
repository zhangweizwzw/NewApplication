package com.bj.yt.newapplication.bean;

import java.util.List;

/**
 * Created by admin on 2017/3/4.
 */

public class LoginBean {

    /**
     * id : 0
     * list : [{"id":7,"submitTime":"2017-03-02T03:18:23.152+0000","sendTime":null,"submitUserId":5,"acceptUserId":0,"context":"15"},{"id":8,"submitTime":"2017-03-02T03:18:23.152+0000","sendTime":null,"submitUserId":55,"acceptUserId":0,"context":"4654"},{"id":5,"submitTime":"2017-03-02T03:18:23.152+0000","sendTime":null,"submitUserId":5,"acceptUserId":0,"context":"c"}]
     */

    private String id;
    private List<NewsBean> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<NewsBean> getList() {
        return list;
    }

    public void setList(List<NewsBean> list) {
        this.list = list;
    }

    public static class ListBean {
        /**
         * id : 7
         * submitTime : 2017-03-02T03:18:23.152+0000
         * sendTime : null
         * submitUserId : 5
         * acceptUserId : 0
         * context : 15
         */

        private int id;
        private String submitTime;
        private Object sendTime;
        private int submitUserId;
        private int acceptUserId;
        private String context;
        private boolean isme;

        public boolean isme() {
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
}
