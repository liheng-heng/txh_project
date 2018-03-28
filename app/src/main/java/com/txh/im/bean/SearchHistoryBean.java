package com.txh.im.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by jiajia on 2017/3/22.
 */
@Entity
public class SearchHistoryBean {
    @Id(autoincrement = true)
    private Long id;
    private String username;
    private String locatinUsername;
    @Generated(hash = 72073478)
    public SearchHistoryBean(Long id, String username, String locatinUsername) {
        this.id = id;
        this.username = username;
        this.locatinUsername = locatinUsername;
    }
    @Generated(hash = 1570282321)
    public SearchHistoryBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getLocatinUsername() {
        return this.locatinUsername;
    }
    public void setLocatinUsername(String locatinUsername) {
        this.locatinUsername = locatinUsername;
    }
}
