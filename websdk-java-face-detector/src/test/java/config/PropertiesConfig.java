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

    }

    public static String getAppId() {
        return appId;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getApiSecret() {
        return apiSecret;
    }

}