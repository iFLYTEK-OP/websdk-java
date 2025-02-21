package cn.xfyun.basic;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: rblu2
 * @create: 2024-09-20 11:07
 **/
@SuppressWarnings("unused")
public class ConvertOperation {

    public static <FROM, TARGET> TARGET to(FROM t, Class<TARGET> clazz) {
        try {
            return ObjectMapperFactory.instance().convertValue(t, clazz);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }
    
    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return ObjectMapperFactory.instance().readValue(json, clazz);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        try {
            ObjectMapper instance = ObjectMapperFactory.instance();
            return instance.readValue(json, instance.getTypeFactory().constructCollectionType(ArrayList.class, clazz));
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <K, V> Map<K, V> parseMap(String json, Class<K> clazz1, Class<V> clazz2) {
        try {
            ObjectMapper instance = ObjectMapperFactory.instance();
            return instance.readValue(json, instance.getTypeFactory().constructMapType(HashMap.class, clazz1, clazz2));
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    public static <T> T fromJson(String json, TypeReference<T> typeReference) {
        try {
            return ObjectMapperFactory.instance().readValue(json, typeReference);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }


    public static <T> String toJson(T t) {
        try {
            return ObjectMapperFactory.instance().writeValueAsString(t);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    public static boolean isJson(String json) {
        try {
            ObjectMapperFactory.instance().readTree(json);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public boolean sameJson(String json1, String json2) {
        try {
            JsonNode node1 = ObjectMapperFactory.instance().readTree(json1);
            JsonNode node2 = ObjectMapperFactory.instance().readTree(json2);
            return node1.equals(node2);
        }catch (Throwable ignored) {
            return false;
        }
    }


}
