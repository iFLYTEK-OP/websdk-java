package cn.xfyun.model.image;

import cn.xfyun.model.sparkmodel.RoleContent;
import java.util.List;

/**
 * 大模型图片生成请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class ImageGenParam {

    /**
     * 图片的宽度
     * 分辨率（width * height）	图点数(计费规则)
     * 512x512	                  6
     * 640x360	                  6
     * 640x480	                  6
     * 640x640	                  7
     * 680x512	                  7
     * 512x680	                  7
     * 768x768	                  8
     * 720x1280	                  12
     * 1280x720	                  12
     * 1024x1024	              14
     */
    private Integer width;

    /**
     * 图片的高度
     * 分辨率（width * height）	图点数(计费规则)
     * 512x512	                  6
     * 640x360	                  6
     * 640x480	                  6
     * 640x640	                  7
     * 680x512	                  7
     * 512x680	                  7
     * 768x768	                  8
     * 720x1280	                  12
     * 1280x720	                  12
     * 1024x1024	              14
     */
    private Integer height;

    /**
     * 角色对话内容
     * 不得超过1000个字符
     */
    private List<RoleContent> messages;

    public ImageGenParam(Builder builder) {
        this.width = builder.width;
        this.height = builder.height;
        this.messages = builder.messages;
    }

    public ImageGenParam() {
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

    public List<RoleContent> getMessages() {
        return messages;
    }

    public void setMessages(List<RoleContent> messages) {
        this.messages = messages;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private Integer width;
        private Integer height;
        private List<RoleContent> messages;

        private Builder() {
        }

        public ImageGenParam build() {
            return new ImageGenParam(this);
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder messages(List<RoleContent> messages) {
            this.messages = messages;
            return this;
        }
    }
}
