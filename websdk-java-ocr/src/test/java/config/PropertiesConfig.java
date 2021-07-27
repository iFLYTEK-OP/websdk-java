package config;

import cn.xfyun.util.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * 统一的测试配置获取
 *
 * @author : jun
 * @date : 2021年04月02日
 */
public class PropertiesConfig {

    private static final String appId;

    private static final String bankcardClientApiKey;

    private static final String businessCardClientApiKey;

    private static final String fingerOcrClientApiKey;
    private static final String fingerOcrClientApiSecret;

    private static final String generalWordsClientApiKey;

    private static final String imageRecClientApiKey;

    private static final String imageWordClientApiKey;
    private static final String imageWordClientApiSecret;

    private static final String intsigOcrClientApiKey;

    private static final String itrClientApiKey;
    private static final String itrClientApiSecret;

    private static final String jDOcrClientApiKey;
    private static final String jDOcrClientApiSecret;

    private static final String placeRecClientApiKey;
    private static final String placeRecClientApiSecret;

    static {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PropertiesConfig.class.getResource("/").getPath() + "test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        appId = properties.getProperty("appId");
        bankcardClientApiKey = properties.getProperty("bankcardClientApiKey");
        businessCardClientApiKey = properties.getProperty("businessCardClientApiKey");
        fingerOcrClientApiKey = properties.getProperty("fingerOcrClientApiKey");
        fingerOcrClientApiSecret = properties.getProperty("fingerOcrClientApiSecret");
        generalWordsClientApiKey = properties.getProperty("generalWordsClientApiKey");
        imageRecClientApiKey = properties.getProperty("imageRecClientApiKey");
        imageWordClientApiKey = properties.getProperty("imageWordClientApiKey");
        imageWordClientApiSecret = properties.getProperty("imageWordClientApiSecret");
        intsigOcrClientApiKey = properties.getProperty("intsigOcrClientApiKey");
        itrClientApiKey = properties.getProperty("itrClientApiKey");
        itrClientApiSecret = properties.getProperty("itrClientApiSecret");
        jDOcrClientApiKey = properties.getProperty("jDOcrClientApiKey");
        jDOcrClientApiSecret = properties.getProperty("jDOcrClientApiSecret");
        placeRecClientApiKey = properties.getProperty("placeRecClientApiKey");
        placeRecClientApiSecret = properties.getProperty("placeRecClientApiSecret");
    }

    public static String getAppId() {
        return appId;
    }

    public static String getBankcardClientApiKey() {
        return bankcardClientApiKey;
    }

    public static String getBusinessCardClientApiKey() {
        return businessCardClientApiKey;
    }

    public static String getFingerOcrClientApiKey() {
        return fingerOcrClientApiKey;
    }

    public static String getFingerOcrClientApiSecret() {
        return fingerOcrClientApiSecret;
    }

    public static String getGeneralWordsClientApiKey() {
        return generalWordsClientApiKey;
    }

    public static String getImageRecClientApiKey() {
        return imageRecClientApiKey;
    }

    public static String getImageWordClientApiKey() {
        return imageWordClientApiKey;
    }

    public static String getImageWordClientApiSecret() {
        return imageWordClientApiSecret;
    }

    public static String getIntsigOcrClientApiKey() {
        return intsigOcrClientApiKey;
    }


    public static String getItrClientApiKey() {
        return itrClientApiKey;
    }

    public static String getItrClientApiSecret() {
        return itrClientApiSecret;
    }

    public static String getjDOcrClientApiKey() {
        return jDOcrClientApiKey;
    }

    public static String getjDOcrClientApiSecret() {
        return jDOcrClientApiSecret;
    }

    public static String getPlaceRecClientApiKey() {
        return placeRecClientApiKey;
    }

    public static String getPlaceRecClientApiSecret() {
        return placeRecClientApiSecret;
    }
}