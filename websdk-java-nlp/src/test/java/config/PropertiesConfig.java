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
    private static final String textComplianceClientApiKey;
    private static final String textComplianceClientApiSecret;
    private static final String picComplianceClientApiKey;
    private static final String picComplianceClientApiSecret;
    private static final String audioComplianceClientApiKey;
    private static final String audioComplianceClientApiSecret;
    private static final String videoComplianceClientApiKey;
    private static final String videoComplianceClientApiSecret;
    private static final String textProofClientApiKey;
    private static final String textProofClientApiSecret;
    private static final String textReWriteClientApiKey;
    private static final String textReWriteClientApiSecret;
    private static final String simInterpClientApiKey;
    private static final String simInterpClientApiSecret;

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
        textComplianceClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("textComplianceClientApiKey")) ? System.getenv("textComplianceClientApiKey") : properties.getProperty("textComplianceClientApiKey");
        textComplianceClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("textComplianceClientApiSecret")) ? System.getenv("textComplianceClientApiSecret") : properties.getProperty("textComplianceClientApiSecret");
        picComplianceClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("picComplianceClientApiKey")) ? System.getenv("picComplianceClientApiKey") : properties.getProperty("picComplianceClientApiKey");
        picComplianceClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("picComplianceClientApiSecret")) ? System.getenv("picComplianceClientApiSecret") : properties.getProperty("picComplianceClientApiSecret");
        audioComplianceClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("audioComplianceClientApiKey")) ? System.getenv("audioComplianceClientApiKey") : properties.getProperty("audioComplianceClientApiKey");
        audioComplianceClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("audioComplianceClientApiSecret")) ? System.getenv("audioComplianceClientApiSecret") : properties.getProperty("audioComplianceClientApiSecret");
        videoComplianceClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("videoComplianceClientApiKey")) ? System.getenv("videoComplianceClientApiKey") : properties.getProperty("videoComplianceClientApiKey");
        videoComplianceClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("videoComplianceClientApiSecret")) ? System.getenv("videoComplianceClientApiSecret") : properties.getProperty("videoComplianceClientApiSecret");
        textProofClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("textProofClientApiKey")) ? System.getenv("textProofClientApiKey") : properties.getProperty("textProofClientApiKey");
        textProofClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("textProofClientApiSecret")) ? System.getenv("textProofClientApiSecret") : properties.getProperty("textProofClientApiSecret");
        textReWriteClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("textReWriteClientApiKey")) ? System.getenv("textReWriteClientApiKey") : properties.getProperty("textReWriteClientApiKey");
        textReWriteClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("textReWriteClientApiSecret")) ? System.getenv("textReWriteClientApiSecret") : properties.getProperty("textReWriteClientApiSecret");
        simInterpClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("simInterpClientApiKey")) ? System.getenv("simInterpClientApiKey") : properties.getProperty("simInterpClientApiKey");
        simInterpClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("simInterpClientApiSecret")) ? System.getenv("simInterpClientApiSecret") : properties.getProperty("simInterpClientApiSecret");
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

    public static String getTextComplianceClientApiKey() {
        return textComplianceClientApiKey;
    }

    public static String getTextComplianceClientApiSecret() {
        return textComplianceClientApiSecret;
    }

    public static String getPicComplianceClientApiKey() {
        return picComplianceClientApiKey;
    }

    public static String getPicComplianceClientApiSecret() {
        return picComplianceClientApiSecret;
    }

    public static String getAudioComplianceClientApiKey() {
        return audioComplianceClientApiKey;
    }

    public static String getAudioComplianceClientApiSecret() {
        return audioComplianceClientApiSecret;
    }

    public static String getVideoComplianceClientApiKey() {
        return videoComplianceClientApiKey;
    }

    public static String getVideoComplianceClientApiSecret() {
        return videoComplianceClientApiSecret;
    }

    public static String getTextProofClientApiKey() {
        return textProofClientApiKey;
    }

    public static String getTextProofClientApiSecret() {
        return textProofClientApiSecret;
    }

    public static String getTextReWriteClientApiKey() {
        return textReWriteClientApiKey;
    }

    public static String getTextReWriteClientApiSecret() {
        return textReWriteClientApiSecret;
    }

    public static String getSimInterpClientApiKey() {
        return simInterpClientApiKey;
    }

    public static String getSimInterpClientApiSecret() {
        return simInterpClientApiSecret;
    }
}