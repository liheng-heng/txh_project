package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/2/13.
 */

public class DemoListBean {
    /**
     * BrandId : 11
     * BrandName : 哇哈哈
     */

    private String BrandId;
    private String BrandName;
    private boolean isselect = false;

    public boolean isselect() {
        return isselect;
    }

    public void setIsselect(boolean isselect) {
        this.isselect = isselect;
    }

    public String getBrandId() {
        return BrandId;
    }

    public void setBrandId(String BrandId) {
        this.BrandId = BrandId;
    }

    public String getBrandName() {
        return BrandName;
    }

    public void setBrandName(String BrandName) {
        this.BrandName = BrandName;
    }
}
