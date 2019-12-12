package com.example.commlib.utils;


import androidx.lifecycle.AndroidViewModel;

import com.example.commlib.base.mvvm.BaseViewModel;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;


public class ClassUtil {

    /**
     * 获取泛型ViewModel的class对象
     *
     * 如果 直接传进来的就是BaseViewModel，则不创建
     */
    public static <T> Class<T> getViewModel(Object obj) {
        Class<?> currentClass = obj.getClass();
        Class<T> tClass = getGenericClass(currentClass, AndroidViewModel.class);
//        if (tClass == null || tClass == AndroidViewModel.class || tClass == NoViewModel.class) {
      if (tClass == null || tClass == BaseViewModel.class) { //如果 直接传进来的就是BaseViewModel，则不创建
            return null;
        }
        return tClass;
    }

    private static <T> Class<T> getGenericClass(Class<?> klass, Class<?> filterClass) {
        Type type = klass.getGenericSuperclass();
        if (type == null || !(type instanceof ParameterizedType)) return null;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        for (Type t : types) {
            Class<T> tClass = (Class<T>) t;
            if (filterClass.isAssignableFrom(tClass)) {
                return tClass;
            }
        }
        return null;
    }
}
