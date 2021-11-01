package com.example.commlib.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.commlib.api.App;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * 手机缓存
 */
public class SPHelper {
    volatile static String spName="SPHelper";
    private static final String SP_PLUGIN_PREFIX = "plugin_";

    public static SharedPreferences getSP() {
        return App.getInstance().getSharedPreferences(spName, Context.MODE_PRIVATE);
    }
    private static SharedPreferences.Editor getEditor() {
        return getSP().edit();
    }


    /**
     * 清除用户账号信息
     */
    public static void clearAppInfo() {
        getEditor().clear().apply();
    }



    //--------------------------------------保存纯数据类型与业务无关-----------------------------------------------------
//    private SharedPreferences sp;
//    private volatile static SPHelper instance;
    private static final Gson gson = new Gson();

    public static void putString(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static String getString(String key) {
        return getSP().getString(key, "");
    }

    public static String getString(String key, String defaultValue) {
        return getSP().getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public static int getInt(String key) {
        return getSP().getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return getSP().getInt(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    public static long getLong(String key) {
        return getSP().getLong(key, 0L);
    }

    public static long getLong(String key, long defaultValue) {
        return getSP().getLong(key, defaultValue);
    }

    public static void putFloat(String key, float value) {
        getEditor().putFloat(key, value).apply();
    }

    public static float getFloat(String key) {
        return getSP().getFloat(key, 0f);
    }

    public static float getFloat(String key, float defaultValue) {
        return getSP().getFloat(key, defaultValue);
    }

    public static void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static boolean getBoolean(String key) {
        return getSP().getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return getSP().getBoolean(key, defaultValue);
    }

    public static void putObj(String key, Object obj) {
        String value = gson.toJson(obj);
        putString(key, value);
    }

    /**
     * 非泛型对象反序列化
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return 反序列化失败或未保存过时为空
     */
    public static <T> T getObj(String key, Class<T> clazz) {
        String value = getString(key);
        T t = null;
        try {
            t = gson.fromJson(value, clazz);
        } catch (JsonSyntaxException exc) {
            Log.e("SPHelper", "parse obj failed,reason:" + exc.getMessage());
        }
        return t;
    }
    //--------------------------------------保存纯数据类型与业务无关-----------------------------------------------------
}
