package cn.xfyun.common;

/**
 * @author: flhong2
 * @description: 语音评测常量
 * @create: 2021-03-18 15:13
 **/
public class IseConstant {

    public static final String HOST_URL = "https://ise-api.xfyun.cn/v2/open-ise";
    public static final String ISE_SUB = "ise";

    /**
     * 中文
     */
    public static final String ISE_ENT_CN = "cn_vip";
    /**
     * 英文
     */
    public static final String ISE_ENT_EN = "en_vip";
    /**
     * 首次上传
     */
    public static final int ISE_STATUS_FRAME_START = 0;
    /**
     * 中间上传
     */
    public static final int ISE_STATUS_FRAME_CONTINUE = 1;
    /**
     * 最后一次上传
     */
    public static final int ISE_STATUS_FRAME_END = 2;
    /**
     * 每一帧音频的大小,建议每 40ms 发送 1280B，大小可调整，
     * 但是不要超过19200B，即base64压缩后能超过26000B，否则会报错10163数据过长错误
     */
    public static final int ISE_SIZE_FRAME = 1280;
    public static final int ISE_FRAME_INTERVEL = 40;

    public static final int CODE_STATUS_SUCCESS = 2;


}
