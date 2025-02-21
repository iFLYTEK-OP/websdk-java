package cn.xfyun.basic;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class TimeOperation {

    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final Map<String, SimpleDateFormat> formats = new HashMap<>();
    private static final EnumMap<TimeUnit, EasyMillSecondsTime> millSeconds = new EnumMap<>(TimeUnit.class);

    static {
        
        formats.put("yyyy-MM-dd HH:mm:ss", DEFAULT_DATE_FORMAT);
        formats.put("yyyyMMddHHmmssSSS", new SimpleDateFormat("yyyyMMddHHmmssSSS"));
        formats.put("yyyy-MM-dd", new SimpleDateFormat("yyyy-MM-dd"));
        formats.put("yyyyMMdd", new SimpleDateFormat("yyyyMMdd"));
        formats.put("HH:mm:ss", new SimpleDateFormat("HH:mm:ss"));
        
        millSeconds.put(TimeUnit.SECONDS, x -> x * 1000);
        millSeconds.put(TimeUnit.MINUTES, x -> x * 60 * 1000);
        millSeconds.put(TimeUnit.HOURS, x -> x * 3600 * 1000);
        millSeconds.put(TimeUnit.DAYS, x -> x * 24 * 3600 * 1000);
    }

    @FunctionalInterface
    public interface EasyMillSecondsTime {
        long apply(long time);
    }
    
    public static Date now() {
        return new Date();
    }

    public static Date date(long timestamp) {
        return new Date(timestamp);
    }

    public static long time() {
        return System.currentTimeMillis();
    }

    public static boolean usableDate(Date start, Date end) {
        Date current = now();
        return !start.after(current) && !end.before(current);
    }

    public static boolean usableTime(long start, long end) {
        long current = time();
        return start <= current && current <= end;
    }

    public static boolean gt(Date date1, Date date2) {
        return date1.after(date2);
    }

    public static boolean gte(Date date1, Date date2) {
        return !date1.before(date2);
    }

    public static boolean lt(Date date1, Date date2) {
        return date1.before(date2);
    }

    public static boolean lte(Date date1, Date date2) {
        return !date1.after(date2);
    }

    public static boolean sameDay(Date date1, Date date2) {
        return Objects.equals(easyDate(date1).toLocalDate(), easyDate(date2).toLocalDate());
    }

    public static long addTime(int num, TimeUnit unit) {
        return millSeconds.get(unit).apply(num);
    }
    
    public static Date toDate(String date) {
        try {
            return DEFAULT_DATE_FORMAT.parse(date);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static EasyDate easyDate() {
        return new EasyDate(now());
    }

    public static EasyDate easyDate(Date date) {
        return new EasyDate(date);
    }

    public static class EasyDate {
        public Date date;

        public EasyDate(Date date) {
            this.date = date;
        }

        public Date get() {
            return date;
        }

        public EasyDate addSeconds(int num) {
            date = easyDateTime(toLocalDateTime().plusSeconds(num)).toDate();
            return this;
        }

        public EasyDate addMinutes(int num) {
            date = easyDateTime(toLocalDateTime().plusMinutes(num)).toDate();
            return this;
        }

        public EasyDate addHours(int num) {
            date = easyDateTime(toLocalDateTime().plusHours(num)).toDate();
            return this;
        }
        
        public EasyDate addDays(int num) {
            date = easyDateTime(toLocalDateTime().plusDays(num)).toDate();
            return this;
        }

        public EasyDate addMonths(int num) {
            date = easyDateTime(toLocalDateTime().plusMonths(num)).toDate();
            return this;
        }

        public EasyDate addYears(int num) {
            date = easyDateTime(toLocalDateTime().plusYears(num)).toDate();
            return this;
        }

        public EasyDate minusSeconds(int num) {
            date = easyDateTime(toLocalDateTime().minusSeconds(num)).toDate();
            return this;
        }
        
        public EasyDate minusMinutes(int num) {
            date = easyDateTime(toLocalDateTime().minusMinutes(num)).toDate();
            return this;
        }
        
        public EasyDate minusHours(int num) {
            date = easyDateTime(toLocalDateTime().minusHours(num)).toDate();
            return this;
        }
        
        public EasyDate minusDays(int num) {
            date = easyDateTime(toLocalDateTime().minusDays(num)).toDate();
            return this;
        }
        
        public EasyDate minusMonths(int num) {
            date = easyDateTime(toLocalDateTime().minusMonths(num)).toDate();
            return this;
        }
        
        public EasyDate minusYears(int num) {
            date = easyDateTime(toLocalDateTime().minusYears(num)).toDate();
            return this;
        }
        
        public EasyDate atStartOfDay() {
            date = Date.from(toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            return this;
        }

        public EasyDate atEndOfDay() {
            date = easyDateTime(LocalDateTime.of(toLocalDate(), LocalTime.of(23, 59, 59))).toDate();
            return this;
        }

        public LocalDateTime toLocalDateTime() {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        public LocalDate toLocalDate() {
            return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        public String format() {
            return DEFAULT_DATE_FORMAT.format(date);
        }
        
        public String format(String pattern) {
            return formats.getOrDefault(pattern, new SimpleDateFormat(pattern)).format(date);
        }

        public String format(DateFormat format) {
            return format.format(date);
        }
    }

    public static EasyDateTime easyDateTime(LocalDateTime dateTime) {
        return new EasyDateTime(dateTime);
    }

    public static class EasyDateTime {
        public final LocalDateTime dateTime;

        public EasyDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public LocalDateTime get() {
            return dateTime;
        }

        public Date toDate() {
            return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        }

    }

    public static void sleep(int time) {
        try {
            TimeUnit.SECONDS.sleep(time);
        }catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }

}
