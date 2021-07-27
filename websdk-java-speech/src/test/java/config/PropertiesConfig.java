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
        appId = properties.getProperty("appId");
        iatClientApiKey = properties.getProperty("iatClientApiKey");
        iatClientApiSecret = properties.getProperty("iatClientApiSecret");
        igrClientApiKey = properties.getProperty("igrClientApiKey");
        igrClientApiSecret = properties.getProperty("igrClientApiSecret");
        iseClientApiKey = properties.getProperty("iseClientApiKey");
        iseClientApiSecret = properties.getProperty("iseClientApiSecret");
        iseHttpClientApiKey = properties.getProperty("iseHttpClientApiKey");
        secretKey = properties.getProperty("secretKey");
        qbhClientApiKey = properties.getProperty("qbhClientApiKey");
        rtasrClientApiKey = properties.getProperty("rtasrClientApiKey");
        ttsClientApiKey = properties.getProperty("ttsClientApiKey");
        ttsClientApiSecret = properties.getProperty("ttsClientApiSecret");
        telerobotAPPKey = properties.getProperty("telerobotAPPKey");
        telerobotAPPSecret = properties.getProperty("telerobotAPPSecret");
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