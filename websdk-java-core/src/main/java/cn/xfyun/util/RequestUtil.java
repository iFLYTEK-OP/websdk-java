package cn.xfyun.util;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Field;

/**
 * http请求工具类
 *
 * @author <zyding6@ifytek.com>
 **/
public class RequestUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    /**
     * 获取formData的body
     *
     * @param param 表单对象数据
     * @return RequestBody
     */
    public static RequestBody getFormDataBody(Object param) {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        Field[] declaredFields = param.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            try {
                // 设置私有字段为可访问
                field.setAccessible(true);
                // 获取字段类型
                Class<?> type = field.getType();
                // 获取字段值
                Object value = field.get(param);
                if (null != value) {
                    if (type == File.class) {
                        File file = (File) value;
                        builder.addFormDataPart(field.getName(), file.getName(),
                                RequestBody.create(MultipartBody.FORM, file));
                    } else {
                        builder.addFormDataPart(field.getName(), String.valueOf(value));
                    }
                }
            } catch (IllegalAccessException e) {
                logger.error("获取表单数据失败", e);
            }
        }
        return builder.build();
    }
}
