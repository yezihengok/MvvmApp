package com.example.mvvmapp.db;

import android.util.Log;

import com.primary.greendao.gen.DaoSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

/**
 * 公用DaoUtil ,避免使用一个对象就创建一个DaoUtil
 * Created by yzh on 2020/6/10 11:03.
 */
public class CommonDao<T> {
    private static final String TAG = CommonDao.class.getSimpleName();

    private DaoSession daoSession;
    private Class<T> entityClass;
    private AbstractDao<T, Long> entityDao;

    public CommonDao(Class<T> pEntityClass, AbstractDao<T, Long> pEntityDao) {
        DaoManager mManager = DaoManager.getInstance();
        daoSession = mManager.getDaoSession();
        entityClass = pEntityClass;
        entityDao = pEntityDao;
    }

    /**
     * 插入记录，如果表未创建，先创建表
     *
     * @param pEntity
     * @return
     */
    public boolean insert(T pEntity) {
        boolean flag = entityDao.insert(pEntity) != -1;
        Log.i(TAG, "insert Meizi :" + flag + "-->" + pEntity.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     *
     * @param pEntityList
     * @return
     */
    public boolean insertMulti(final List<T> pEntityList) {
        try {
            daoSession.runInTx(new Runnable() {
                @Override
                public void run() {
                    for (T meizi : pEntityList) {
                        daoSession.insertOrReplace(meizi);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 修改一条数据
     *
     * @param pEntity
     * @return
     */
    public boolean update(T pEntity) {
        try {
            daoSession.update(pEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除单条记录
     *
     * @param pEntity
     * @return
     */
    public boolean delete(T pEntity) {
        try {
            //按照id删除
            daoSession.delete(pEntity);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public boolean deleteAll() {
        try {
            //按照id删除
            daoSession.deleteAll(entityClass);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<T> queryAll() {
        return daoSession.loadAll(entityClass);
    }

    /**
     * 根据主键_id查询记录
     *
     * @param key
     * @return
     */
    public T queryById(long key) {
        return daoSession.load(entityClass, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<T> queryByNativeSql(String sql, String[] conditions) {
        return daoSession.queryRaw(entityClass, sql, conditions);
    }

//    public List<T> queryData(String s) {
//        List<Student> students = daoSession.queryRaw(Student.class, " where id = ?", s);
//    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<T> queryByQueryBuilder(WhereCondition cond, WhereCondition... condMore) {
        QueryBuilder<T> queryBuilder = daoSession.queryBuilder(entityClass);
        return queryBuilder.where(cond, condMore).list();
    }
}
