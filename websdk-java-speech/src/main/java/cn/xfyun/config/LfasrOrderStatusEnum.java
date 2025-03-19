package cn.xfyun.config;

/**
 * 讯飞语音转写订单状态枚举
 */
public enum LfasrOrderStatusEnum {
    
    /**
     * 订单已创建
     */
    CREATED(0, "订单已创建"),
    
    /**
     * 订单等待中
     */
    WAITING(1, "订单等待中"),
    
    /**
     * 订单处理中
     */
    PROCESSING(3, "订单处理中"),
    
    /**
     * 订单已完成
     */
    COMPLETED(4, "订单已完成"),
    
    /**
     * 订单失败
     */
    FAILED(-1, "订单失败");
    
    private final int key;
    private final String value;
    
    LfasrOrderStatusEnum(int key, String value) {
        this.key = key;
        this.value = value;
    }
    
    public int getKey() {
        return key;
    }
    
    public String getValue() {
        return value;
    }
    
    public static LfasrOrderStatusEnum getEnum(int key) {
        for (LfasrOrderStatusEnum statusEnum : LfasrOrderStatusEnum.values()) {
            if (statusEnum.getKey() == key) {
                return statusEnum;
            }
        }
        return null;
    }
}