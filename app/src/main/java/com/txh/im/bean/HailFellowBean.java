package com.txh.im.bean;

import java.util.List;
/**
 * Created by jiajia on 2017/4/10.
 */

public class HailFellowBean {

    /**
     * Index : 0
     * Name : 我的店铺
     * List : [{"UnitId":13,"UnitType":1,"IsTrue":1,"UnitName":null,"FromUnitId":0,"UserMapId":100010,"Icon":"","Item":[{"UserId":100010,"UserName":"18817384535","RoleId":2,"RoleType":0,"RoleName":"主管理员","ImagesHead":"http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png"}]}]
     */

    private String Index;
    private String Name;
    private List<FriendsShopBean> List;

    public String getIndex() {
        return Index;
    }

    public void setIndex(String index) {
        Index = index;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public java.util.List<FriendsShopBean> getList() {
        return List;
    }

    public void setList(java.util.List<FriendsShopBean> list) {
        List = list;
    }

    @Override
    public String toString() {
        return "HailFellowBean{" +
                "Index='" + Index + '\'' +
                ", Name='" + Name + '\'' +
                ", List=" + List +
                '}';
    }
}
