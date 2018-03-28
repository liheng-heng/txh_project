package com.txh.im.bean;

/**
 * Created by jiajia on 2017/6/26.
 */

public class AppShare {

    /**
     * AppShareInfo : {"UserKey":1856114945849887000,"ShareTitle":"天下货APP下载地址","SharePic":"http://img.tianxiahuo.cn/public/txh_newapp/User/applogo_120.png","ShareUrl":"http://h5.new.tianxiahuo.cn/shareDown.html","ShareMsg":"天下货下载地址：http://h5.new.tianxiahuo.cn/shareDown.html"}
     */

    private AppShareInfoBean AppShareInfo;

    public AppShareInfoBean getAppShareInfo() {
        return AppShareInfo;
    }

    public void setAppShareInfo(AppShareInfoBean AppShareInfo) {
        this.AppShareInfo = AppShareInfo;
    }

    public static class AppShareInfoBean {
        /**
         * UserKey : 1856114945849887000
         * ShareTitle : 天下货APP下载地址
         * SharePic : http://img.tianxiahuo.cn/public/txh_newapp/User/applogo_120.png
         * ShareUrl : http://h5.new.tianxiahuo.cn/shareDown.html
         * ShareMsg : 天下货下载地址：http://h5.new.tianxiahuo.cn/shareDown.html
         */

        private long UserKey;
        private String ShareTitle;
        private String SharePic;
        private String ShareUrl;
        private String ShareMsg;
        private String ShareDesc;

        public long getUserKey() {
            return UserKey;
        }

        public void setUserKey(long UserKey) {
            this.UserKey = UserKey;
        }

        public String getShareTitle() {
            return ShareTitle;
        }

        public void setShareTitle(String ShareTitle) {
            this.ShareTitle = ShareTitle;
        }

        public String getSharePic() {
            return SharePic;
        }

        public void setSharePic(String SharePic) {
            this.SharePic = SharePic;
        }

        public String getShareUrl() {
            return ShareUrl;
        }

        public void setShareUrl(String ShareUrl) {
            this.ShareUrl = ShareUrl;
        }

        public String getShareMsg() {
            return ShareMsg;
        }

        public void setShareMsg(String ShareMsg) {
            this.ShareMsg = ShareMsg;
        }

        public String getShareDesc() {
            return ShareDesc;
        }

        public void setShareDesc(String shareDesc) {
            ShareDesc = shareDesc;
        }
    }
}
