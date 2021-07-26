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
    private static final String apiKey;
    private static final String apiSecret;
    private static final String secretKey;
    private static final String lfasrAppId;
    private static final String ltpKey;
    private static final String telerobotAPPKey;
    private static final String telerobotAPPSecret;

    static {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PropertiesConfig.class.getResource("/").getPath() + "test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        appId = StringUtils.isNullOrEmpty(properties.getProperty("appId")) ? System.getenv("appId") : properties.getProperty("appId");
        apiSecret = StringUtils.isNullOrEmpty(properties.getProperty("apiSecret")) ? System.getenv("apiSecret") : properties.getProperty("apiSecret");
        apiKey = StringUtils.isNullOrEmpty(properties.getProperty("apiKey")) ? System.getenv("apiKey") : properties.getProperty("apiKey");

        lfasrAppId = StringUtils.isNullOrEmpty(properties.getProperty("lfasrAppId")) ? System.getenv("lfasrAppId") : properties.getProperty("lfasrAppId");
        secretKey = StringUtils.isNullOrEmpty(properties.getProperty("secretKey")) ? System.getenv("secretKey") : properties.getProperty("secretKey");
        ltpKey = StringUtils.isNullOrEmpty(properties.getProperty("ltpKey")) ? System.getenv("ltpKey") : properties.getProperty("ltpKey");

        telerobotAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("telerobotAPPKey")) ? System.getenv("telerobotAPPKey") : properties.getProperty("telerobotAPPKey");
        telerobotAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("telerobotAPPSecret")) ? System.getenv("telerobotAPPSecret") : properties.getProperty("telerobotAPPSecret");

    }

    public static String getAppId() {
        return appId;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static String getLfasrAppId() {
        return lfasrAppId;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getApiSecret() {
        return apiSecret;
    }

    public static String getLtpKey() {
        return ltpKey;
    }

    public static String getTelerobotAPPKey() {
        return telerobotAPPKey;
    }

    public static String getTelerobotAPPSecret() {
        return telerobotAPPSecret;
    }
}