package com.txh.im.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/3/3.
 * 验证码
 */

public class HttpBean implements Serializable{

    /**
     * status :
     * msg :
     * smsg : 用户已注册，请直接登录
     * obj : {"Result":"2","Phone":"186*****341","UserName":"","TXHCode":"","UserQRUrl":"http://img.tianxiahuo.cn/public/NetFile/20170306/dad2db3f16b33c3d8a95db4bb39a7a.png","ImagesHead":""}
     */

    private String status;
    private String msg;
    private String smsg;
    private CryptonyBean obj;

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

    public CryptonyBean getObj() {
        return obj;
    }

    public void setObj(CryptonyBean obj) {
        this.obj = obj;
    }

}
