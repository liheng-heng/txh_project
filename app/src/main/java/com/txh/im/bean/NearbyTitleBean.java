package com.txh.im.bean;

import java.util.List;

public class NearbyTitleBean {

    /**
     * IsMatch : 0
     * List : [{"Radius":"5","Text":"5km内"},{"Radius":"10","Text":"10km内"},{"Radius":"0","Text":"不限"}]
     * <p>
     * <p>
     * {
     * "Location":"121.648686,31.465868", //经纬度 以","隔开的
     * "Default":"金钟路968号",
     * "List":[{
     * "Radius":"5",
     * "Text"="5km内"
     * },
     * {
     * "Radius":"10",
     * "Text"="10km内"
     * },
     * {
     * "Radius":"0",
     * "Text"="不限"
     * },
     * ]
     * }
     */

    private String IsMatch;
    private String Default;
    private String Location;

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDefault() {
        return Default;
    }

    public void setDefault(String aDefault) {
        Default = aDefault;
    }

    private java.util.List<ListBean> List;

    public String getIsMatch() {
        return IsMatch;
    }

    public void setIsMatch(String IsMatch) {
        this.IsMatch = IsMatch;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {
        /**
         * Radius : 5
         * Text : 5km内
         */

        private String Radius;
        private String Text;
        private boolean isselect = false;

        public boolean isselect() {
            return isselect;
        }

        public void setIsselect(boolean isselect) {
            this.isselect = isselect;
        }

        public String getRadius() {
            return Radius;
        }

        public void setRadius(String Radius) {
            this.Radius = Radius;
        }

        public String getText() {
            return Text;
        }

        public void setText(String Text) {
            this.Text = Text;
        }
    }
}
