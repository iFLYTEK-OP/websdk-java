package cn.xfyun.model.oralchat;

import com.google.gson.annotations.SerializedName;

/**
 * 超拟人聊天开启形象参数
 *
 * @author <zyding6@iflytek.com>
 **/
public class Avatar {

    /**
     * 形象ID
     */
    @SerializedName("avatar_id")
    private String avatarId;
    /**
     * 形象照片base64
     */
    private String image;
    /**
     * 照片格式
     */
    private String encoding;
    /**
     * 宽
     */
    private Integer width;
    /**
     * 高
     */
    private Integer height;

    public String getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(String avatarId) {
        this.avatarId = avatarId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }
}
