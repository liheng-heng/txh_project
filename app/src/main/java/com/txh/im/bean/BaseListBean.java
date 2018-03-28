package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/2/13.
 */

public class BaseListBean<T> {

    /**
     * status : 200
     * msg : 请求成功
     * smsg : null
     * pageSize : 0
     * obj : [{"AuditStatusName":"已发送","PushByName":"测试璇","LocationName":"玄武区,秦淮区","LocationId":"959,960","IsCurrentUser":false,"Id":49,"Title":"222","NoticeContent":"的说法撒旦法","PushStatus":1,"PushBy":36034,"PushTime":"2017-02-09T13:36:32.3","IsDelete":false,"CreateBy":36034,"CreateTime":"2017-02-09T13:36:08.613","LastUpdateBy":null,"LastUpdateTime":"2017-02-09T13:36:32.613"},{"AuditStatusName":"已发送","PushByName":"www","LocationName":"江川路街道,古美街道","LocationId":"156,157","IsCurrentUser":false,"Id":48,"Title":"22","NoticeContent":"阿娇开发","PushStatus":1,"PushBy":32861,"PushTime":"2017-02-09T13:54:50.99","IsDelete":false,"CreateBy":32861,"CreateTime":"2017-02-09T10:27:24.257","LastUpdateBy":null,"LastUpdateTime":"2017-02-09T13:54:51.007"},{"AuditStatusName":"已发送","PushByName":"www","LocationName":"闵行区","LocationId":"20","IsCurrentUser":false,"Id":29,"Title":"4","NoticeContent":"德国汉堡就那么\u2026\u2026","PushStatus":1,"PushBy":32861,"PushTime":"2017-02-09T14:30:02.113","IsDelete":false,"CreateBy":32861,"CreateTime":"2017-02-09T09:45:52.977","LastUpdateBy":null,"LastUpdateTime":"2017-02-09T14:30:02.13"},{"AuditStatusName":"已发送","PushByName":"www","LocationName":"江苏省","LocationId":"957","IsCurrentUser":false,"Id":22,"Title":"阿发射点发","NoticeContent":"阿斯蒂芬撒旦法","PushStatus":1,"PushBy":32861,"PushTime":"2017-02-08T16:18:50.283","IsDelete":false,"CreateBy":32861,"CreateTime":"2017-02-08T16:06:20.21","LastUpdateBy":null,"LastUpdateTime":"2017-02-08T16:18:50.317"},{"AuditStatusName":"已发送","PushByName":"www","LocationName":"全国","LocationId":"1","IsCurrentUser":false,"Id":20,"Title":"141414","NoticeContent":"1414141","PushStatus":1,"PushBy":32861,"PushTime":"2017-02-08T16:44:10.66","IsDelete":false,"CreateBy":32861,"CreateTime":"2017-02-08T16:05:41.047","LastUpdateBy":null,"LastUpdateTime":"2017-02-08T16:44:10.687"},{"AuditStatusName":"已发送","PushByName":"www","LocationName":"上海市,浙江省,江苏省","LocationId":"2,3,957","IsCurrentUser":false,"Id":19,"Title":"7788","NoticeContent":"大撒旦撒发生分","PushStatus":1,"PushBy":32861,"PushTime":"2017-02-08T16:45:00.297","IsDelete":false,"CreateBy":32861,"CreateTime":"2017-02-08T13:45:45.777","LastUpdateBy":null,"LastUpdateTime":"2017-02-08T16:45:00.32"},{"AuditStatusName":"已发送","PushByName":"www","LocationName":"上海市,浙江省","LocationId":"2,3","IsCurrentUser":false,"Id":8,"Title":"测试--5","NoticeContent":"佛度有缘人--4","PushStatus":1,"PushBy":32861,"PushTime":"2017-02-09T10:23:44.733","IsDelete":false,"CreateBy":32861,"CreateTime":"2017-02-07T11:54:39.397","LastUpdateBy":null,"LastUpdateTime":"2017-02-09T10:23:44.783"},{"AuditStatusName":"已发送","PushByName":"www","LocationName":"江川路街道","LocationId":"156","IsCurrentUser":false,"Id":5,"Title":"测试2","NoticeContent":"佛度有缘人1","PushStatus":1,"PushBy":32861,"PushTime":"2017-02-09T15:04:00.94","IsDelete":false,"CreateBy":32861,"CreateTime":"2017-01-16T14:05:56.2","LastUpdateBy":null,"LastUpdateTime":"2017-02-09T15:04:00.957"},{"AuditStatusName":"已发送","PushByName":"www","LocationName":"江川路街道","LocationId":"156","IsCurrentUser":false,"Id":4,"Title":"测试1","NoticeContent":"日夜念想，必有回应。","PushStatus":1,"PushBy":32861,"PushTime":"2017-02-08T16:27:25.883","IsDelete":false,"CreateBy":32861,"CreateTime":"2017-01-16T11:59:28.83","LastUpdateBy":null,"LastUpdateTime":"2017-02-08T16:27:25.903"}]
     * PageCount : 0
     * TotalCount : 0
     */

    private String status;
    private String msg;
    private String smsg;
    private String pageSize;
    private String PageCount;
    private String TotalCount;
    private List<T> obj;

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

    public List<T> getObj() {
        return obj;
    }

    public void setObj(List<T> obj) {
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "BaseListBean{" +
                "status='" + status + '\'' +
                ", msg='" + msg + '\'' +
                ", smsg='" + smsg + '\'' +
                ", pageSize='" + pageSize + '\'' +
                ", PageCount='" + PageCount + '\'' +
                ", TotalCount='" + TotalCount + '\'' +
                ", obj=" + obj +
                '}';
    }
}
