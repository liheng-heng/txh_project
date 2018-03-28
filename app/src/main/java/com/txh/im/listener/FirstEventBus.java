package com.txh.im.listener;

import com.amap.api.services.core.PoiItem;

/**
 * Created by jiajia on 2017/5/6.
 */

public class FirstEventBus {
    private String mMsg;
    private PoiItem poiItem;
    private String address;

    private String callback_receiveAddress;
    private String callback_longitude;
    private String callback_latitude;

    public FirstEventBus(PoiItem poiItem, String msg, String address) {
        this.poiItem = poiItem;
        mMsg = msg;
        this.address = address;
    }

    public FirstEventBus(String msg, String callback_receiveAddress, String callback_longitude, String callback_latitude) {
        mMsg = msg;
        this.callback_receiveAddress = callback_receiveAddress;
        this.callback_longitude = callback_longitude;
        this.callback_latitude = callback_latitude;

    }

    public FirstEventBus(String msg) {
        mMsg = msg;
    }

    public String getMsg() {
        return mMsg;
    }

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public String getAddress() {
        return address;
    }

    public String getCallback_receiveAddress() {
        return callback_receiveAddress;
    }

    public void setCallback_receiveAddress(String callback_receiveAddress) {
        this.callback_receiveAddress = callback_receiveAddress;
    }

    public String getCallback_longitude() {
        return callback_longitude;
    }

    public void setCallback_longitude(String callback_longitude) {
        this.callback_longitude = callback_longitude;
    }

    public String getCallback_latitude() {
        return callback_latitude;
    }

    public void setCallback_latitude(String callback_latitude) {
        this.callback_latitude = callback_latitude;
    }
}
