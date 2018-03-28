package com.txh.im.bean;

/**
 * Created by jiajia on 2017/4/11.
 */

public class GetAskListBean {
    /**
     * FId : 11
     * FromUserId : 100001
     * FromUnitId : 4
     * UserName : 15222222222
     * TXHCode : sjtvv2489587604
     * ImagesHead : http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png
     * Status : 0
     * UpdateTime : 2017-04-11T19:00:43.407
     */

    private String FId;
    private String FromUserId;
    private String FromUnitId;
    private String UserName;
    private String TXHCode;
    private String ImagesHead;
    private int Status;
    private String UpdateTime;

    public String getFId() {
        return FId;
    }

    public void setFId(String FId) {
        this.FId = FId;
    }

    public String getFromUserId() {
        return FromUserId;
    }

    public void setFromUserId(String fromUserId) {
        FromUserId = fromUserId;
    }

    public String getFromUnitId() {
        return FromUnitId;
    }

    public void setFromUnitId(String fromUnitId) {
        FromUnitId = fromUnitId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getTXHCode() {
        return TXHCode;
    }

    public void setTXHCode(String TXHCode) {
        this.TXHCode = TXHCode;
    }

    public String getImagesHead() {
        return ImagesHead;
    }

    public void setImagesHead(String imagesHead) {
        ImagesHead = imagesHead;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }

    @Override
    public String toString() {
        return "GetAskListBean{" +
                "FId='" + FId + '\'' +
                ", FromUserId='" + FromUserId + '\'' +
                ", FromUnitId='" + FromUnitId + '\'' +
                ", UserName='" + UserName + '\'' +
                ", TXHCode='" + TXHCode + '\'' +
                ", ImagesHead='" + ImagesHead + '\'' +
                ", Status=" + Status +
                ", UpdateTime='" + UpdateTime + '\'' +
                '}';
    }
}
