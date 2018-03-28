package com.txh.im.bean;

import java.util.List;

public class GoodsAddressBean_new {


    /**
     * IsLack : 0
     * List : [{"AddressId":"521","Name":"贺俊欣揍小圆剧场","Phone":"15021741524","ProvinceCityZoneText":"上海市 上海市 长宁区","ReceiveAddress":"天山西路1068号","IsDefault":"0","IsSelf":"1","Longitude":"121.353794","Latitude":"31.219413"},{"AddressId":"516","Name":"图咯","Phone":"15021741258","ProvinceCityZoneText":"上海市 上海市 长宁区","ReceiveAddress":"金钟路858号","IsDefault":"1","IsSelf":"1","Longitude":"121.354510","Latitude":"31.220840"},{"AddressId":"515","Name":"歌剧院","Phone":"15021741827","ProvinceCityZoneText":"上海市 上海市 长宁区","ReceiveAddress":"天山西路1068号第II期1层A单元","IsDefault":"0","IsSelf":"1","Longitude":"121.353794","Latitude":"31.219413"},{"AddressId":"514","Name":"贺俊欣","Phone":"15021741854","ProvinceCityZoneText":"上海市 上海市 长宁区","ReceiveAddress":"金钟路999号虹桥国际科技广场A座","IsDefault":"0","IsSelf":"1","Longitude":"121.351790","Latitude":"31.220547"},{"AddressId":"503","Name":"贺俊欣测试","Phone":"15021741827","ProvinceCityZoneText":"上海市 上海市 长宁区","ReceiveAddress":"协和路1033号(近北翟路)","IsDefault":"0","IsSelf":"1","Longitude":"121.353676","Latitude":"31.223031"},{"AddressId":"473","Name":"搞活经济","Phone":"15023423154","ProvinceCityZoneText":"上海市 上海市 长宁区","ReceiveAddress":"天山西路1068号第II期1层A单元","IsDefault":"0","IsSelf":"1","Longitude":"121.353794","Latitude":"31.219413"},{"AddressId":"316","Name":"谢谢谢谢谢谢谢谢谢谢谢谢谢谢","Phone":"15021741524","ProvinceCityZoneText":"江苏省 苏州市 姑苏区","ReceiveAddress":"统一战线","IsDefault":"0","IsSelf":"1","Longitude":"120.617296","Latitude":"31.336392"},{"AddressId":"312","Name":"好咯微信我","Phone":"15021741528","ProvinceCityZoneText":"江苏省 南京市 高淳区","ReceiveAddress":"好旅途","IsDefault":"0","IsSelf":"1","Longitude":"118.892085","Latitude":"31.328471"},{"AddressId":"309","Name":"图哦健健康康","Phone":"15236254125","ProvinceCityZoneText":"安徽省 安庆市 大观区","ReceiveAddress":"统一小心翼翼","IsDefault":"0","IsSelf":"1","Longitude":"117.021670","Latitude":"30.553957"},{"AddressId":"305","Name":"测试袁","Phone":"15900843291","ProvinceCityZoneText":"安徽省 芜湖市 南陵县","ReceiveAddress":"许镇镇太丰乡太丰街道888号","IsDefault":"0","IsSelf":"1","Longitude":"118.413846","Latitude":"31.119877"}]
     */

    private String IsLack;
    private java.util.List<ListBean> List;

    public String getIsLack() {
        return IsLack;
    }

    public void setIsLack(String isLack) {
        IsLack = isLack;
    }

    public List<ListBean> getList() {
        return List;
    }

    public void setList(List<ListBean> List) {
        this.List = List;
    }

    public static class ListBean {
        /**
         * AddressId : 521
         * Name : 贺俊欣揍小圆剧场
         * Phone : 15021741524
         * ProvinceCityZoneText : 上海市 上海市 长宁区
         * ReceiveAddress : 天山西路1068号
         * IsDefault : 0
         * IsSelf : 1
         * Longitude : 121.353794
         * Latitude : 31.219413
         */

        private String AddressId;
        private String Name;
        private String Phone;
        private String ProvinceCityZoneText;
        private String ReceiveAddress;
        private String IsDefault;
        private String IsSelf;
        private String Longitude;
        private String Latitude;

        public String getAddressId() {
            return AddressId;
        }

        public void setAddressId(String AddressId) {
            this.AddressId = AddressId;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getPhone() {
            return Phone;
        }

        public void setPhone(String Phone) {
            this.Phone = Phone;
        }

        public String getProvinceCityZoneText() {
            return ProvinceCityZoneText;
        }

        public void setProvinceCityZoneText(String ProvinceCityZoneText) {
            this.ProvinceCityZoneText = ProvinceCityZoneText;
        }

        public String getReceiveAddress() {
            return ReceiveAddress;
        }

        public void setReceiveAddress(String ReceiveAddress) {
            this.ReceiveAddress = ReceiveAddress;
        }

        public String getIsDefault() {
            return IsDefault;
        }

        public void setIsDefault(String IsDefault) {
            this.IsDefault = IsDefault;
        }

        public String getIsSelf() {
            return IsSelf;
        }

        public void setIsSelf(String IsSelf) {
            this.IsSelf = IsSelf;
        }

        public String getLongitude() {
            return Longitude;
        }

        public void setLongitude(String Longitude) {
            this.Longitude = Longitude;
        }

        public String getLatitude() {
            return Latitude;
        }

        public void setLatitude(String Latitude) {
            this.Latitude = Latitude;
        }
    }
}
