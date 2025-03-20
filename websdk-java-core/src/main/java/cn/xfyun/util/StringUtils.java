package cn.xfyun.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * String工具类
 *
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class StringUtils {
    public static final String COMMA_SEPARATOR = ",";
    public static final Charset UTF8 = StandardCharsets.UTF_8;
    public static final Gson gson = new Gson();
    public static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String DEFAULT_ENCODING = "UTF-8";

    public static Integer toInteger(StringBuilder value) {
        return Integer.parseInt(value.toString());
    }

    public static String toString(StringBuilder value) {
        return value.toString();
    }

    public static Boolean toBoolean(StringBuilder value) {
        return Boolean.valueOf(value.toString());
    }

    public static String fromInteger(Integer value) {
        return Integer.toString(value);
    }

    public static String fromLong(Long value) {
        return Long.toString(value);
    }

    public static String fromString(String value) {
        return value;
    }

    public static String fromBoolean(Boolean value) {
        return Boolean.toString(value);
    }

    public static String fromBigInteger(BigInteger value) {
        return value.toString();
    }

    public static String fromBigDecimal(BigDecimal value) {
        return value.toString();
    }

    public static BigInteger toBigInteger(String s) {
        return new BigInteger(s);
    }

    public static BigDecimal toBigDecimal(String s) {
        return new BigDecimal(s);
    }

    public static String fromFloat(Float value) {
        return Float.toString(value);
    }

    public static String fromDouble(Double d) {
        return Double.toString(d);
    }

    public static String fromByte(Byte b) {
        return Byte.toString(b);
    }

    public static String replace(String originalString, String partToMatch, String replacement) {
        StringBuilder buffer = new StringBuilder(originalString.length());
        buffer.append(originalString);
        int indexOf = buffer.indexOf(partToMatch);
        while (indexOf != -1) {
            buffer.replace(indexOf, indexOf + partToMatch.length(), replacement);
            indexOf = buffer.indexOf(partToMatch);
        }
        return buffer.toString();
    }

    public static String join(String joiner, String... parts) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            builder.append(parts[i]);
            if (i < parts.length - 1) {
                builder.append(joiner);
            }
        }
        return builder.toString();
    }

    public static String trim(String value) {
        if (value == null) {
            return null;
        }
        return value.trim();
    }

    public static boolean isNullOrEmpty(String value) {
        if (value == null) {
            return true;
        }
        return value.isEmpty();
    }

    public static String toJson(Map<String, Object> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            System.out.println("JSON转换出错：" + e.getMessage());
            return "{}";
        }
    }

    public static byte[] unitByteArray(byte[] byte1, byte[] byte2) {
        byte[] unitByte = new byte[byte1.length + byte2.length];
        System.arraycopy(byte1, 0, unitByte, 0, byte1.length);
        System.arraycopy(byte2, 0, unitByte, byte1.length, byte2.length);
        return unitByte;
    }
}
