package com.example.mvvmapp.db;

import com.example.mvvmapp.bean.ArticleBean;
import com.example.mvvmapp.bean.TestBean;
import com.primary.greendao.gen.ArticleBeanDao;
import com.primary.greendao.gen.TestBeanDao;

/**
 * 存放DaoUtils
 * Created by yzh on 2020/6/10 11:05.
 */
public class DaoUtilsStore {
    private volatile static DaoUtilsStore instance;
    public static DaoUtilsStore getInstance() {
        if (instance == null) {
            synchronized (DaoUtilsStore.class) {
                if (instance == null) {
                    instance = new DaoUtilsStore();
                }
            }
        }
        return instance;
    }

    private DaoManager mManager;
    private DaoUtilsStore() {
        mManager = DaoManager.getInstance();
    }


    private CommonDao<ArticleBean> mArticleBeanUtil;
    private CommonDao<TestBean> mTestBeanUtil;

    public CommonDao<ArticleBean> getArticleBeanUtil() {
        if (mArticleBeanUtil == null) {
            ArticleBeanDao articleBeanDao = mManager.getDaoSession().getArticleBeanDao();
            mArticleBeanUtil = new CommonDao<>(ArticleBean.class, articleBeanDao);
        }
        return mArticleBeanUtil;
    }

    public CommonDao<TestBean> getTestBeanUtil() {
        if (mTestBeanUtil == null) {
            TestBeanDao testBeanDao = mManager.getDaoSession().getTestBeanDao();
            mTestBeanUtil = new CommonDao<>(TestBean.class, testBeanDao);
        }
        return mTestBeanUtil;
    }



}

