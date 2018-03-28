package com.txh.im.bean;

import java.util.List;

/**
 * Created by liheng on 2017/4/20.
 */

public class GetHistoryMerchantBean {

    private List<SearchKeyWordHistoryBean> SearchKeyWordHistory;

    public List<SearchKeyWordHistoryBean> getSearchKeyWordHistory() {
        return SearchKeyWordHistory;
    }

    public void setSearchKeyWordHistory(List<SearchKeyWordHistoryBean> SearchKeyWordHistory) {
        this.SearchKeyWordHistory = SearchKeyWordHistory;
    }

    public static class SearchKeyWordHistoryBean {
        /**
         * KeyWord : 天下货
         * ObjTypes :
         * ObjTypeNames :
         * SearchTime : 2017-03-27
         */

        private String KeyWord;
        private String ObjTypes;
        private String ObjTypeNames;
        private String SearchTime;

        public String getKeyWord() {
            return KeyWord;
        }

        public void setKeyWord(String KeyWord) {
            this.KeyWord = KeyWord;
        }

        public String getObjTypes() {
            return ObjTypes;
        }

        public void setObjTypes(String ObjTypes) {
            this.ObjTypes = ObjTypes;
        }

        public String getObjTypeNames() {
            return ObjTypeNames;
        }

        public void setObjTypeNames(String ObjTypeNames) {
            this.ObjTypeNames = ObjTypeNames;
        }

        public String getSearchTime() {
            return SearchTime;
        }

        public void setSearchTime(String SearchTime) {
            this.SearchTime = SearchTime;
        }
    }
}
