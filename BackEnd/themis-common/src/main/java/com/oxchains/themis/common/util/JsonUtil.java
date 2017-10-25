package com.oxchains.themis.common.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
}
