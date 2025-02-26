package cn.xfyun.basic;


import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * @author: rblu2
 * @desc: SIMPLE
 * @create: 2024-09-09 20:15
 **/

@SuppressWarnings("unused")
public class EasyOperation {


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

    public static <T> void predicate(boolean predicate, Consumer<T> consumer, T t) {
        if(predicate) {
            consumer.accept(t);
        }
    }
    
    public static <R> R getOrDefault(Supplier<R> r1, R r2) {
        return Optional.ofNullable(r1.get()).orElse(r2);
    }

    public static <R> R getOrDefault(boolean predicate, R r1, R r2) {
        return predicate ? r1 : r2;
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

    public static <T> EasySet<T> set() {
        return new EasySet<>();
    }

    public static <T> EasySet<T> set(Class<T> ignore) {
        return new EasySet<>();
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

    public static class EasySet<T> {
        private final Set<T> set;

        private EasySet() {
            set = new HashSet<>();
        }

        public Set<T> get() {
            return set;
        }

        public EasySet<T> add(T value) {
            set.add(value);
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
    
    public static <EXCEPTION extends Exception> void retry(Runnable runnable, int times, Class<EXCEPTION> retryExceptionClass) {
        times++;
        while (times-- > 0) {
            try {
                runnable.run();
                return;
            } catch (Exception exception) {
                if(!retryExceptionClass.isAssignableFrom(exception.getClass()) || times == 0) {
                    throw exception;
                }
            }
        }
    }

    public static boolean trySuccess(BooleanSupplier supplier, int times, int interval) {
        while (times-- > 0) {
            if(supplier.getAsBoolean()) {
                return true;
            } else {
                TimeOperation.sleep(interval);
            }
        }
        return false;
    }

    public static <EXCEPTION extends Exception> void retry(Runnable runnable, int times, Class<EXCEPTION> retryExceptionClass, int sleep) {
        times++;
        while (times-- > 0) {
            try {
                runnable.run();
                return;
            } catch (Exception exception) {
                if(!retryExceptionClass.isAssignableFrom(exception.getClass()) || times == 0) {
                    throw exception;
                }

                TimeOperation.sleep(sleep);
            }
        }
    }

    public static <R, EXCEPTION extends Exception> R retry(Callable<R> callable, int times, Class<EXCEPTION> retryExceptionClass) {
        times++;
        while (times-- > 0) {
            try {
                return callable.call();
            } catch (Exception exception) {
                if(!retryExceptionClass.isAssignableFrom(exception.getClass()) || times == 0) {
                    throw new RuntimeException(exception);
                }
            }
        }
        return null;
    }

    public static <R, EXCEPTION extends Exception> R retry(Callable<R> callable, int times, Class<EXCEPTION> retryExceptionClass, int sleep) {
        times++;
        while (times-- > 0) {
            try {
                return callable.call();
            } catch (Exception exception) {
                if(!retryExceptionClass.isAssignableFrom(exception.getClass()) || times == 0) {
                    throw new RuntimeException(exception);
                }
                TimeOperation.sleep(sleep);
            }
        }
        return null;
    }


}
