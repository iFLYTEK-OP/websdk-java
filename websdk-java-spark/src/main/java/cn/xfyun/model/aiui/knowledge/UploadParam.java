package cn.xfyun.model.aiui.knowledge;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.List;

/**
 * 个性化知识库上传请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class UploadParam {

    private Long uid;
    private String sid;
    private String groupId;
    private List<File> files;
    private String labels;
    private List<FileInfo> fileList;
    private ParseConfig parseConfig;

    public UploadParam(Builder builder) {
        this.uid = builder.uid;
        this.sid = builder.sid;
        this.groupId = builder.groupId;
        this.files = builder.files;
        this.labels = builder.labels;
        this.fileList = builder.fileList;
        this.parseConfig = builder.parseConfig;
    }

    public UploadParam() {
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public String getLabels() {
        return labels;
    }

    public void setLabels(String labels) {
        this.labels = labels;
    }

    public List<FileInfo> getFileList() {
        return fileList;
    }

    public void setFileList(List<FileInfo> fileList) {
        this.fileList = fileList;
    }

    public ParseConfig getParseConfig() {
        return parseConfig;
    }

    public void setParseConfig(ParseConfig parseConfig) {
        this.parseConfig = parseConfig;
    }

    public void uploadCheck() {
        if (null == uid) {
            throw new BusinessException("uid不能为空");
        } else if (StringUtils.isNullOrEmpty(groupId)) {
            throw new BusinessException("groupId不能为空");
        } else if ((null == files || files.isEmpty()) && (null == fileList || fileList.isEmpty())) {
            throw new BusinessException("files和fileList不能同时为空");
        }
    }

    public RequestBody toFormDataBody() {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (null != uid) {
            builder.addFormDataPart("uid", String.valueOf(uid));
        }
        if (!StringUtils.isNullOrEmpty(sid)) {
            builder.addFormDataPart("sid", sid);
        }
        if (null != files && !files.isEmpty()) {
            for (File file : files) {
                builder.addFormDataPart("file", file.getName(),
                        RequestBody.create(MultipartBody.FORM, file));
            }
        } else if (null != fileList && !fileList.isEmpty()) {
            builder.addFormDataPart("fileListStr", StringUtils.gson.toJson(fileList));
        }
        if (!StringUtils.isNullOrEmpty(groupId)) {
            builder.addFormDataPart("groupId", groupId);
        }
        if (!StringUtils.isNullOrEmpty(labels)) {
            builder.addFormDataPart("labels", labels);
        }
        if (null != parseConfig) {
            builder.addFormDataPart("parseConfig", StringUtils.gson.toJson(parseConfig));
        }
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public static class FileInfo {

        private String fileName;
        private String filePath;
        private Long fileSize;

        public FileInfo() {
        }

        public FileInfo(String fileName, String filePath, Long fileSize) {
            this.fileName = fileName;
            this.filePath = filePath;
            this.fileSize = fileSize;
        }

        public String getFileName() {
            return fileName;
        }

        public void setFileName(String fileName) {
            this.fileName = fileName;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public Long getFileSize() {
            return fileSize;
        }

        public void setFileSize(Long fileSize) {
            this.fileSize = fileSize;
        }
    }

    public static class ParseConfig {

        private String chunkType;
        private String separator;
        private String cutLevel;
        private String lengthRange;
        private String cutOff;

        public ParseConfig() {
        }

        public ParseConfig(String chunkType, String separator, String cutLevel, String lengthRange, String cutOff) {
            this.chunkType = chunkType;
            this.separator = separator;
            this.cutLevel = cutLevel;
            this.lengthRange = lengthRange;
            this.cutOff = cutOff;
        }

        public String getChunkType() {
            return chunkType;
        }

        public void setChunkType(String chunkType) {
            this.chunkType = chunkType;
        }

        public String getSeparator() {
            return separator;
        }

        public void setSeparator(String separator) {
            this.separator = separator;
        }

        public String getCutLevel() {
            return cutLevel;
        }

        public void setCutLevel(String cutLevel) {
            this.cutLevel = cutLevel;
        }

        public String getLengthRange() {
            return lengthRange;
        }

        public void setLengthRange(String lengthRange) {
            this.lengthRange = lengthRange;
        }

        public String getCutOff() {
            return cutOff;
        }

        public void setCutOff(String cutOff) {
            this.cutOff = cutOff;
        }
    }

    public static final class Builder {

        private Long uid;
        private String sid;
        private String groupId;
        private List<File> files;
        private String labels;
        private List<FileInfo> fileList;
        private ParseConfig parseConfig;

        private Builder() {
        }

        public UploadParam build() {
            return new UploadParam(this);
        }

        public Builder uid(long uid) {
            this.uid = uid;
            return this;
        }

        public Builder sid(String sid) {
            this.sid = sid;
            return this;
        }

        public Builder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public Builder files(List<File> files) {
            this.files = files;
            return this;
        }

        public Builder labels(String labels) {
            this.labels = labels;
            return this;
        }

        public Builder fileList(List<FileInfo> fileList) {
            this.fileList = fileList;
            return this;
        }

        public Builder parseConfig(ParseConfig parseConfig) {
            this.parseConfig = parseConfig;
            return this;
        }
    }
}
