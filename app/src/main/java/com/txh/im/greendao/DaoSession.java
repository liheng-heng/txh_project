package com.txh.im.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.txh.im.bean.HomeSingleListBean;
import com.txh.im.bean.SearchHistoryBean;
import com.txh.im.bean.UpdateAddressBean;

import com.txh.im.greendao.HomeSingleListBeanDao;
import com.txh.im.greendao.SearchHistoryBeanDao;
import com.txh.im.greendao.UpdateAddressBeanDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig homeSingleListBeanDaoConfig;
    private final DaoConfig searchHistoryBeanDaoConfig;
    private final DaoConfig updateAddressBeanDaoConfig;

    private final HomeSingleListBeanDao homeSingleListBeanDao;
    private final SearchHistoryBeanDao searchHistoryBeanDao;
    private final UpdateAddressBeanDao updateAddressBeanDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        homeSingleListBeanDaoConfig = daoConfigMap.get(HomeSingleListBeanDao.class).clone();
        homeSingleListBeanDaoConfig.initIdentityScope(type);

        searchHistoryBeanDaoConfig = daoConfigMap.get(SearchHistoryBeanDao.class).clone();
        searchHistoryBeanDaoConfig.initIdentityScope(type);

        updateAddressBeanDaoConfig = daoConfigMap.get(UpdateAddressBeanDao.class).clone();
        updateAddressBeanDaoConfig.initIdentityScope(type);

        homeSingleListBeanDao = new HomeSingleListBeanDao(homeSingleListBeanDaoConfig, this);
        searchHistoryBeanDao = new SearchHistoryBeanDao(searchHistoryBeanDaoConfig, this);
        updateAddressBeanDao = new UpdateAddressBeanDao(updateAddressBeanDaoConfig, this);

        registerDao(HomeSingleListBean.class, homeSingleListBeanDao);
        registerDao(SearchHistoryBean.class, searchHistoryBeanDao);
        registerDao(UpdateAddressBean.class, updateAddressBeanDao);
    }
    
    public void clear() {
        homeSingleListBeanDaoConfig.clearIdentityScope();
        searchHistoryBeanDaoConfig.clearIdentityScope();
        updateAddressBeanDaoConfig.clearIdentityScope();
    }

    public HomeSingleListBeanDao getHomeSingleListBeanDao() {
        return homeSingleListBeanDao;
    }

    public SearchHistoryBeanDao getSearchHistoryBeanDao() {
        return searchHistoryBeanDao;
    }

    public UpdateAddressBeanDao getUpdateAddressBeanDao() {
        return updateAddressBeanDao;
    }

}