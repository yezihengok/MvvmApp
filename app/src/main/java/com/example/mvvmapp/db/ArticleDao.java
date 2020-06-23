package com.example.mvvmapp.db;

import com.blankj.ALog;
import com.example.commlib.api.App;
import com.example.mvvmapp.bean.ArticleBean;
import com.primary.greendao.gen.ArticleBeanDao;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by yzh on 2020/6/9 15:18.
 */
public class ArticleDao {
    private static final String TAG = ArticleDao.class.getSimpleName();

    private DaoManager mManager;
    public ArticleDao() {
        mManager = DaoManager.getInstance();
        mManager.init(App.getInstance());
    }

    /**
     * 完成ArticleBean记录的插入，如果表未创建，先创建ArticleBean表
     * @param articleBean
     * @return
     */
    public boolean insertArticleBean(ArticleBean articleBean){
        boolean flag = false;
        flag = mManager.getDaoSession().getArticleBeanDao().insert(articleBean) != -1;
        ALog.i(TAG, "insert ArticleBean :" + flag + "-->" + articleBean.toString());
        return flag;
    }


    /**
     * 插入多条数据，在子线程操作
     * @param articleBeanList
     * @return
     */
    public boolean insertMultArticleBean(final List<ArticleBean> articleBeanList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (ArticleBean ArticleBean : articleBeanList) {
                        mManager.getDaoSession().insertOrReplace(ArticleBean);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     * @param articleBean
     * @return
     */
    public boolean updateArticleBean(ArticleBean articleBean){
        boolean flag = false;
        try {
            mManager.getDaoSession().update(articleBean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     * @param articleBean
     * @return
     */
    public boolean deleteArticleBean(ArticleBean articleBean){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(articleBean);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     * @return
     */
    public boolean deleteAll(){
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(ArticleBean.class);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     * @return
     */
    public List<ArticleBean> queryAllArticleBean(){
        return mManager.getDaoSession().loadAll(ArticleBean.class);
    }

    /**
     * 根据主键id查询记录
     * @param key
     * @return
     */
    public ArticleBean queryArticleBeanById(long key){
        return mManager.getDaoSession().load(ArticleBean.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<ArticleBean> queryArticleBeanByNativeSql(String sql, String[] conditions){
        return mManager.getDaoSession().queryRaw(ArticleBean.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     * @return
     */
    public List<ArticleBean> queryArticleBeanByQueryBuilder(long id){
        QueryBuilder<ArticleBean> queryBuilder = mManager.getDaoSession().queryBuilder(ArticleBean.class);
        return queryBuilder.where(ArticleBeanDao.Properties._id.eq(id)).list();
//        return queryBuilder.where(ArticleBeanDao.Properties._id.ge(id)).list();
    }
}
