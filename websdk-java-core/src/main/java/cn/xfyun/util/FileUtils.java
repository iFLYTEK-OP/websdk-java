package cn.xfyun.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

/**
 * @program: websdk-java
 * @description:
 * @author: zyding6
 * @create: 2025/3/14 10:23
 **/
public class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    private final static List<String> suffixes = Arrays.asList("mp3", "alaw", "ulaw", "pcm", "aac", "wav");

    // 读取文件为为byte
    public static byte[] fileToByteArray(String filePath) throws IOException {
        InputStream inputStream = new FileInputStream(filePath);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n;
        while ((n = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, n);
        }
        return byteArrayOutputStream.toByteArray();
    }

    // 读取文件为为base64
    public static String fileToBase64(String filePath) throws IOException {
        return Base64.getEncoder().encodeToString(fileToByteArray(filePath));
    }

    //获取音频Url后缀
    public static String getLegalAudioSuffixByUrl(String fileUrl) {
        if (StringUtils.isNullOrEmpty(fileUrl)) {
            return "";
        }
        String suffix = fileUrl.substring(fileUrl.lastIndexOf(".") + 1);
        if (!suffixes.contains(suffix)) {
            return "";
        }
        return suffix;
    }

    //获取音频Url名称
    public static String getLegalAudioNameByUrl(String fileUrl) {
        if (StringUtils.isNullOrEmpty(fileUrl)) {
            return "";
        }
        return fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
    }

    public static String downloadFileWithFolder(String url, String folderPath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL fileUrl = new URL(url);
            // 打开连接
            inputStream = fileUrl.openStream();
            // 获取文件名
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            // 拼接文件路径
            String filePath = folderPath + File.separator + fileName;
            // 创建输出流
            outputStream = Files.newOutputStream(Paths.get(filePath));
            // 读取数据并写入文件
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            log.info("文件已成功下载： " + filePath);
            return filePath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            // 关闭流
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (null != outputStream) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        return null;
    }
}
