package com.txh.im.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.txh.im.greendao.DaoMaster;

/**
 * Created by jiajia on 2017/3/17.
 */

public class DBManager {
    private final static String dbName = "single_history";
    private static DBManager mInstance;
    private Context mContext;
    private DaoMaster.DevOpenHelper openHelper;
    public DBManager(Context context) {
        this.mContext = context;
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
    }
    /**
     * 初始化,推荐在application中初始化
     *
     * @param myApp
     */
    public static void init(Context myApp) {
        mInstance = new DBManager(myApp);
    }
    /**
     * 获取单例引用
     *
     * @param
     * @return
     */
    public static DBManager getInstance() {
        return mInstance;
    }
    /**
     * 获取可读数据库
     */
    public SQLiteDatabase getReadableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        }
        SQLiteDatabase db = openHelper.getReadableDatabase();
        return db;
    }
    /**
     * 获取可写数据库
     */
    public SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(mContext, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }
}
