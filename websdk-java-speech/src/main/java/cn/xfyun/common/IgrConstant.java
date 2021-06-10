package cn.xfyun.common;

/**
 * @author: <flhong2@iflytek.com>
 * @description: 性别年龄评测常量
 * @version: v1.0
 * @create: 2021-06-02 10:22
 **/
public class IgrConstant {

    public static final String HOST_URL = "https://ws-api.xfyun.cn/v2/igr";

    /**
     * 首次上传
     */
    public static final int IGR_STATUS_FRAME_START = 0;
    /**
     * 中间上传
     */
    public static final int IGR_STATUS_FRAME_CONTINUE = 1;
    /**
     * 最后一次上传
     */
    public static final int IGR_STATUS_FRAME_END = 2;
    /**
     * 每一帧音频的大小,建议每 40ms 发送 1280B，大小可调整，
     */
    public static final int IGR_SIZE_FRAME = 1280;
    public static final int IGR_FRAME_INTERVEL = 40;
    public static final int CODE_STATUS_SUCCESS = 2;

}
