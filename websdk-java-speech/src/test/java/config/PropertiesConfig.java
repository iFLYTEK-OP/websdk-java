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
    private static final String iatClientApiKey;
    private static final String iatClientApiSecret;
    private static final String igrClientApiKey;
    private static final String igrClientApiSecret;
    private static final String  iseClientApiKey;
    private static final String  iseClientApiSecret;
    private static final String iseHttpClientApiKey;
    private static final String secretKey;
    private static final String qbhClientApiKey;
    private static final String rtasrClientApiKey;
    private static final String ttsClientApiKey;
    private static final String ttsClientApiSecret;
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
        iatClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("iatClientApiKey")) ? System.getenv("iatClientApiKey") : properties.getProperty("iatClientApiKey");
        iatClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("iatClientApiSecret")) ? System.getenv("iatClientApiSecret") : properties.getProperty("iatClientApiSecret");
        igrClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("igrClientApiKey")) ? System.getenv("igrClientApiKey") : properties.getProperty("igrClientApiKey");
        igrClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("igrClientApiSecret")) ? System.getenv("igrClientApiSecret") : properties.getProperty("igrClientApiSecret");
        iseClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("iseClientApiKey")) ? System.getenv("iseClientApiKey") : properties.getProperty("iseClientApiKey");
        iseClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("iseClientApiSecret")) ? System.getenv("iseClientApiSecret") : properties.getProperty("iseClientApiSecret");
        iseHttpClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("iseHttpClientApiKey")) ? System.getenv("iseHttpClientApiKey") : properties.getProperty("iseHttpClientApiKey");
        secretKey = StringUtils.isNullOrEmpty(properties.getProperty("secretKey")) ? System.getenv("secretKey") : properties.getProperty("secretKey");
        qbhClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("qbhClientApiKey")) ? System.getenv("qbhClientApiKey") : properties.getProperty("qbhClientApiKey");
        rtasrClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("rtasrClientApiKey")) ? System.getenv("rtasrClientApiKey") : properties.getProperty("rtasrClientApiKey");
        ttsClientApiKey = StringUtils.isNullOrEmpty(properties.getProperty("ttsClientApiKey")) ? System.getenv("ttsClientApiKey") : properties.getProperty("ttsClientApiKey");
        ttsClientApiSecret = StringUtils.isNullOrEmpty(properties.getProperty("ttsClientApiSecret")) ? System.getenv("ttsClientApiSecret") : properties.getProperty("ttsClientApiSecret");
        telerobotAPPKey = StringUtils.isNullOrEmpty(properties.getProperty("appId")) ? System.getenv("telerobotAPPKey") : properties.getProperty("telerobotAPPKey");
        telerobotAPPSecret = StringUtils.isNullOrEmpty(properties.getProperty("telerobotAPPSecret")) ? System.getenv("telerobotAPPSecret") : properties.getProperty("telerobotAPPSecret");
    }

    public static String getAppId() {
        return appId;
    }

    public static String getIatClientApiKey() {
        return iatClientApiKey;
    }

    public static String getIatClientApiSecret() {
        return iatClientApiSecret;
    }

    public static String getIgrClientApiKey() {
        return igrClientApiKey;
    }

    public static String getIgrClientApiSecret() {
        return igrClientApiSecret;
    }

    public static String getIseClientApiKey() {
        return iseClientApiKey;
    }

    public static String getIseClientApiSecret() {
        return iseClientApiSecret;
    }

    public static String getIseHttpClientApiKey() {
        return iseHttpClientApiKey;
    }

    public static String getSecretKey() {
        return secretKey;
    }

    public static String getQbhClientApiKey() {
        return qbhClientApiKey;
    }

    public static String getRtasrClientApiKey() {
        return rtasrClientApiKey;
    }

    public static String getTtsClientApiKey() {
        return ttsClientApiKey;
    }

    public static String getTtsClientApiSecret() {
        return ttsClientApiSecret;
    }

    public static String getTelerobotAPPKey() {
        return telerobotAPPKey;
    }

    public static String getTelerobotAPPSecret() {
        return telerobotAPPSecret;
    }
}