package cn.xfyun.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author <ydwang16@iflytek.com>
 * @description 关闭流工具
 * @date 2021/3/27
 */
public class IOCloseUtil {

    private static final Logger logger = LoggerFactory.getLogger(IOCloseUtil.class);

    public static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            logger.error("Close IO exception ", e);
        }
    }
}
