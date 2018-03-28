package com.txh.im.bean;

import java.util.List;

/**
 * Created by lyl on 2017/4/25.
 */

public class FristWordBean {
    private String FristWord;
    private List<FriendsShopBean> List;

    public String getFristWord() {
        return FristWord;
    }

    public void setFristWord(String fristWord) {
        FristWord = fristWord;
    }

    public java.util.List<FriendsShopBean> getList() {
        return List;
    }

    public void setList(java.util.List<FriendsShopBean> list) {
        List = list;
    }

    @Override
    public String toString() {
        return "FristWordBean{" +
                "FristWord='" + FristWord + '\'' +
                ", List=" + List +
                '}';
    }
}
