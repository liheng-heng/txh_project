package com.txh.im.dao;

import com.txh.im.bean.SearchHistoryBean;
import com.txh.im.db.DBManager;
import com.txh.im.greendao.DaoMaster;
import com.txh.im.greendao.SearchHistoryBeanDao;

import java.util.List;

/**
 * Created by jiajia on 2017/3/17.
 */

public class SearchHistoryStringDao {
    private DaoMaster daoMaster;

    private SearchHistoryStringDao() {
        daoMaster = new DaoMaster(DBManager.getInstance().getWritableDatabase());
    }

    private static SearchHistoryStringDao instance;

    public static SearchHistoryStringDao getInstance() {
        synchronized (SearchHistoryStringDao.class) {
            if (instance == null) {
                instance = new SearchHistoryStringDao();
            }
        }
        return instance;
    }

    /**
     * 获取所有的搜索记录
     */
    public List<SearchHistoryBean> getHistory() {
        List<SearchHistoryBean> list = daoMaster.newSession()
                .getSearchHistoryBeanDao()
                .queryBuilder()
                .orderDesc(SearchHistoryBeanDao.Properties.Id)
                .build()
                .list();
        return list;
    }

    /**
     * 增加或更新一条搜索记录
     *
     * @param value 增加或更新一条搜索记录
     */
    public void addHistory(SearchHistoryBean value) {
        daoMaster.newSession()
                .getSearchHistoryBeanDao()
                .queryBuilder()
                .where(SearchHistoryBeanDao.Properties.Username.eq(value.getUsername()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
        daoMaster
                .newSession()
                .getSearchHistoryBeanDao()
                .insert(value);
    }

    /**
     * 清空所有历史记录
     */
    public void clear() {
        daoMaster.newSession()
                .getSearchHistoryBeanDao()
                .deleteAll();
    }

    /**
     * 删除某条历史记录
     *
     * @param value 要删除的历史记录
     */
    public void delete(SearchHistoryBean value) {
        daoMaster.newSession()
                .getSearchHistoryBeanDao()
                .queryBuilder()
                .where(SearchHistoryBeanDao.Properties.Id.eq(value.getId()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }
}
