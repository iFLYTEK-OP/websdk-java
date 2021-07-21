package cn.xfyun.config;

/**
 * @author mqgao
 * @version 1.0
 * @date 2021/7/7 14:00
 */
public enum ItrEntEnum {

    /**
     *   拍照速算识别
     */
    MATH_ARITH("math-arith"),

    /**
     *   公式识别
     */
    TEACH_PHOTO_PRINT("teach-photo-print");

    private String value;

    ItrEntEnum(String value){
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
