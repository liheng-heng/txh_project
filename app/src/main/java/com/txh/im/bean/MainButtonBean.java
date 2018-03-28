package com.txh.im.bean;

/**
 * Created by jiajia on 2017/2/13.
 */

public class MainButtonBean {


    /**
     * Title : 首页
     * ImgUrl : http://img.tianxiahuo.cn/public/NetFile/20170315/3ff2f14c0762cc734e64b528e3b415d.png
     * SelectedImgUrl : http://img.tianxiahuo.cn/public/NetFile/20170315/9f446692aa8012f87a9a9126d1ac3d2f.png
     * ToURL : index.html
     * MenuCode : FirstPage
     * OrderId : 1
     * DefaultColor : #666666
     * SelectedColor : #E75858
     */

    private String Title;
    private String ImgUrl;
    private String SelectedImgUrl;
    private String ToURL;
    private String MenuCode;
    private String OrderId;
    private String DefaultColor;
    private String SelectedColor;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String ImgUrl) {
        this.ImgUrl = ImgUrl;
    }

    public String getSelectedImgUrl() {
        return SelectedImgUrl;
    }

    public void setSelectedImgUrl(String SelectedImgUrl) {
        this.SelectedImgUrl = SelectedImgUrl;
    }

    public String getToURL() {
        return ToURL;
    }

    public void setToURL(String ToURL) {
        this.ToURL = ToURL;
    }

    public String getMenuCode() {
        return MenuCode;
    }

    public void setMenuCode(String MenuCode) {
        this.MenuCode = MenuCode;
    }

    public String getOrderId() {
        return OrderId;
    }

    public void setOrderId(String OrderId) {
        this.OrderId = OrderId;
    }

    public String getDefaultColor() {
        return DefaultColor;
    }

    public void setDefaultColor(String DefaultColor) {
        this.DefaultColor = DefaultColor;
    }

    public String getSelectedColor() {
        return SelectedColor;
    }

    public void setSelectedColor(String SelectedColor) {
        this.SelectedColor = SelectedColor;
    }
}
