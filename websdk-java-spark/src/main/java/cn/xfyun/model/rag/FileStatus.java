package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 知识库文件上传状态请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class FileStatus {

    /**
     * 上传的文件id列表，多个文档id用英文逗号分割
     */
    private String fileIds;

    public FileStatus(Builder builder) {
        this.fileIds = builder.fileIds;
    }

    public FileStatus() {
    }

    public String getFileIds() {
        return fileIds;
    }

    public void setFileIds(String fileIds) {
        this.fileIds = fileIds;
    }

    public RequestBody toFormDataBody() {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (!StringUtils.isNullOrEmpty(fileIds)) {
            builder.addFormDataPart("fileIds", fileIds);
        }
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private String fileIds;

        private Builder() {
        }

        public FileStatus build() {
            return new FileStatus(this);
        }

        public Builder fileIds(String fileIds) {
            this.fileIds = fileIds;
            return this;
        }
    }
}
