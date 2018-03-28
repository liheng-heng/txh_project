package com.txh.im.dao;

import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.db.DBManager;
import com.txh.im.greendao.DaoMaster;
import com.txh.im.greendao.HomeSingleListBeanDao;

import java.util.List;

/**
 * Created by jiajia on 2017/3/17.
 */

public class SearchHistoryDao {
    private DaoMaster daoMaster;
    private SearchHistoryDao() {
        daoMaster = new DaoMaster(DBManager.getInstance().getWritableDatabase());
    }
    private static SearchHistoryDao instance;
    public static SearchHistoryDao getInstance() {
        synchronized (SearchHistoryDao.class) {
            if (instance == null) {
                instance = new SearchHistoryDao();
            }
        }
        return instance;
    }
    /**
     * 获取所有的搜索记录
     */
    public List<HomeSingleListBean> getHistory() {
        List<HomeSingleListBean> list = daoMaster.newSession()
                .getHomeSingleListBeanDao()
                .queryBuilder()
                .orderDesc(HomeSingleListBeanDao.Properties.Id)
                .build()
                .list();
        return list;
    }
    /**
     * 增加或更新一条搜索记录
     *
     * @param value 增加或更新一条搜索记录
     */
    public void addHistory(HomeSingleListBean value) {
        daoMaster.newSession()
                .getHomeSingleListBeanDao()
                .queryBuilder()
                .where(HomeSingleListBeanDao.Properties.UserId.eq(value.getUserId()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
//        HomeSingleListBean info = value;
        daoMaster
                .newSession()
                .getHomeSingleListBeanDao()
                .insert(value);
    }
    /**
     * 清空所有历史记录
     */
    public void clear() {
        daoMaster.newSession()
                .getHomeSingleListBeanDao()
                .deleteAll();
    }
    /**
     * 删除某条历史记录
     *
     * @param value 要删除的历史记录
     */
    public void delete(HomeSingleListBean value) {
        daoMaster.newSession()
                .getHomeSingleListBeanDao()
                .queryBuilder()
                .where(HomeSingleListBeanDao.Properties.UserId.eq(value.getUserId()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }
}
