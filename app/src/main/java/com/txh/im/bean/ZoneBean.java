package com.txh.im.bean;

/**
 * Created by jiajia on 2017/2/20.
 */

public class ZoneBean {

    /**
     * *
     "LocationId":"1193",
     "ParentId":"1",
     "LocationName":"安徽省",
     "LocationType":"2",
     "LocationLetterIndex":"A"
     */

    private String LocationId;
    private String ParentId;
    private String LocationName;
    private String LocationType;
    private String LocationLetterIndex;

    public String getLocationId() {
        return LocationId;
    }

    public void setLocationId(String locationId) {
        LocationId = locationId;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getLocationType() {
        return LocationType;
    }

    public void setLocationType(String locationType) {
        LocationType = locationType;
    }

    public String getLocationLetterIndex() {
        return LocationLetterIndex;
    }

    public void setLocationLetterIndex(String locationLetterIndex) {
        LocationLetterIndex = locationLetterIndex;
    }
}
