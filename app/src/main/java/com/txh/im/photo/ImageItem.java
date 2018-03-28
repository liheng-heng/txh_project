package com.txh.im.photo;

import java.io.Serializable;

/**
 * 图片对象
 */
public class ImageItem implements Serializable {

    private static final long serialVersionUID = -7188270558443739436L;
    public String imageId;
    public String thumbnailPath;
    public String sourcePath;
    public boolean isSelected = false;
    private String oss_path;

    public String getOss_path() {
        return oss_path;
    }

    public void setOss_path(String oss_path) {
        this.oss_path = oss_path;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    @Override
    public String toString() {
        return "ImageItem{" +
                "imageId='" + imageId + '\'' +
                ", thumbnailPath='" + thumbnailPath + '\'' +
                ", sourcePath='" + sourcePath + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }
}
