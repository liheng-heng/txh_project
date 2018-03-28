package com.txh.im.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.txh.im.bean.HomeSingleListBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HOME_SINGLE_LIST_BEAN".
*/
public class HomeSingleListBeanDao extends AbstractDao<HomeSingleListBean, Long> {

    public static final String TABLENAME = "HOME_SINGLE_LIST_BEAN";

    /**
     * Properties of entity HomeSingleListBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property UserId = new Property(1, String.class, "UserId", false, "USER_ID");
        public final static Property UserName = new Property(2, String.class, "UserName", false, "USER_NAME");
        public final static Property TXHCode = new Property(3, String.class, "TXHCode", false, "TXHCODE");
        public final static Property ImagesHead = new Property(4, String.class, "ImagesHead", false, "IMAGES_HEAD");
        public final static Property IsFriend = new Property(5, String.class, "IsFriend", false, "IS_FRIEND");
        public final static Property IsShop = new Property(6, String.class, "IsShop", false, "IS_SHOP");
        public final static Property Icon = new Property(7, String.class, "Icon", false, "ICON");
        public final static Property ProvinceName = new Property(8, String.class, "ProvinceName", false, "PROVINCE_NAME");
        public final static Property CityName = new Property(9, String.class, "CityName", false, "CITY_NAME");
        public final static Property ZoneName = new Property(10, String.class, "ZoneName", false, "ZONE_NAME");
        public final static Property CountyName = new Property(11, String.class, "CountyName", false, "COUNTY_NAME");
        public final static Property Address = new Property(12, String.class, "Address", false, "ADDRESS");
        public final static Property GroupName = new Property(13, String.class, "GroupName", false, "GROUP_NAME");
        public final static Property LastUpdateTime = new Property(14, String.class, "LastUpdateTime", false, "LAST_UPDATE_TIME");
        public final static Property LastMessage = new Property(15, String.class, "LastMessage", false, "LAST_MESSAGE");
        public final static Property UnreadMessageCount = new Property(16, String.class, "UnreadMessageCount", false, "UNREAD_MESSAGE_COUNT");
        public final static Property MarkName = new Property(17, String.class, "MarkName", false, "MARK_NAME");
        public final static Property UnitName = new Property(18, String.class, "UnitName", false, "UNIT_NAME");
        public final static Property UnitMarkName = new Property(19, String.class, "UnitMarkName", false, "UNIT_MARK_NAME");
        public final static Property UnitType = new Property(20, String.class, "UnitType", false, "UNIT_TYPE");
        public final static Property UnitId = new Property(21, String.class, "UnitId", false, "UNIT_ID");
        public final static Property RoleType = new Property(22, int.class, "RoleType", false, "ROLE_TYPE");
        public final static Property Shops = new Property(23, String.class, "Shops", false, "SHOPS");
        public final static Property MySelfUserId = new Property(24, String.class, "MySelfUserId", false, "MY_SELF_USER_ID");
        public final static Property Phone = new Property(25, String.class, "Phone", false, "PHONE");
        public final static Property RoleName = new Property(26, String.class, "RoleName", false, "ROLE_NAME");
    }


    public HomeSingleListBeanDao(DaoConfig config) {
        super(config);
    }
    
    public HomeSingleListBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HOME_SINGLE_LIST_BEAN\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"USER_ID\" TEXT," + // 1: UserId
                "\"USER_NAME\" TEXT," + // 2: UserName
                "\"TXHCODE\" TEXT," + // 3: TXHCode
                "\"IMAGES_HEAD\" TEXT," + // 4: ImagesHead
                "\"IS_FRIEND\" TEXT," + // 5: IsFriend
                "\"IS_SHOP\" TEXT," + // 6: IsShop
                "\"ICON\" TEXT," + // 7: Icon
                "\"PROVINCE_NAME\" TEXT," + // 8: ProvinceName
                "\"CITY_NAME\" TEXT," + // 9: CityName
                "\"ZONE_NAME\" TEXT," + // 10: ZoneName
                "\"COUNTY_NAME\" TEXT," + // 11: CountyName
                "\"ADDRESS\" TEXT," + // 12: Address
                "\"GROUP_NAME\" TEXT," + // 13: GroupName
                "\"LAST_UPDATE_TIME\" TEXT," + // 14: LastUpdateTime
                "\"LAST_MESSAGE\" TEXT," + // 15: LastMessage
                "\"UNREAD_MESSAGE_COUNT\" TEXT," + // 16: UnreadMessageCount
                "\"MARK_NAME\" TEXT," + // 17: MarkName
                "\"UNIT_NAME\" TEXT," + // 18: UnitName
                "\"UNIT_MARK_NAME\" TEXT," + // 19: UnitMarkName
                "\"UNIT_TYPE\" TEXT," + // 20: UnitType
                "\"UNIT_ID\" TEXT," + // 21: UnitId
                "\"ROLE_TYPE\" INTEGER NOT NULL ," + // 22: RoleType
                "\"SHOPS\" TEXT," + // 23: Shops
                "\"MY_SELF_USER_ID\" TEXT," + // 24: MySelfUserId
                "\"PHONE\" TEXT," + // 25: Phone
                "\"ROLE_NAME\" TEXT);"); // 26: RoleName
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HOME_SINGLE_LIST_BEAN\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HomeSingleListBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindString(2, UserId);
        }
 
        String UserName = entity.getUserName();
        if (UserName != null) {
            stmt.bindString(3, UserName);
        }
 
        String TXHCode = entity.getTXHCode();
        if (TXHCode != null) {
            stmt.bindString(4, TXHCode);
        }
 
        String ImagesHead = entity.getImagesHead();
        if (ImagesHead != null) {
            stmt.bindString(5, ImagesHead);
        }
 
        String IsFriend = entity.getIsFriend();
        if (IsFriend != null) {
            stmt.bindString(6, IsFriend);
        }
 
        String IsShop = entity.getIsShop();
        if (IsShop != null) {
            stmt.bindString(7, IsShop);
        }
 
        String Icon = entity.getIcon();
        if (Icon != null) {
            stmt.bindString(8, Icon);
        }
 
        String ProvinceName = entity.getProvinceName();
        if (ProvinceName != null) {
            stmt.bindString(9, ProvinceName);
        }
 
        String CityName = entity.getCityName();
        if (CityName != null) {
            stmt.bindString(10, CityName);
        }
 
        String ZoneName = entity.getZoneName();
        if (ZoneName != null) {
            stmt.bindString(11, ZoneName);
        }
 
        String CountyName = entity.getCountyName();
        if (CountyName != null) {
            stmt.bindString(12, CountyName);
        }
 
        String Address = entity.getAddress();
        if (Address != null) {
            stmt.bindString(13, Address);
        }
 
        String GroupName = entity.getGroupName();
        if (GroupName != null) {
            stmt.bindString(14, GroupName);
        }
 
        String LastUpdateTime = entity.getLastUpdateTime();
        if (LastUpdateTime != null) {
            stmt.bindString(15, LastUpdateTime);
        }
 
        String LastMessage = entity.getLastMessage();
        if (LastMessage != null) {
            stmt.bindString(16, LastMessage);
        }
 
        String UnreadMessageCount = entity.getUnreadMessageCount();
        if (UnreadMessageCount != null) {
            stmt.bindString(17, UnreadMessageCount);
        }
 
        String MarkName = entity.getMarkName();
        if (MarkName != null) {
            stmt.bindString(18, MarkName);
        }
 
        String UnitName = entity.getUnitName();
        if (UnitName != null) {
            stmt.bindString(19, UnitName);
        }
 
        String UnitMarkName = entity.getUnitMarkName();
        if (UnitMarkName != null) {
            stmt.bindString(20, UnitMarkName);
        }
 
        String UnitType = entity.getUnitType();
        if (UnitType != null) {
            stmt.bindString(21, UnitType);
        }
 
        String UnitId = entity.getUnitId();
        if (UnitId != null) {
            stmt.bindString(22, UnitId);
        }
        stmt.bindLong(23, entity.getRoleType());
 
        String Shops = entity.getShops();
        if (Shops != null) {
            stmt.bindString(24, Shops);
        }
 
        String MySelfUserId = entity.getMySelfUserId();
        if (MySelfUserId != null) {
            stmt.bindString(25, MySelfUserId);
        }
 
        String Phone = entity.getPhone();
        if (Phone != null) {
            stmt.bindString(26, Phone);
        }
 
        String RoleName = entity.getRoleName();
        if (RoleName != null) {
            stmt.bindString(27, RoleName);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HomeSingleListBean entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String UserId = entity.getUserId();
        if (UserId != null) {
            stmt.bindString(2, UserId);
        }
 
        String UserName = entity.getUserName();
        if (UserName != null) {
            stmt.bindString(3, UserName);
        }
 
        String TXHCode = entity.getTXHCode();
        if (TXHCode != null) {
            stmt.bindString(4, TXHCode);
        }
 
        String ImagesHead = entity.getImagesHead();
        if (ImagesHead != null) {
            stmt.bindString(5, ImagesHead);
        }
 
        String IsFriend = entity.getIsFriend();
        if (IsFriend != null) {
            stmt.bindString(6, IsFriend);
        }
 
        String IsShop = entity.getIsShop();
        if (IsShop != null) {
            stmt.bindString(7, IsShop);
        }
 
        String Icon = entity.getIcon();
        if (Icon != null) {
            stmt.bindString(8, Icon);
        }
 
        String ProvinceName = entity.getProvinceName();
        if (ProvinceName != null) {
            stmt.bindString(9, ProvinceName);
        }
 
        String CityName = entity.getCityName();
        if (CityName != null) {
            stmt.bindString(10, CityName);
        }
 
        String ZoneName = entity.getZoneName();
        if (ZoneName != null) {
            stmt.bindString(11, ZoneName);
        }
 
        String CountyName = entity.getCountyName();
        if (CountyName != null) {
            stmt.bindString(12, CountyName);
        }
 
        String Address = entity.getAddress();
        if (Address != null) {
            stmt.bindString(13, Address);
        }
 
        String GroupName = entity.getGroupName();
        if (GroupName != null) {
            stmt.bindString(14, GroupName);
        }
 
        String LastUpdateTime = entity.getLastUpdateTime();
        if (LastUpdateTime != null) {
            stmt.bindString(15, LastUpdateTime);
        }
 
        String LastMessage = entity.getLastMessage();
        if (LastMessage != null) {
            stmt.bindString(16, LastMessage);
        }
 
        String UnreadMessageCount = entity.getUnreadMessageCount();
        if (UnreadMessageCount != null) {
            stmt.bindString(17, UnreadMessageCount);
        }
 
        String MarkName = entity.getMarkName();
        if (MarkName != null) {
            stmt.bindString(18, MarkName);
        }
 
        String UnitName = entity.getUnitName();
        if (UnitName != null) {
            stmt.bindString(19, UnitName);
        }
 
        String UnitMarkName = entity.getUnitMarkName();
        if (UnitMarkName != null) {
            stmt.bindString(20, UnitMarkName);
        }
 
        String UnitType = entity.getUnitType();
        if (UnitType != null) {
            stmt.bindString(21, UnitType);
        }
 
        String UnitId = entity.getUnitId();
        if (UnitId != null) {
            stmt.bindString(22, UnitId);
        }
        stmt.bindLong(23, entity.getRoleType());
 
        String Shops = entity.getShops();
        if (Shops != null) {
            stmt.bindString(24, Shops);
        }
 
        String MySelfUserId = entity.getMySelfUserId();
        if (MySelfUserId != null) {
            stmt.bindString(25, MySelfUserId);
        }
 
        String Phone = entity.getPhone();
        if (Phone != null) {
            stmt.bindString(26, Phone);
        }
 
        String RoleName = entity.getRoleName();
        if (RoleName != null) {
            stmt.bindString(27, RoleName);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public HomeSingleListBean readEntity(Cursor cursor, int offset) {
        HomeSingleListBean entity = new HomeSingleListBean( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // UserId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // UserName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // TXHCode
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // ImagesHead
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // IsFriend
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // IsShop
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // Icon
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // ProvinceName
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // CityName
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // ZoneName
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // CountyName
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // Address
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // GroupName
            cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14), // LastUpdateTime
            cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15), // LastMessage
            cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16), // UnreadMessageCount
            cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17), // MarkName
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // UnitName
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // UnitMarkName
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // UnitType
            cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21), // UnitId
            cursor.getInt(offset + 22), // RoleType
            cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23), // Shops
            cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24), // MySelfUserId
            cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25), // Phone
            cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26) // RoleName
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HomeSingleListBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUserId(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setUserName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTXHCode(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImagesHead(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIsFriend(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setIsShop(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setIcon(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setProvinceName(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setCityName(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setZoneName(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setCountyName(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setAddress(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setGroupName(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setLastUpdateTime(cursor.isNull(offset + 14) ? null : cursor.getString(offset + 14));
        entity.setLastMessage(cursor.isNull(offset + 15) ? null : cursor.getString(offset + 15));
        entity.setUnreadMessageCount(cursor.isNull(offset + 16) ? null : cursor.getString(offset + 16));
        entity.setMarkName(cursor.isNull(offset + 17) ? null : cursor.getString(offset + 17));
        entity.setUnitName(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setUnitMarkName(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setUnitType(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setUnitId(cursor.isNull(offset + 21) ? null : cursor.getString(offset + 21));
        entity.setRoleType(cursor.getInt(offset + 22));
        entity.setShops(cursor.isNull(offset + 23) ? null : cursor.getString(offset + 23));
        entity.setMySelfUserId(cursor.isNull(offset + 24) ? null : cursor.getString(offset + 24));
        entity.setPhone(cursor.isNull(offset + 25) ? null : cursor.getString(offset + 25));
        entity.setRoleName(cursor.isNull(offset + 26) ? null : cursor.getString(offset + 26));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(HomeSingleListBean entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(HomeSingleListBean entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HomeSingleListBean entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
