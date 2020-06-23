package com.example.mvvmapp.db.converter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *  * GreenDao实体类默认不支持List，数组和Object类型
 *  * 需要自己定义转换器，将复杂类型转换为基本类型，再存到数据库
 *  * 获取的时候，再将基本类型转换为复杂类型
 * Created by yzh on 2020/6/9 11:46.
 */

public class InterConverter implements PropertyConverter<List<Integer>, String> {

    private final Gson mGson;

    public InterConverter() {
        mGson = new Gson();
    }

    @Override
    public List<Integer> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArrayList<Integer>>() {
        }.getType();
        ArrayList<Integer> list = mGson.fromJson(databaseValue , type);
        return list;
    }

    @Override
    public String convertToDatabaseValue(List<Integer> entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }


}

