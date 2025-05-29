package cn.xfyun.model.translate;

/**
 * 翻译请求参数
 *
 * @author <zyding6@ifytek.com>
 */
public class TransParam {

    /**
     * 需要翻译的文本
     */
    private String text;

    /**
     * 源语种
     */
    private String from;

    /**
     * 翻译语种
     */
    private String to;

    /**
     * 个性化术语ID
     */
    private String resId;

    public TransParam() {
    }

    public TransParam(Builder builder) {
        this.text = builder.text;
        this.from = builder.from;
        this.to = builder.to;
        this.resId = builder.resId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String text;
        private String from = "cn";
        private String to = "en";
        private String resId;

        private Builder() {
        }

        public TransParam build() {
            return new TransParam(this);
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder from(String from) {
            this.from = from;
            return this;
        }

        public Builder to(String to) {
            this.to = to;
            return this;
        }

        public Builder resId(String resId) {
            this.resId = resId;
            return this;
        }
    }
}
