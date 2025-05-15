package cn.xfyun.model.voiceclone.request;

import cn.xfyun.util.StringUtils;

/**
 * 创建任务请求实体类
 *
 * @author zyding6
 **/
public class CreateTaskParam {

    /**
     * 创建任务名称, 默认””
     */
    private String taskName;

    /**
     * 性别, 1:男2:女, 默认1
     */
    private Integer sex;

    /**
     * 1:儿童、2:青年、3:中年、4:中老年, 默认1
     */
    private Integer ageGroup;

    /**
     * 用户标识, 默认””
     */
    private String thirdUser;

    /**
     * 训练的语种, 默认””
     * 中文：不传language参数，默认中文
     * 英：en
     * 日：jp
     * 韩：ko
     * 俄：ru
     */
    private String language;

    /**
     * 音库名称, 默认””
     */
    private String resourceName;

    /**
     * 音库类型
     * 12:一句话合成
     */
    private Integer resourceType;

    /**
     * 任务结果回调地址，训练结束时进行回调不穿默认不回调
     * 回调参数1            taskName	string	false	任务名称
     * 回调参数2            trainVid	string	true	音库id
     * 回调参数3            trainVcn	string	true	训练得到的音色id，后续根据该音色id进行音频合成
     * 回调参数4            resourceType	string	true	12：一句话
     * 回调参数5            taskId	string	true	任务唯一id
     * 回调参数6            trainStatus	string	true	-1训练中 0 失败 1成功 2草稿
     */
    private String callbackUrl;

    /**
     * 降噪开关, 默认0
     * 0: 关闭降噪 1:开启降噪
     */
    private Integer denoiseSwitch;

    /**
     * 范围0.0～5.0，单位0.1，默认0.0
     * 大于0，则开启音频检测。该值为对应的检测阈值,音频得分高于该值时将会生成音频特征
     */
    private Float mosRatio;

    public CreateTaskParam(Builder builder) {
        this.taskName = builder.taskName;
        this.sex = builder.sex;
        this.ageGroup = builder.ageGroup;
        this.thirdUser = builder.thirdUser;
        this.language = builder.language;
        this.resourceName = builder.resourceName;
        this.resourceType = builder.resourceType;
        this.callbackUrl = builder.callbackUrl;
        this.denoiseSwitch = builder.denoiseSwitch;
        this.mosRatio = builder.mosRatio;
    }

    public String getTaskName() {
        return taskName;
    }

    public Integer getSex() {
        return sex;
    }

    public Integer getAgeGroup() {
        return ageGroup;
    }

    public String getThirdUser() {
        return thirdUser;
    }

    public String getLanguage() {
        return language;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public void setAgeGroup(Integer ageGroup) {
        this.ageGroup = ageGroup;
    }

    public void setThirdUser(String thirdUser) {
        this.thirdUser = thirdUser;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getDenoiseSwitch() {
        return denoiseSwitch;
    }

    public void setDenoiseSwitch(Integer denoiseSwitch) {
        this.denoiseSwitch = denoiseSwitch;
    }

    public Float getMosRatio() {
        return mosRatio;
    }

    public void setMosRatio(Float mosRatio) {
        this.mosRatio = mosRatio;
    }

    public String toJsonString() {
        return StringUtils.gson.toJson(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String taskName = "";
        private Integer sex = 1;
        private Integer ageGroup = 1;
        private String thirdUser = "";
        private String language;
        private String resourceName = "";
        private Integer resourceType = 12;
        private String callbackUrl;
        private Integer denoiseSwitch;
        private Float mosRatio;

        public CreateTaskParam build() {
            return new CreateTaskParam(this);
        }

        private Builder() {
        }

        public Builder taskName(String taskName) {
            this.taskName = taskName;
            return this;
        }

        public Builder sex(Integer sex) {
            this.sex = sex;
            return this;
        }

        public Builder ageGroup(Integer ageGroup) {
            this.ageGroup = ageGroup;
            return this;
        }

        public Builder thirdUser(String thirdUser) {
            this.thirdUser = thirdUser;
            return this;
        }

        public Builder language(String language) {
            this.language = language;
            return this;
        }

        public Builder resourceName(String resourceName) {
            this.resourceName = resourceName;
            return this;
        }

        public Builder resourceType(Integer resourceType) {
            this.resourceType = resourceType;
            return this;
        }

        public Builder callbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        public Builder mosRatio(float mosRatio) {
            this.mosRatio = mosRatio;
            return this;
        }

        public Builder denoiseSwitch(int denoiseSwitch) {
            this.denoiseSwitch = denoiseSwitch;
            return this;
        }
    }
}
