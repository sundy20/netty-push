package com.sundy.nettypush.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;

import java.util.List;

/**
 * @author plus.wang
 * @description
 * @date 2018/8/22
 */
public class FastJsonUtil {

    public static <T> T deserialize(String str, Class<T> clazz) {

        return JSON.parseObject(str, clazz);
    }

    public static String serialize(Object obj) {

        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

    public static <T> List<T> deserializeArray(String str, Class<T> clazz) {

        return JSONArray.parseArray(str, clazz);
    }

    public static String serializeArray(Object obj) {

        return JSONArray.toJSONString(obj);
    }

}
