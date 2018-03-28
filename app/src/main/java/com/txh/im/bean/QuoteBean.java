package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/5/23.
 * 报价单管理bean
 */

public class QuoteBean {

    /**
     * PageTitle : 报价单管理
     * PageSize : 10
     * UnitList : [{"UnitId":"100","UnitName":"张三小店","UnitLogo":"","GroupId":"13","GroupName":"A组"},{"UnitId":"101","UnitName":"李四小店","UnitLogo":"","GroupId":"13","GroupName":"B组"}]
     */

    private String PageTitle;
    private int PageSize;
    private List<UnitBean> UnitList;

    public String getPageTitle() {
        return PageTitle;
    }

    public void setPageTitle(String pageTitle) {
        PageTitle = pageTitle;
    }

    public int getPageSize() {
        return PageSize;
    }

    public void setPageSize(int pageSize) {
        PageSize = pageSize;
    }

    public List<UnitBean> getUnitList() {
        return UnitList;
    }

    public void setUnitList(List<UnitBean> unitList) {
        UnitList = unitList;
    }
}
