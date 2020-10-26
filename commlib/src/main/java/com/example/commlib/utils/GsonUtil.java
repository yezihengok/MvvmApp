package com.example.commlib.utils;

import android.text.TextUtils;

import com.blankj.ALog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gson 工具类
 */
public class GsonUtil {
    private static String TAG = GsonUtil.class.getSimpleName();
    private static Gson gson = new Gson();

    /**
     * 把一个map变成json字符串
     *
     * @param map
     * @return
     */
    public static String parseMapToJson(Map<?, ?> map) {
        try {
            return gson.toJson(map);
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 把一个json字符串变成对象
     *
     * @param json
     * @param cls
     * @return
     */
    public static <T> T parseJsonToBean(String json, Class<T> cls) {
        T t = null;
        try {
            t = gson.fromJson(json, cls);
        } catch (Exception e) {

        }
        return t;
    }

    /**
     * 把json字符串变成map
     *
     * @param json
     * @return
     */
    public static HashMap<String, Object> parseJsonToMap(String json) {
        Type type = new TypeToken<HashMap<String, Object>>() {
        }.getType();
        HashMap<String, Object> map = null;
        try {
            map = gson.fromJson(json, type);
        } catch (Exception e) {
            ALog.v(TAG, "转换异常" + e.getMessage());

        }
        return map;
    }

    /**
     * 把json字符串变成集合
     * params: new TypeToken<List<yourbean>>(){}.getType(),
     *
     * @param json
     * @param type new TypeToken<List<yourbean>>(){}.getType()
     * @return
     */
    public static List<?> parseJsonToList(String json, Type type) {
        List<?> list = gson.fromJson(json, type);
        return list;
    }

    /**
     * 把  list  转成  Strin   json
     *
     * @param list
     * @return
     */
    public static String parseListToJson(List<?> list) {
        String json = gson.toJson(list);
        return json;
    }

    /**
     * 获取json串中某个字段的值，注意，只能获取同一层级的value
     *
     * @param json
     * @param key
     * @return
     */
    public static String getFieldValue(String json, String key) {
        if (TextUtils.isEmpty(json))
            return null;
        if (!json.contains(key))
            return "";
        JSONObject jsonObject = null;
        String value = null;
        try {
            jsonObject = new JSONObject(json);
            value = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     * 格式化json
     *
     * @param uglyJSONString
     * @return
     */
    public static String jsonFormatter(String uglyJSONString) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(uglyJSONString);
        String prettyJsonString = gson.toJson(je);
        return prettyJsonString;
    }


    public static Map<String, String> getJsonToMap(String json) {
        return gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());
    }

    /**
     * 通过  bean    返回 json
     *
     * @param bean
     * @return
     */
    public static String getBeanToJson(Object bean) {
        Gson gson = new Gson();
        return gson.toJson(bean);
    }


    //----------------------手动解析json类---------------------------
    public static JSONArray getJSONArray(JSONObject object, String name) {
        if (object.isNull(name)) {
            ALog.e("没有找到节点：" + name);
            return null;
        }
        try {
            return object.getJSONArray(name);
        } catch (JSONException e) {
            //Log.e("", e.getMessage());
            return null;
        }
    }

    /**
     * 加工json节点的数据，如果有节点则取节点数据，如果没有节点则返回空字符串
     * @param object
     * @param name
     * @return
     */
    public static String getString(JSONObject object, String name) {
        if(object==null){
            return "";
        }
        if (object.isNull(name)) {
            //CommUtils.logD("没有找到节点：" + name);
            return "";
        }
        try {
            return object.getString(name);
        } catch (JSONException e) {
            //Log.e("", e.getMessage());
            return "";
        }
    }

    public static String getDoubleString(JSONObject object, String name) {
        if (object.isNull(name)) {
            //Log.e("", "没有找到节点："+name);
            return "0";
        }
        try {
            return object.getString(name);
        } catch (JSONException e) {
            //Log.e("", e.getMessage());
            return "0";
        }
    }


    /**
     * 获取json节点的数据并换行为Long型，否则返回0
     * @param object
     * @param name
     * @return
     */
    public static double getDouble(JSONObject object, String name) {
        if (object.isNull(name)) {
            return 0;
        }
        try {
            return object.getDouble(name);
        } catch (JSONException e) {
            return 0;
        }
    }


    public static int getInt(JSONObject object, String name) {
        if (object.isNull(name)) {
            return 0;
        }
        try {
            return object.getInt(name);
        } catch (JSONException e) {
            return 0;
        }
    }
    /**
     * <获取json节点的数据并换行为int型，否则返回0>
     *
     */
    public static String getIntString(JSONObject object, String name) {
        if (object.isNull(name)) {
            return "0";
        }
        try {
            return object.getString(name);
        } catch (JSONException e) {
            return "0";
        }
    }
}
