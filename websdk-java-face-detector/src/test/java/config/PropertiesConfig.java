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
    private static final String antiSpoofClientApiKey;
    private static final String antiSpoofClientApiSecret;
    private static final String faceCompareClientApiKey;
    private static final String faceCompareClientApiSecret;
    private static final String faceDetectClientApiKey;
    private static final String faceDetectClientApiSecret;
    private static final String faceStatusClientApiKey;
    private static final String faceStatusClientApiSecret;
    private static final String faceVerificationClientApiKey;
    private static final String silentDetectionClientApiKey;
    private static final String tupApiClientApiKey;
    private static final String watermarkVerificationApiKey;

    static {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(PropertiesConfig.class.getResource("/").getPath() + "test.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        appId = StringUtils.isNullOrEmpty(properties.getProperty("appId")) ? System.getenv("appId") : properties.getProperty("appId");
        antiSpoofClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("antiSpoofClientApiKey")) ? System.getenv("antiSpoofClientApiKey") : properties.getProperty("antiSpoofClientApiKey");
        antiSpoofClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("antiSpoofClientApiSecret")) ? System.getenv("antiSpoofClientApiSecret") : properties.getProperty("antiSpoofClientApiSecret");
        faceCompareClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("faceCompareClientApiKey")) ? System.getenv("faceCompareClientApiKey") : properties.getProperty("faceCompareClientApiKey");
        faceCompareClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("faceCompareClientApiSecret")) ? System.getenv("faceCompareClientApiSecret") : properties.getProperty("faceCompareClientApiSecret");
        faceDetectClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("faceDetectClientApiKey")) ? System.getenv("faceDetectClientApiKey") : properties.getProperty("faceDetectClientApiKey");
        faceDetectClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("faceDetectClientApiSecret")) ? System.getenv("faceDetectClientApiSecret") : properties.getProperty("faceDetectClientApiSecret");
        faceStatusClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("faceStatusClientApiKey")) ? System.getenv("faceStatusClientApiKey") : properties.getProperty("faceStatusClientApiKey");
        faceStatusClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("faceStatusClientApiSecret")) ? System.getenv("faceStatusClientApiSecret") : properties.getProperty("faceStatusClientApiSecret");
        faceVerificationClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("faceVerificationClientApiKey")) ? System.getenv("faceVerificationClientApiKey") : properties.getProperty("faceVerificationClientApiKey");
        silentDetectionClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("silentDetectionClientApiKey")) ? System.getenv("silentDetectionClientApiKey") : properties.getProperty("silentDetectionClientApiKey");
        tupApiClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("tupApiClientApiKey")) ? System.getenv("tupApiClientApiKey") : properties.getProperty("tupApiClientApiKey");
        watermarkVerificationApiKey = StringUtils.isNullOrEmpty(properties.getProperty("watermarkVerificationApiKey")) ? System.getenv("watermarkVerificationApiKey") : properties.getProperty("watermarkVerificationApiKey");
    }

    public static String getAppId() {
        return appId;
    }

    public static String getAntiSpoofClientApiKey() {
        return antiSpoofClientApiKey;
    }

    public static String getAntiSpoofClientApiSecret() {
        return antiSpoofClientApiSecret;
    }

    public static String getFaceCompareClientApiKey() {
        return faceCompareClientApiKey;
    }

    public static String getFaceCompareClientApiSecret() {
        return faceCompareClientApiSecret;
    }

    public static String getFaceDetectClientApiKey() {
        return faceDetectClientApiKey;
    }

    public static String getFaceDetectClientApiSecret() {
        return faceDetectClientApiSecret;
    }

    public static String getFaceStatusClientApiKey() {
        return faceStatusClientApiKey;
    }

    public static String getFaceStatusClientApiSecret() {
        return faceStatusClientApiSecret;
    }

    public static String getFaceVerificationClientApiKey() {
        return faceVerificationClientApiKey;
    }

    public static String getSilentDetectionClientApiKey() {
        return silentDetectionClientApiKey;
    }

    public static String getTupApiClientApiKey() {
        return tupApiClientApiKey;
    }

    public static String getWatermarkVerificationApiKey() {
        return watermarkVerificationApiKey;
    }
}