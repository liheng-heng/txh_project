package com.txh.im.bean;

/**
 * Created by lyl on 2017/4/25.
 */

public class GetAskNotReadCountBean {
    private String status;
    private String msg;
    private String smsg;
    private int obj;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSmsg() {
        return smsg;
    }

    public void setSmsg(String smsg) {
        this.smsg = smsg;
    }

    public int getObj() {
        return obj;
    }

    public void setObj(int obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "GetAskNotReadCountBean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", smsg='" + smsg + '\'' +
                ", obj=" + obj +
                '}';
    }
}
