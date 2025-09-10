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
    private static final String maasAPPKey;
    private static final String maasAPPSecret;
    private static final String maasAPIKey;
    private static final String voiceCloneAPPKey;
    private static final String voiceCloneAPPSecret;
    private static final String sparkAPPKey;
    private static final String sparkAPPSecret;
    private static final String sparkAPIPassword;
    private static final String imageGenAPPKey;
    private static final String imageGenAPPSecret;
    private static final String hidreamAPPKey;
    private static final String hidreamAPPSecret;
    private static final String imgUnderstandAPPSecret;
    private static final String imgUnderstandAPIKey;
    private static final String resumeGenClientApiKey;
    private static final String resumeGenClientApiSecret;
    private static final String sparkBatchAPIPassword;
    private static final String aiuiKnowledgePassword;

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
        maasAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("maasAPPKey")) ? System.getenv("maasAPPKey") : properties.getProperty("maasAPPKey");
        maasAPIKey = StringUtils.isNullOrEmpty(properties.getProperty("maasAPIKey")) ? System.getenv("maasAPIKey") : properties.getProperty("maasAPIKey");
        maasAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("maasAPPSecret")) ? System.getenv("maasAPPSecret") : properties.getProperty("maasAPPSecret");
        aiPPTClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("aiPPTClientApiKey")) ? System.getenv("aiPPTClientApiKey") : properties.getProperty("aiPPTClientApiKey");
        aiPPTClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("aiPPTClientApiSecret")) ? System.getenv("aiPPTClientApiSecret") : properties.getProperty("aiPPTClientApiSecret");
        voiceCloneAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("voiceCloneAPPKey")) ? System.getenv("voiceCloneAPPKey") : properties.getProperty("voiceCloneAPPKey");
        voiceCloneAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("voiceCloneAPPSecret")) ? System.getenv("voiceCloneAPPSecret") : properties.getProperty("voiceCloneAPPSecret");
        sparkAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("sparkAPPKey")) ? System.getenv("sparkAPPKey") : properties.getProperty("sparkAPPKey");
        sparkAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("sparkAPPSecret")) ? System.getenv("sparkAPPSecret") : properties.getProperty("sparkAPPSecret");
        sparkAPIPassword = StringUtils.isNullOrEmpty(properties.getProperty("sparkAPIPassword")) ? System.getenv("sparkAPIPassword") : properties.getProperty("sparkAPIPassword");
        imageGenAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("imageGenAPPKey")) ? System.getenv("imageGenAPPKey") : properties.getProperty("imageGenAPPKey");
        imageGenAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("imageGenAPPSecret")) ? System.getenv("imageGenAPPSecret") : properties.getProperty("imageGenAPPSecret");
        hidreamAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("hidreamAPPKey")) ? System.getenv("hidreamAPPKey") : properties.getProperty("hidreamAPPKey");
        hidreamAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("hidreamAPPSecret")) ? System.getenv("hidreamAPPSecret") : properties.getProperty("hidreamAPPSecret");
        imgUnderstandAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("imgUnderstandAPPSecret")) ? System.getenv("imgUnderstandAPPSecret") : properties.getProperty("imgUnderstandAPPSecret");
        imgUnderstandAPIKey = StringUtils.isNullOrEmpty(properties.getProperty("imgUnderstandAPIKey")) ? System.getenv("imgUnderstandAPIKey") : properties.getProperty("imgUnderstandAPIKey");
        resumeGenClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("resumeGenClientApiKey")) ? System.getenv("resumeGenClientApiKey") : properties.getProperty("resumeGenClientApiKey");
        resumeGenClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("resumeGenClientApiSecret")) ? System.getenv("resumeGenClientApiSecret") : properties.getProperty("resumeGenClientApiSecret");
        sparkBatchAPIPassword = StringUtils.isNullOrEmpty(properties.getProperty("sparkBatchAPIPassword")) ? System.getenv("sparkBatchAPIPassword") : properties.getProperty("sparkBatchAPIPassword");
        aiuiKnowledgePassword = StringUtils.isNullOrEmpty(properties.getProperty("aiuiKnowledgePassword")) ? System.getenv("aiuiKnowledgePassword") : properties.getProperty("aiuiKnowledgePassword");
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

    public static String getMaasAPPKey() {
        return maasAPPKey;
    }

    public static String getMaasAPIKey() {
        return maasAPIKey;
    }

    public static String getMaasAPPSecret() {
        return maasAPPSecret;
    }

    public static String getVoiceCloneAPPSecret() {
        return voiceCloneAPPSecret;
    }

    public static String getVoiceCloneAPPKey() {
        return voiceCloneAPPKey;
    }

    public static String getSparkAPPKey() {
        return sparkAPPKey;
    }

    public static String getSparkAPPSecret() {
        return sparkAPPSecret;
    }

    public static String getSparkAPIPassword() {
        return sparkAPIPassword;
    }

    public static String getImageGenAPPKey() {
        return imageGenAPPKey;
    }

    public static String getImageGenAPPSecret() {
        return imageGenAPPSecret;
    }

    public static String getHidreamAPPKey() {
        return hidreamAPPKey;
    }

    public static String getHidreamAPPSecret() {
        return hidreamAPPSecret;
    }

    public static String getImgUnderstandAPPSecret() {
        return imgUnderstandAPPSecret;
    }

    public static String getImgUnderstandAPIKey() {
        return imgUnderstandAPIKey;
    }

    public static String getResumeGenClientApiKey() {
        return resumeGenClientApiKey;
    }

    public static String getResumeGenClientApiSecret() {
        return resumeGenClientApiSecret;
    }

    public static String getSparkBatchAPIPassword() {
        return sparkBatchAPIPassword;
    }

    public static String getAiUiKnowledgePassword() {
        return aiuiKnowledgePassword;
    }
}