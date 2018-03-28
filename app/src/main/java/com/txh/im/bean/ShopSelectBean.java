package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/5/26.
 */

public class ShopSelectBean {

    /**
     * PageSize : 10
     * UnitList : [{"UnitId":"1143","UnitName":"15933966159","UnitLogo":"","IsAdd":"0"},{"UnitId":"1693","UnitName":"15900840444","UnitLogo":"","IsAdd":"0"},{"UnitId":"1683","UnitName":"15900840888","UnitLogo":"","IsAdd":"0"},{"UnitId":"1681","UnitName":"15900023951","UnitLogo":"","IsAdd":"0"},{"UnitId":"1679","UnitName":"15900840016","UnitLogo":"","IsAdd":"0"},{"UnitId":"1679","UnitName":"15900840016","UnitLogo":"","IsAdd":"0"},{"UnitId":"1679","UnitName":"15900840016","UnitLogo":"","IsAdd":"0"},{"UnitId":"1657","UnitName":"15900883291","UnitLogo":"","IsAdd":"0"},{"UnitId":"1663","UnitName":"15901718238","UnitLogo":"","IsAdd":"0"},{"UnitId":"1677","UnitName":"15900840009","UnitLogo":"","IsAdd":"0"}]
     */

    private int PageSize;
    private List<UnitBean> UnitList;

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
