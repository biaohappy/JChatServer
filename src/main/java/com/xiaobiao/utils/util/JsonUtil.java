package com.xiaobiao.utils.util;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhoup on 2016/8/3.
 * json 字符串转换工具类
 */
public class JsonUtil {
    private static Gson gson = null;

    static {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.registerTypeAdapter(Date.class, new DateDeserializer())
                    .setDateFormat("yyyy-MM-dd HH:mm:ss");
            gson = builder.create();
        }
    }

    private JsonUtil() {
    }

    public static <T> T json2Object(String json, Class<T> clz) {
        return gson.fromJson(json, clz);
    }

    public static String obj2JsonString(Object object) {
        return gson.toJson(object);
    }

    public static <T> List<T> gson2List(String json, Class<T> clz) {
        List<T> list = null;
        if (!Strings.isNullOrEmpty(json)) {
            list = gson.fromJson(json, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (gson != null) {
            list = gson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>() {
                    }.getType());
        }
        return list;
    }

    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (gson != null) {
            map = gson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }
}
