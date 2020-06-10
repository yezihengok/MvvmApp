package com.example.mvvmapp.db.connverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CommBeanConvent<T>  implements PropertyConverter<List<T>,String> {
    private final Gson mGson;

    public CommBeanConvent() {
        mGson = new Gson();
    }
    @Override
    public List<T> convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArrayList<T>>() {
        }.getType();
        ArrayList<T> itemList= mGson.fromJson(databaseValue, type);
        return itemList;

    }

    @Override
    public String convertToDatabaseValue(List<T> entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }
}
