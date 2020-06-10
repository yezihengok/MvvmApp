package com.example.mvvmapp.db.connverter;

import com.example.mvvmapp.bean.ArticleBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;

/**
 * Created by yzh on 2020/6/9 14:16.
 */
public class MaterialReleaseInfoConverter implements PropertyConverter<ArticleBean.MaterialReleaseInfo, String> {
    private final Gson mGson;

    public MaterialReleaseInfoConverter() {
        mGson = new Gson();
    }
    @Override
    public ArticleBean.MaterialReleaseInfo convertToEntityProperty(String databaseValue) {
        Type type = new TypeToken<ArticleBean.MaterialReleaseInfo>() {
        }.getType();
        ArticleBean.MaterialReleaseInfo itemList= mGson.fromJson(databaseValue, type);
        return itemList;

    }

    @Override
    public String convertToDatabaseValue(ArticleBean.MaterialReleaseInfo entityProperty) {
        String dbString = mGson.toJson(entityProperty);
        return dbString;
    }
}
