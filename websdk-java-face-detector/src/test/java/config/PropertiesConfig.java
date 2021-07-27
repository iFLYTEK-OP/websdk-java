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
        appId = properties.getProperty("appId");
        antiSpoofClientApiKey = properties.getProperty("antiSpoofClientApiKey");
        antiSpoofClientApiSecret = properties.getProperty("antiSpoofClientApiSecret");
        faceCompareClientApiKey = properties.getProperty("faceCompareClientApiKey");
        faceCompareClientApiSecret = properties.getProperty("faceCompareClientApiSecret");
        faceDetectClientApiKey = properties.getProperty("faceDetectClientApiKey");
        faceDetectClientApiSecret = properties.getProperty("faceDetectClientApiSecret");
        faceStatusClientApiKey = properties.getProperty("faceStatusClientApiKey");
        faceStatusClientApiSecret = properties.getProperty("faceStatusClientApiSecret");
        faceVerificationClientApiKey = properties.getProperty("faceVerificationClientApiKey");
        silentDetectionClientApiKey = properties.getProperty("silentDetectionClientApiKey");
        tupApiClientApiKey = properties.getProperty("tupApiClientApiKey");
        watermarkVerificationApiKey = properties.getProperty("watermarkVerificationApiKey");
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