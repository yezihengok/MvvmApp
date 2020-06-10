package com.example.mvvmapp.db.connverter;

import com.example.mvvmapp.bean.ParagrapBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yzh on 2020/6/9 14:02.
 */
public class ParagrapBeanConvent implements PropertyConverter<List<ParagrapBean>,String> {
    private final Gson mGson;

    public ParagrapBeanConvent() {
        mGson = new Gson();
    }
    @Override
    public List<ParagrapBean> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArrayList<ParagrapBean>>() {
        }.getType();
        ArrayList<ParagrapBean> itemList= mGson.fromJson(databaseValue, type);
        return itemList;

    }

    @Override
    public String convertToDatabaseValue(List<ParagrapBean> entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }
}
