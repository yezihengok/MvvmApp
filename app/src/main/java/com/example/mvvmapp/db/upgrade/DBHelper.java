package com.example.mvvmapp.db.upgrade;


import android.content.Context;

import com.blankj.ALog;
import com.primary.greendao.gen.ArticleBeanDao;
import com.primary.greendao.gen.DaoMaster;
import com.primary.greendao.gen.TestBeanDao;

import org.greenrobot.greendao.database.Database;

/**
 * 数据库Helper类,主要用来数据库升级
 * Created by yzh on 2020/6/10 18:57.
 */
public class DBHelper extends DaoMaster.DevOpenHelper {

    public DBHelper(Context context, String name) {
        super(context, name);
    }

//    注：数据库升级不可使用GreenDao默认生成的DaoMaster.DevOpenHelper，因为DaoMaster.DevOpenHelper在数据库升级时会删除数据库再重新创建。
//
//    因此，我们需要继承DaoMaster.OpenHelper实现自己的MyOpenHelper，在onUpgrade()方法中升级数据库。

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        ALog.a(String.format("onUpgrade---oldVersion:%s newVersion%s",oldVersion,newVersion));
        //  需要进行数据迁移更新的实体类Dao ，新增的不用加
        DBMigrationHelper.getInstance().migrate(db, ArticleBeanDao.class, TestBeanDao.class);
    }

}
