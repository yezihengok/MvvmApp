package com.example.mvvmapp.bean;

import androidx.annotation.NonNull;

import com.example.commlib.utils.GsonUtil;


/**
 * Created by Android Studio.
 * 以继承方式  方便拓展
 * @author zjx
 * @date 2020/4/27
 */
public class BaseBean {
    @NonNull
    @Override
    public String toString() {
        return GsonUtil.getBeanToJson(this);
    }
}
