package com.txh.im.bean;

/**
 * Created by Administrator on 2016/1/21.
 */
public class Basebean_Net {

    private String Status;
    private String ErrorCode;
    private String Message;
    private String Data;

    /*
       "Status": 0,
       "ErrorCode": "0",
       "Message": "接口调用成功",
       "Data": "{\"status\":\"20。。。。。。}
     */

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String errorCode) {
        ErrorCode = errorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "Status='" + Status + '\'' +
                ", ErrorCode='" + ErrorCode + '\'' +
                ", Message='" + Message + '\'' +
                ", Data='" + Data + '\'' +
                '}';
    }
}
