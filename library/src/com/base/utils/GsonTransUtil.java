package com.base.utils;

import android.util.Log;

import com.dataexchange.BaseVo;

/**
 * Created by zhangyu on 16-9-6.
 */
public class GsonTransUtil {

    public static BaseVo fromJson(String gsonStr, Class clazz) {
        try {
            if (gsonStr == null) {
                return null;
            }


            com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder();
            com.google.gson.Gson gson = builder.create();
            return (BaseVo) gson.fromJson(gsonStr, clazz);
        } catch (Exception ex) {
            Log.e("TEST", " exception , " + ex.getMessage());
            return null;
        }
    }

    public static String toJson(Object obj) {
        try {
            if (obj == null) {
                return null;
            }
            com.google.gson.GsonBuilder builder = new com.google.gson.GsonBuilder();
            com.google.gson.Gson gson = builder.create();
            return gson.toJson(obj);
        } catch (Exception ex) {
            // Log.e("TEST", " exception , " + ex.getMessage());
            return null;
        }
    }
}
