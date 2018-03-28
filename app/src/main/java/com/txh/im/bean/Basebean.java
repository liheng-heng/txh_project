package com.txh.im.bean;

/**
 * Created by Administrator on 2016/1/21.
 */
public class Basebean<T> {

    public String status;

    public T data;

    public T obj;

    public String msg;

    public String smsg;

    public String pageSize;

    public String PageCount;

    public String TotalCount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
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

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getPageCount() {
        return PageCount;
    }

    public void setPageCount(String pageCount) {
        PageCount = pageCount;
    }

    public String getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(String totalCount) {
        TotalCount = totalCount;
    }

    @Override
    public String toString() {
        return "Basebean{" +
                "status='" + status + '\'' +
                ", data=" + data +
                ", obj=" + obj +
                ", msg='" + msg + '\'' +
                ", smsg='" + smsg + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", PageCount='" + PageCount + '\'' +
                ", TotalCount='" + TotalCount + '\'' +
                '}';
    }
}
