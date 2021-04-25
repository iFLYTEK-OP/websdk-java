package cn.xfyun.config;

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

    static {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PropertiesConfig.class.getResource("/").getPath() + "test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        appId = properties.getProperty("appId");
        apiSecret = properties.getProperty("apiSecret");
        apiKey = properties.getProperty("apiKey");

        lfasrAppId = properties.getProperty("lfasrAppId");
        secretKey = properties.getProperty("secretKey");
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
}