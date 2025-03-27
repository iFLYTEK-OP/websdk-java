package config;

import cn.xfyun.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(PropertiesConfig.class);

    private static final String appId;
    private static final String ltpClientApiKey;
    private static final String saClientApiKey;
    private static final String textCheckClientApiKey;
    private static final String textCheckClientApiSecret;
    private static final String transClientApiKey;
    private static final String transClientApiSecret;
    private static final String aiPPTClientApiKey;
    private static final String aiPPTClientApiSecret;
    private static final String oralAPPKey;
    private static final String oralAPPSecret;
    private static final String sparkIatAPPKey;
    private static final String sparkIatAPPSecret;
    private static final String massAPPKey;
    private static final String massAPPSecret;
    private static final String massAPIKey;

    static {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PropertiesConfig.class.getResource("/").getPath() + "test.properties"));
        } catch (IOException e) {
            logger.error("load test.properties error", e);
        }
        appId = StringUtils.isNullOrEmpty(properties.getProperty("appId")) ? System.getenv("appId") : properties.getProperty("appId");
        ltpClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("ltpClientApiKey")) ? System.getenv("ltpClientApiKey") : properties.getProperty("ltpClientApiKey");
        saClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("saClientApiKey")) ? System.getenv("saClientApiKey") : properties.getProperty("saClientApiKey");
        textCheckClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("textCheckClientApiKey")) ? System.getenv("textCheckClientApiKey") : properties.getProperty("textCheckClientApiKey");
        textCheckClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("textCheckClientApiSecret")) ? System.getenv("textCheckClientApiSecret") : properties.getProperty("textCheckClientApiSecret");
        transClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("transClientApiKey")) ? System.getenv("transClientApiKey") : properties.getProperty("transClientApiKey");
        transClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("transClientApiSecret")) ? System.getenv("transClientApiSecret") : properties.getProperty("transClientApiSecret");
        oralAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("oralAPPKey")) ? System.getenv("oralAPPKey") : properties.getProperty("oralAPPKey");
        oralAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("oralAPPSecret")) ? System.getenv("oralAPPSecret") : properties.getProperty("oralAPPSecret");
        sparkIatAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("sparkIatAPPKey")) ? System.getenv("sparkIatAPPKey") : properties.getProperty("sparkIatAPPKey");
        sparkIatAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("sparkIatAPPSecret")) ? System.getenv("sparkIatAPPSecret") : properties.getProperty("sparkIatAPPSecret");
        massAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("massAPPKey")) ? System.getenv("massAPPKey") : properties.getProperty("massAPPKey");
        massAPIKey = StringUtils.isNullOrEmpty(properties.getProperty("massAPIKey")) ? System.getenv("massAPIKey") : properties.getProperty("massAPIKey");
        massAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("massAPPSecret")) ? System.getenv("massAPPSecret") : properties.getProperty("massAPPSecret");
        aiPPTClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("aiPPTClientApiKey")) ? System.getenv("aiPPTClientApiKey") : properties.getProperty("aiPPTClientApiKey");
        aiPPTClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("aiPPTClientApiSecret")) ? System.getenv("aiPPTClientApiSecret") : properties.getProperty("aiPPTClientApiSecret");
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

    public static String getAIPPTClientApiKey() {
        return aiPPTClientApiKey;
    }

    public static String getAIPPTClientApiSecret() {
        return aiPPTClientApiSecret;
    }

    public static String getOralAPPKey() {
        return oralAPPKey;
    }

    public static String getOralAPPSecret() {
        return oralAPPSecret;
    }

    public static String getSparkIatAPPKey() {
        return sparkIatAPPKey;
    }

    public static String getSparkIatAPPSecret() {
        return sparkIatAPPSecret;
    }

    public static String getMassAPPKey() {
        return massAPPKey;
    }

    public static String getMassAPIKey() {
        return massAPIKey;
    }

    public static String getMassAPPSecret() {
        return massAPPSecret;
    }
}