package cn.xfyun.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * 文件工具类
 *
 * @author kaili23
 */
public class FileUtil {

    private static final Logger log = LoggerFactory.getLogger(FileUtil.class);

    /**
     * 默认缓冲区大小：8KB
     */
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * 将文件读取为字节数组
     *
     * @param file 要读取的文件
     * @return 文件内容的字节数组
     * @throws IOException 如果读取过程中发生IO异常
     */
    public static byte[] readFileToByteArray(File file) throws IOException {
        if (file == null) {
            throw new IllegalArgumentException("文件参数不能为null");
        }
        if (!file.exists()) {
            throw new IOException("文件不存在: " + file);
        }
        if (!file.isFile()) {
            throw new IOException("不是一个文件: " + file);
        }
        // 对于小文件，使用简单方法
        if (file.length() < DEFAULT_BUFFER_SIZE * 10) {
            return readSmallFile(file);
        }
        // 对于大文件，使用缓冲区分批读取
        return readLargeFile(file);
    }

    /**
     * 读取小文件（小于80KB）
     */
    private static byte[] readSmallFile(File file) throws IOException {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[(int) file.length()];
            int bytesRead = fileInputStream.read(buffer);
            // 检查是否完整读取
            if (bytesRead != buffer.length) {
                throw new IOException("无法完整读取文件，预期读取 " + buffer.length + " 字节，实际读取 " + bytesRead + " 字节");
            }
            return buffer;
        } finally {
            IOCloseUtil.close(fileInputStream);
        }
    }

    /**
     * 读取大文件，使用缓冲区分批读取
     */
    private static byte[] readLargeFile(File file) throws IOException {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream outputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int bytesRead;
            // 分批读取文件内容
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        } finally {
            IOCloseUtil.close(outputStream);
            IOCloseUtil.close(fileInputStream);
        }
    }

    /**
     * 读取文件为为base64
     *
     * @param filePath 文件路径
     * @return 文件的Base64值
     * @throws IOException 错误信息
     */
    public static String fileToBase64(String filePath) throws IOException {
        return Base64.getEncoder().encodeToString(readFileToByteArray(new File(filePath)));
    }


    /**
     * 下载url文件到指定问价夹
     *
     * @param url        文件链接
     * @param folderPath 需要保存的问价夹路径
     * @return 保存的文件全路径
     */
    public static String downloadFileWithFolder(String url, String folderPath, String fileName) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            URL fileUrl = new URL(url);
            // 打开连接
            inputStream = fileUrl.openStream();
            // 获取文件名
            if (StringUtils.isNullOrEmpty(fileName)) {
                fileName = url.substring(url.lastIndexOf("/") + 1);
            }
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
