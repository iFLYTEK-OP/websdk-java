package cn.xfyun.model.voiceclone.request;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

import java.io.File;

/**
 * 添加音频文件请求实体类
 *
 * @author zyding6
 **/
public class AudioAddParam {

    /**
     * 训练任务唯一id
     */
    private String taskId;

    /**
     * 上传的音频文件
     * 音频要求：
     * 1、音频格式限制wav、mp3、m4a、pcm，推荐使用无压缩wav格式
     * 2、单通道，采样率24k及以上，位深度16bit，时长无严格限制，音频大小限制3M。
     */
    private File file;

    /**
     * 文件上传的url地址, 必须是http/https开头，以mp3/wav/m4a/pcm结尾
     */
    private String audioUrl;

    /**
     * 文本ID, 可使用通用训练文本(textId=5001)
     */
    private Long textId;

    /**
     * 训练样例文本段落ID, 例：1, 2, 3 ……
     */
    private Long textSegId;

    public AudioAddParam(Builder builder) {
        this.taskId = builder.taskId;
        this.textId = builder.textId;
        this.textSegId = builder.textSegId;
        this.audioUrl = builder.audioUrl;
        this.file = builder.file;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getAudioUrl() {
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

    public Long getTextId() {
        return textId;
    }

    public void setTextId(Long textId) {
        this.textId = textId;
    }

    public Long getTextSegId() {
        return textSegId;
    }

    public void setTextSegId(Long textSegId) {
        this.textSegId = textSegId;
    }

    public String toJsonString() {
        return StringUtils.gson.toJson(this);
    }

    public void selfCheckFile() {
        if (StringUtils.isNullOrEmpty(taskId)) {
            throw new BusinessException("任务ID不能为空");
        } else if (null == file) {
            throw new BusinessException("媒体文件不能为空");
        } else if (null == textId) {
            throw new BusinessException("文本ID不能为空");
        } else if (null == textSegId) {
            throw new BusinessException("文本段落ID不能为空");
        }
    }

    public void selfCheckUrl() {
        if (StringUtils.isNullOrEmpty(taskId)) {
            throw new BusinessException("任务ID不能为空");
        } else if (null == audioUrl) {
            throw new BusinessException("媒体文件url不能为空");
        } else if (null == textId) {
            throw new BusinessException("文本ID不能为空");
        } else if (null == textSegId) {
            throw new BusinessException("文本段落ID不能为空");
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String taskId;
        private Long textId;
        private Long textSegId;
        private String audioUrl;
        private File file;

        public AudioAddParam build() {
            return new AudioAddParam(this);
        }

        private Builder() {
        }

        public Builder taskId(String taskId) {
            this.taskId = taskId;
            return this;
        }

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder audioUrl(String audioUrl) {
            this.audioUrl = audioUrl;
            return this;
        }

        public Builder textId(Long textId) {
            this.textId = textId;
            return this;
        }

        public Builder textSegId(Long textSegId) {
            this.textSegId = textSegId;
            return this;
        }
    }
}
