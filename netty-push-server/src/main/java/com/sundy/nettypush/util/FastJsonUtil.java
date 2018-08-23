package com.sundy.nettypush.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * @author plus.wang
 * @description
 * @date 2018/8/22
 */
public class FastJsonUtil {

    public static Object deserialize(String str) {
        return JSON.parse(str);
    }

    public static String serialize(Object obj) {
        try {
            return JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteNullStringAsEmpty, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object deserializeArray(String str) {
        return JSONArray.parseArray(str, JSONArray.class);
    }

    public static String serializeArray(Object obj) {
        return JSONArray.toJSONString(obj);
    }

}
