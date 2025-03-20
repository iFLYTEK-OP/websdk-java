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
    private static final String resumeGenClientApiKey;
    private static final String resumeGenClientApiSecret;
    private static final String oralAPPKey;
    private static final String oralAPPSecret;
    private static final String voiceCloneAPPKey;
    private static final String voiceCloneAPPSecret;
    private static final String sparkIatAPPKey;
    private static final String sparkIatAPPSecret;

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
        resumeGenClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("resumeGenClientApiKey")) ? System.getenv("resumeGenClientApiKey") : properties.getProperty("resumeGenClientApiKey");
        resumeGenClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("resumeGenClientApiSecret")) ? System.getenv("resumeGenClientApiSecret") : properties.getProperty("resumeGenClientApiSecret");
        oralAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("oralAPPKey")) ? System.getenv("oralAPPKey") : properties.getProperty("oralAPPKey");
        oralAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("oralAPPSecret")) ? System.getenv("oralAPPSecret") : properties.getProperty("oralAPPSecret");
        voiceCloneAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("voiceCloneAPPKey")) ? System.getenv("voiceCloneAPPKey") : properties.getProperty("voiceCloneAPPKey");
        voiceCloneAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("voiceCloneAPPSecret")) ? System.getenv("voiceCloneAPPSecret") : properties.getProperty("voiceCloneAPPSecret");
        sparkIatAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("sparkIatAPPKey")) ? System.getenv("sparkIatAPPKey") : properties.getProperty("sparkIatAPPKey");
        sparkIatAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("sparkIatAPPSecret")) ? System.getenv("sparkIatAPPSecret") : properties.getProperty("sparkIatAPPSecret");
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

    public static String getResumeGenClientApiKey() {
        return resumeGenClientApiKey;
    }

    public static String getResumeGenClientApiSecret() {
        return resumeGenClientApiSecret;
    }

    public static String getOralAPPKey() {
        return oralAPPKey;
    }

    public static String getOralAPPSecret() {
        return oralAPPSecret;
    }

    public static String getVoiceCloneAPPSecret() {
        return voiceCloneAPPSecret;
    }

    public static String getVoiceCloneAPPKey() {
        return voiceCloneAPPKey;
    }

    public static String getSparkIatAPPKey() {
        return sparkIatAPPKey;
    }

    public static String getSparkIatAPPSecret() {
        return sparkIatAPPSecret;
    }
}