package com.txh.im.bean;

/**
 * Created by liheng on 2017/4/26.
 */

public class DisclaimerBean {

    /**
     * AgreementId : 1
     * AgreementTitle : 服务条款
     * AgreementContent : <p style="margin-left:0cm;background:white;">
     <b>1</b><b>、</b><b>天下货</b><b>服务条款的接受</b>
     </p>
     */

    private String AgreementId;
    private String AgreementTitle;
    private String AgreementContent;

    public String getAgreementId() {
        return AgreementId;
    }

    public void setAgreementId(String AgreementId) {
        this.AgreementId = AgreementId;
    }

    public String getAgreementTitle() {
        return AgreementTitle;
    }

    public void setAgreementTitle(String AgreementTitle) {
        this.AgreementTitle = AgreementTitle;
    }

    public String getAgreementContent() {
        return AgreementContent;
    }

    public void setAgreementContent(String AgreementContent) {
        this.AgreementContent = AgreementContent;
    }
}
