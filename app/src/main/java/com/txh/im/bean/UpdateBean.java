package com.txh.im.bean;

/**
 * content :  检查版本
 */
public class UpdateBean {
    private String VersionCode;
    private String VersionUrl;
    private String VersionContent;
    private String UpdateType;

    public String getVersionCode() {
        return VersionCode;
    }

    public void setVersionCode(String VersionCode) {
        this.VersionCode = VersionCode;
    }

    public String getVersionUrl() {
        return VersionUrl;
    }

    public void setVersionUrl(String VersionUrl) {
        this.VersionUrl = VersionUrl;
    }

    public String getVersionContent() {
        return VersionContent;
    }

    public void setVersionContent(String VersionContent) {
        this.VersionContent = VersionContent;
    }

    public String getUpdateType() {
        return UpdateType;
    }

    public void setUpdateType(String UpdateType) {
        this.UpdateType = UpdateType;
    }

}