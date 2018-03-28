package com.txh.im.bean;

/**
 * Created by jiajia on 2017/4/10.
 */

public class FriendsRolesBean {
    /**
     * UserId : 100010
     * UserName : 18817384535
     * RoleId : 2
     * RoleType : 0
     * RoleName : 主管理员
     * IsFriend: 0
     * ImagesHead : http://img.tianxiahuo.cn/public/NetFile/20170309/dd1d1c9e72477e1e27c161c81bd1c3.png
     */
    private String UserId;
    private String UserName;
    private String RoleId;
    private int RoleType;
    private String RoleName;
    private String ImagesHead;
    private String IsFriend;
    private String MarkName;
    private int isChecked;//0是未选择,1是选择了

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

    public String getRoleId() {
        return RoleId;
    }

    public void setRoleId(String roleId) {
        RoleId = roleId;
    }

    public int getRoleType() {
        return RoleType;
    }

    public void setRoleType(int roleType) {
        RoleType = roleType;
    }

    public String getRoleName() {
        return RoleName;
    }

    public void setRoleName(String roleName) {
        RoleName = roleName;
    }

    public String getImagesHead() {
        return ImagesHead;
    }

    public void setImagesHead(String imagesHead) {
        ImagesHead = imagesHead;
    }

    public String getIsFriend() {
        return IsFriend;
    }

    public void setIsFriend(String isFriend) {
        IsFriend = isFriend;
    }

    public String getMarkName() {
        return MarkName;
    }

    public void setMarkName(String markName) {
        MarkName = markName;
    }

    public int getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(int isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "FriendsRolesBean{" +
                "UserId='" + UserId + '\'' +
                ", UserName='" + UserName + '\'' +
                ", RoleId='" + RoleId + '\'' +
                ", RoleType=" + RoleType +
                ", RoleName='" + RoleName + '\'' +
                ", ImagesHead='" + ImagesHead + '\'' +
                ", IsFriend='" + IsFriend + '\'' +
                ", MarkName='" + MarkName + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }
}
