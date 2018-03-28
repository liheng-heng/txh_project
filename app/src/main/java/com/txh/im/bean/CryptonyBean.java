package com.txh.im.bean;

/**
 * Created by Administrator on 2017/02/21.
 * 用户基本信息
 */
public class CryptonyBean {

    /**
     "Result":"1",//1注册成功且可以自动登录，2用户已注册，请去登录，3注册成功，但设置usertoken失败，不能自动登录
     "UserId": "73",//用户ID
     "UserName": "",//昵称
     "Sex": "0",//性别：1男，2女，0未知
     "Phone": "18601760347",//手机号
     "ImagesHead": "",//用户头像
     "UserQRUrl": "http://img.tianxiahuo.cn/public/NetFile/20170307/da843ec5ff9f91e847133ca45385bc9.png",//用户二维码图片url
     "IsSetLoginPwd": "0",//是否设置了登录密码
     "IsSetPayPwd": "0",//是否设置了支付密码
     "RoleId": "",
     "TXHCode": "nslbu248885****",//天下货号
     "UserToken": "34445da9-ec37-44ca-9944-3a48f806c044",//用户自动登录后usertoken
     "WaitTimeForAutoLogin":"5"//注册成功后自动登录等待的时间
     "HXIMPwd":"cb4e8ca283f3bdf9489454177db3b076"
     --------------------------------------------------------
     "APPName": "天下货",
     "APPLogo": "http://img.tianxiahuo.cn/public/NetFile/20170308/ee10ca3ac1ab9abcfce5fefb4e60dbfe.png",//APPLogo
     "VCodeUrl": "http://192.168.3.18:9999/content/VCodePic/2017030810/PIC_63624564690058501254.png"//H5中验证码图片URL访问地址

     "LocationId": "",//地区ID
     "ProvinceId": "",//省份ID
     "CityId": "",//城市ID
     "ZoneId": "",//区\县ID
     "ProvinceName": "",//省份名称
     "CityName": "",//城市名称
     "ZoneName": "",//区\县名称
     "Address": "",//详细地址

     "UnitType":"1",//1买家，2买家
     "UnitId":"10"//UnitType对应的商户ID，如配送商的ID
     "ShopInfoIsOK": "0"//商户信息是否ok（0否，1是）（当身份为卖家unittype=2且该值为0时跳到我的资料编辑页面）

     */

    /**
     * 应用名称
     */
    private String APPName;
    /**
     * 应用logo
     */
    private String APPLogo;

    /**
     * 1注册成功且可以自动登录，2用户已注册，请去登录，3注册成功，但设置usertoken失败，不能自动登录
     */
    private String Result;
    /**
     * 用户ID
     */
    private String UserId;
    /**
     * 昵称
     */
    private String UserName;
    /**
     * 性别：1男，2女，0未知
     */
    private String Sex;
    /**
     * 手机号
     */
    private String Phone;
    /**
     * 用户头像
     */
    private String ImagesHead;
    /**
     * 用户二维码图
     */
    private String UserQRUrl;
    /**
     * 是否设置了登录密码
     */
    private String IsSetLoginPwd;
    /**
     * 是否设置了支付密码
     */
    private String IsSetPayPwd;
    /**
     * 角色ID
     */
    private String RoleId;
    /**
     * 用户自动登录后usertoken
     */
    private String UserToken;
    /**
     * 天下货号
     */
    private String TXHCode;
    /**
     * 环信密码
     */
    private String HXIMPwd;
    /**
     * 自动登录等待时间
     */
    private String WaitTimeForAutoLogin;

//            "LocationId": "",//地区ID
//            "ProvinceId": "",//省份ID
//            "CityId": "",//城市ID
//            "ZoneId": "",//区\县ID
//            "ProvinceName": "",//省份名称
//            "CityName": "",//城市名称
//            "ZoneName": "",//区\县名称
//            "Address": "",//详细地址

    private String LocationId;
    private String ProvinceId;
    private String CityId;
    private String ZoneId;
    private String ProvinceName;
    private String CityName;
    private String ZoneName;
    private String Address;

    private String longitude;
    private String latitude;

    /**
     * 1买家，2卖家
     */
    private String UnitType;
    /**
     * UnitType对应的商户ID，如配送商的ID
     */
    private String UnitId;

    /**
     * "ShopInfoIsOK": "0"//商户信息是否ok（0否，1是）（当身份为卖家unittype=2且该值为0时跳到我的资料编辑页面）
     *
     * @return
     */

    private String ShopInfoIsOK;

    public String getShopInfoIsOK() {
        return ShopInfoIsOK;
    }

    public void setShopInfoIsOK(String shopInfoIsOK) {
        ShopInfoIsOK = shopInfoIsOK;
    }

    public String getUnitType() {
        return UnitType;
    }

    public void setUnitType(String unitType) {
        UnitType = unitType;
    }

    public String getUnitId() {
        return UnitId;
    }

    public void setUnitId(String unitId) {
        UnitId = unitId;
    }

    public String getLocationId() {
        return LocationId;
    }

    public void setLocationId(String locationId) {
        LocationId = locationId;
    }

    public String getProvinceId() {
        return ProvinceId;
    }

    public void setProvinceId(String provinceId) {
        ProvinceId = provinceId;
    }

    public String getCityId() {
        return CityId;
    }

    public void setCityId(String cityId) {
        CityId = cityId;
    }

    public String getZoneId() {
        return ZoneId;
    }

    public void setZoneId(String zoneId) {
        ZoneId = zoneId;
    }

    public String getProvinceName() {
        return ProvinceName;
    }

    public void setProvinceName(String provinceName) {
        ProvinceName = provinceName;
    }

    public String getCityName() {
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getZoneName() {
        return ZoneName;
    }

    public void setZoneName(String zoneName) {
        ZoneName = zoneName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAPPName() {
        return APPName;
    }

    public void setAPPName(String APPName) {
        this.APPName = APPName;
    }

    public String getAPPLogo() {
        return APPLogo;
    }

    public void setAPPLogo(String APPLogo) {
        this.APPLogo = APPLogo;
    }

    public String getHXIMPwd() {
        return HXIMPwd;
    }

    public void setHXIMPwd(String HXIMPwd) {
        this.HXIMPwd = HXIMPwd;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getWaitTimeForAutoLogin() {
        return WaitTimeForAutoLogin;
    }

    public void setWaitTimeForAutoLogin(String waitTimeForAutoLogin) {
        WaitTimeForAutoLogin = waitTimeForAutoLogin;
    }

    public String getTXHCode() {
        return TXHCode;
    }

    public void setTXHCode(String TXHCode) {
        this.TXHCode = TXHCode;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getImagesHead() {
        return ImagesHead;
    }

    public void setImagesHead(String imagesHead) {
        ImagesHead = imagesHead;
    }

    public String getUserQRUrl() {
        return UserQRUrl;
    }

    public void setUserQRUrl(String userQRUrl) {
        UserQRUrl = userQRUrl;
    }

    public String getIsSetLoginPwd() {
        return IsSetLoginPwd;
    }

    public void setIsSetLoginPwd(String isSetLoginPwd) {
        IsSetLoginPwd = isSetLoginPwd;
    }

    public String getIsSetPayPwd() {
        return IsSetPayPwd;
    }

    public void setIsSetPayPwd(String isSetPayPwd) {
        IsSetPayPwd = isSetPayPwd;
    }

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
