package com.txh.im.photo;

import java.util.ArrayList;
import java.util.List;

/**
 * http://blog.csdn.net/lmj623565791/article/details/40212367
 *
 * @author zhy
 */
public class Node {

    private int id;
    /**
     * 根节点pId为0
     */
    private int pId = 0;

    private String name;

    /**
     * 当前的级别
     */
    private int level;

    /**
     * 是否展开
     */
    private boolean isExpand = false;

    /**
     *
     */
    private int icon;

    /**
     * 下一级的子Node
     */
    private List<Node> children = new ArrayList<Node>();

    /**
     * 父Node
     */
    private Node parent;

    public Node() {
    }

    /**
     * Checkbox是否选中
     */

    private boolean is = false;

    private String phone;
    private String nickname;
    private String company_job;
    private String Treeimages_head;
    private String hx_username;
    private String assessor_uid;

    public Node(int id, int pId, String name, String phone, String nickname, String company_job, String Treeimages_head, String hx_username, String assessor_uid) {
        super();
        this.id = id;
        this.pId = pId;
        this.name = name;
        this.phone = phone;
        this.nickname = nickname;
        this.company_job = company_job;
        this.Treeimages_head = Treeimages_head;
        this.hx_username = hx_username;
        this.assessor_uid = assessor_uid;
    }

    public String getTreeimages_head() {
        return Treeimages_head;
    }

    public void setTreeimages_head(String treeimages_head) {
        Treeimages_head = treeimages_head;
    }

    public String getHx_username() {
        return hx_username;
    }

    public void setHx_username(String hx_username) {
        this.hx_username = hx_username;
    }

    public String getAssessor_uid() {
        return assessor_uid;
    }

    public void setAssessor_uid(String assessor_uid) {
        this.assessor_uid = assessor_uid;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * 是否为跟节点
     *
     * @return
     */
    public boolean isRoot() {
        return parent == null;
    }

    /**
     * 判断父节点是否展开
     *
     * @return
     */
    public boolean isParentExpand() {
        if (parent == null)
            return false;
        return parent.isExpand();
    }

    /**
     * 是否是叶子界点
     *
     * @return
     */
    public boolean isLeaf() {
        return children.size() == 0;
    }

    /**
     * 获取level
     */
    public int getLevel() {
        return parent == null ? 0 : parent.getLevel() + 1;
    }

    /**
     * 设置展开
     *
     * @param isExpand
     */
    public void setExpand(boolean isExpand) {
        this.isExpand = isExpand;
        if (!isExpand) {

            for (Node node : children) {
                node.setExpand(isExpand);
            }
        }
    }

    public void setIsExpand(boolean isExpand) {
        this.isExpand = isExpand;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCompany_job() {
        return company_job;
    }

    public void setCompany_job(String company_job) {
        this.company_job = company_job;
    }

    public boolean getIs() {
        return is;
    }

    public void setIs(boolean is) {
        this.is = is;
    }
}
