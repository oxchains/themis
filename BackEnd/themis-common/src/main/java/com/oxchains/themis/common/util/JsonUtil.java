package com.oxchains.themis.common.util;

import com.alibaba.fastjson.JSONArray;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;

import java.util.List;

/**
 * Created by xuqi on 2017/10/17.
 */
public class JsonUtil {
    public static Gson gson = new GsonBuilder().create();
    public static String toJson(Object o){
        return gson.toJson(o).toString();
    }
    public static Object fromJson(String message,Class clazz){
        return gson.fromJson(message,clazz);
    }
    public static <T> List<T> jsonToList(String jsonString, Class<T> clazz) {
        List<T> ts = (List<T>) JSONArray.parseArray(jsonString, clazz);
        return ts;
    }
    public static <T> T jsonToEntity(String jsonString, Class<T> clazz){
        return gson.fromJson(jsonString, clazz);
    }
}
