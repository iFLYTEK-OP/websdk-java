package cn.xfyun.util;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件工具类
 *
 * @author kaili23
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);

    // 默认缓冲区大小：8KB
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

}
