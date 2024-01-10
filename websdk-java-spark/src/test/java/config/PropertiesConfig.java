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
    private static final String ltpClientApiKey;
    private static final String saClientApiKey;
    private static final String textCheckClientApiKey;
    private static final String textCheckClientApiSecret;
    private static final String transClientApiKey;
    private static final String transClientApiSecret;

    static {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PropertiesConfig.class.getResource("/").getPath() + "test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        appId = StringUtils.isNullOrEmpty(properties.getProperty("appId")) ? System.getenv("appId") : properties.getProperty("appId");
        ltpClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("ltpClientApiKey")) ? System.getenv("ltpClientApiKey") : properties.getProperty("ltpClientApiKey");
        saClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("saClientApiKey")) ? System.getenv("saClientApiKey") : properties.getProperty("saClientApiKey");
        textCheckClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("textCheckClientApiKey")) ? System.getenv("textCheckClientApiKey") : properties.getProperty("textCheckClientApiKey");
        textCheckClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("textCheckClientApiSecret")) ? System.getenv("textCheckClientApiSecret") : properties.getProperty("textCheckClientApiSecret");
        transClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("transClientApiKey")) ? System.getenv("transClientApiKey") : properties.getProperty("transClientApiKey");
        transClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("transClientApiSecret")) ? System.getenv("transClientApiSecret") : properties.getProperty("transClientApiSecret");
    }

    public static String getAppId() {
        return appId;
    }

    public static String getLtpClientApiKey() {
        return ltpClientApiKey;
    }

    public static String getSaClientApiKey() {
        return saClientApiKey;
    }

    public static String getTextCheckClientApiKey() {
        return textCheckClientApiKey;
    }

    public static String getTextCheckClientApiSecret() {
        return textCheckClientApiSecret;
    }

    public static String getTransClientApiKey() {
        return transClientApiKey;
    }

    public static String getTransClientApiSecret() {
        return transClientApiSecret;
    }
}