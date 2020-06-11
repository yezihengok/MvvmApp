package com.example.mvvmapp.db.upgrade;



import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import com.primary.greendao.gen.DaoMaster;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 数据库迁移
 */
public class DBMigrationHelper {

    private static DBMigrationHelper instance;

    public static DBMigrationHelper getInstance() {
        if (instance == null) {
            instance = new DBMigrationHelper();
        }
        return instance;
    }

    /**
     * 获取列的名字
     *
     * @param db
     * @param tableName
     * @return
     */
    private static List<String> getColumns(Database db, String tableName) {
        List<String> columns = new ArrayList<>();
        Cursor cursor  = null;
        try {
            cursor = db.rawQuery("SELECT * FROM " + tableName + " limit 1", null);
            if (cursor != null) {
                columns = new ArrayList<>(Arrays.asList(cursor.getColumnNames()));
            }
        } catch (Exception e) {
            Log.e(tableName, e.getMessage(), e);
            e.printStackTrace();
        }
        finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return columns;
    }


    /**
     * 数据库迁移
     *
     * @param db
     * @param daoClasses
     */
    public void migrate(Database db, Class... daoClasses) {
        //生成临时表，复制表数据
        generateTempTables(db, daoClasses);
        DaoMaster.dropAllTables(db, true);
        DaoMaster.createAllTables(db, false);
        //恢复数据
        restoreData(db, daoClasses);
    }

    /**
     * 生成临时表，复制表数据
     *
     * @param db
     * @param daoClasses
     */
    private void generateTempTables(Database db, Class... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig     = new DaoConfig(db, daoClasses[i]);
            String    divider       = "";
            String    tableName     = daoConfig.tablename;
            String    tempTableName = daoConfig.tablename.concat("_TEMP");

            ArrayList     properties               = new ArrayList<>();
            StringBuilder createTableStringBuilder = new StringBuilder();

            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (");
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tableName).contains(columnName)) {
                    properties.add(columnName);
                    String type = null;
                    try {
                        type = getTypeByClass(daoConfig.properties[j].type);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    createTableStringBuilder.append(divider).append(columnName).append(" ").append(type);
                    if (daoConfig.properties[j].primaryKey) {
                        createTableStringBuilder.append(" PRIMARY KEY");
                    }
                    divider = ",";
                }
            }

            createTableStringBuilder.append(");");
            db.execSQL(createTableStringBuilder.toString());
            Log.e("DBMigrationHelper", "generateTempTables: sql:" + createTableStringBuilder.toString());

            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(" FROM ").append(tableName).append(";");

            db.execSQL(insertTableStringBuilder.toString());
            Log.e("DBMigrationHelper", "generateTempTables: sql:" + insertTableStringBuilder.toString());
        }
    }

    /**
     * 恢复数据
     *
     * @param db
     * @param daoClasses
     */
    private void restoreData(Database db, Class... daoClasses) {
        for (int i = 0; i < daoClasses.length; i++) {
            DaoConfig daoConfig       = new DaoConfig(db, daoClasses[i]);
            String    tableName       = daoConfig.tablename;
            String    tempTableName   = daoConfig.tablename.concat("_TEMP");
            ArrayList properties      = new ArrayList();
            ArrayList propertiesQuery = new ArrayList();
            for (int j = 0; j < daoConfig.properties.length; j++) {
                String columnName = daoConfig.properties[j].columnName;
                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName);
                    propertiesQuery.add(columnName);
                } else {
                    try {
                        if (getTypeByClass(daoConfig.properties[j].type).equals("INTEGER")) {
                            propertiesQuery.add("0 as " + columnName);
                            properties.add(columnName);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            StringBuilder insertTableStringBuilder = new StringBuilder();
            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (");
            insertTableStringBuilder.append(TextUtils.join(",", properties));
            insertTableStringBuilder.append(") SELECT ");
            insertTableStringBuilder.append(TextUtils.join(",", propertiesQuery));
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";");

            StringBuilder dropTableStringBuilder = new StringBuilder();
            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName);

            db.execSQL(insertTableStringBuilder.toString());
            db.execSQL(dropTableStringBuilder.toString());

            Log.e("DBMigrationHelper", "restoreData: sql:" + insertTableStringBuilder.toString());
            Log.e("DBMigrationHelper", "restoreData: sql:" + dropTableStringBuilder.toString());
        }
    }

    /**
     * @param type
     * @return
     * @throws Exception
     */
    private String getTypeByClass(Class type) throws Exception {
        if (type.equals(String.class)) {
            return "TEXT";
        }

        if (type.equals(Long.class) || type.equals(Integer.class) ||
                type.equals(long.class) || type.equals(int.class)) {
            return "INTEGER";
        }

        if (type.equals(Boolean.class) || type.equals(boolean.class)) {
            return "BOOLEAN";
        }

        Exception exception = new Exception("migration helper - class doesn't match with the current parameters".concat(" - Class: ").concat(type.toString()));
        exception.printStackTrace();
        throw exception;
    }
}


