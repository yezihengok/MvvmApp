package com.example.mvvmapp.db.connverter;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Arrays;
import java.util.List;

/**
 * GreenDao实体类默认不支持List，数组和Object类型
 * 需要自己定义转换器，将复杂类型转换为基本类型，再存到数据库
 * 获取的时候，再将基本类型转换为复杂类型
 * Created by yzh on 2020/6/9 11:47.
 */

public class StringConverter implements PropertyConverter<List<String> , String> {

    private static final String SPIT="#spit#";
    @Override
    public List<String> convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        else {
            List<String> list = Arrays.asList(databaseValue.split(SPIT));
            return list;
        }
    }

    @Override
    public String convertToDatabaseValue(List<String> entityProperty) {
        if(entityProperty==null){
            return null;
        }
        else{
            StringBuilder sb= new StringBuilder();
            for(String link:entityProperty){
                sb.append(link);
                sb.append(SPIT);
            }
            return sb.toString();
        }
    }
}

