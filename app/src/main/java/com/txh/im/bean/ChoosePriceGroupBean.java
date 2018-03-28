package com.txh.im.bean;

import java.util.List;

/**
 * Created by jiajia on 2017/6/2.
 * 选择价格分组
 */

public class ChoosePriceGroupBean {
    private List<UnitBean> GroupList;

    public List<UnitBean> getGroupList() {
        return GroupList;
    }

    public void setGroupList(List<UnitBean> groupList) {
        GroupList = groupList;
    }
}
