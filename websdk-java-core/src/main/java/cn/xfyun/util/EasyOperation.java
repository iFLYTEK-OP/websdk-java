package cn.xfyun.util;


import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: rblu2
 * @desc: 通用工具类
 * @create: 2024-09-09 20:15
 **/

@SuppressWarnings("unused")
public class EasyOperation {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static ObjectMapper objectMapper() {
        return objectMapper;
    }
    

    public static <T> List<T> toList(T[] arr) {
        if(Objects.isNull(arr) || arr.length == 0) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(arr));
    }
    
    public static List<String> split(String text, String delimiter) {
        return new ArrayList<>(Arrays.asList(text.split(delimiter)));
    }

    public static <EXCEPTION extends RuntimeException> void asserts(boolean error, EXCEPTION exception) {
        if(error) {
            throw exception;
        }
    }
    
    public static <R> R predicate(boolean predicate, Supplier<R> r1, Supplier<R> r2) {
        if(predicate) {
            return r1.get();
        }
        return r2.get();
    }

    public static void predication(boolean predicate, Runnable r1, Runnable r2) {
        if(predicate) {
            r1.run();
        } else {
            r2.run();
        }
    }

    public static void predication(boolean predicate, Runnable r1) {
        if(predicate) {
            r1.run();
        }
    }

    public static <T> EasyMap<T> map() {
        return new EasyMap<>();
    }

    public static <T> EasyMap<T> map(Class<T> ignore) {
        return new EasyMap<>();
    }

    public static <T> EasyList<T> list() {
        return new EasyList<>();
    }

    public static <T> EasyList<T> list(Class<T> ignore) {
        return new EasyList<>();
    }
    

    public static class EasyList<T> {
        private final List<T> list;

        private EasyList() {
            list = new ArrayList<>();
        }

        public List<T> get() {
            return list;
        }

        public EasyList<T> add(T value) {
            list.add(value);
            return this;
        }

    }
    

    public static class EasyMap<T> {
        private final Map<String, T> map;

        private EasyMap() {
            map = new HashMap<>();
        }

        public Map<String, T> get() {
            return map;
        }

        public EasyMap<T> put(String key, T value) {
            map.put(key, value);
            return this;
        }
    }

    public static EasyJoiner joiner(String delimiter) {
        return new EasyJoiner(delimiter);
    }

    public static EasyJoiner joiner(List<String> list, String delimiter) {
        return new EasyJoiner(list, delimiter);
    }


    public static class EasyJoiner {
        private final List<String> list;
        private final String delimiter;
        
        public EasyJoiner(String delimiter) {
            this.delimiter = delimiter;
            list = new ArrayList<>();
        }
        
        public EasyJoiner(List<String> list, String delimiter) {
            this.delimiter = delimiter;
            this.list = list;
        }
        
        public EasyJoiner append(String value) {
            list.add(value);
            return this;
        }
        
        public String get() {
            StringBuilder str = new StringBuilder();
            for(int i = 0; i < list.size(); i++) {
                if(i == list.size() - 1) {
                    str.append(list.get(i));
                } else {
                    str.append(list.get(i)).append(delimiter);
                }
            }
            return str.toString();
        }
    }

    public static boolean isEmpty(String str) {
        return Objects.isNull(str) || str.isEmpty();
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }


    public static <T> String toJson(T t) {
        try {
            return objectMapper.writeValueAsString(t);
        } catch (Throwable var3) {
            throw new RuntimeException(var3);
        }
    }

    public static boolean isJson(String json) {
        try {
            objectMapper.readTree(json);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    public static void sleep(int time, TimeUnit unit) {
        try {
            unit.sleep(time);
        } catch (Exception var2) {
            Thread.currentThread().interrupt();
        }
    }

    public static <T> EasyLog<T> log(Class<T> clazz) {
        return new EasyLog<>(clazz);
    }
    
    public static class EasyLog<T> {
        private final Logger logger;
        public EasyLog(Class<?> clazz) {
            this.logger = LoggerFactory.getLogger(clazz);
        }

        public void trace(Consumer<Logger> consumer) {
            if(logger.isDebugEnabled()) {
                consumer.accept(this.logger);
            }
        }
        public Logger logger() {
            return logger;
        }
    }

}
