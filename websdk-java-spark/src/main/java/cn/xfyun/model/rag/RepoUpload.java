package cn.xfyun.model.rag;

import cn.xfyun.util.StringUtils;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;

/**
 * 知识库上传请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class RepoUpload {

    /**
     * 目前支持 doc/docx、pdf、md、txt 格式，单文件大小不超过 20MB，不超过 100W 字符。
     */
    private File file;

    /**
     * 文件 url （文件和文件 url 必须有一个）
     */
    private String url;

    /**
     * 文件名称，带后缀。文件用 url 的方式，该字段必传；
     * 传 file 的话，该字段可不传
     */
    private String fileName;

    /**
     * 文件类型，目前传固定值"wiki"
     */
    private String fileType;

    /**
     * 文件解析类型，"AUTO"-服务端智能判断是否需要走OCR，
     * "TEXT"-直接读取文件文本内容，"OCR"-强制走OCR，目前仅pdf、word支持
     */
    private String parseType;

    /**
     * 是否分步处理，true代表文件上传之后，服务只做了分片，
     * 这时候还不能去问答。需要业务做分片的确认，然后调用【文档向量化】接口，发起切片向量。待向量完成，即可问答。
     */
    private Boolean stepByStep;

    /**
     * 文件状态回调地址，文件状态有变动时服务会调用该 url。
     * 调用的时候会带上鉴权头，鉴权方式同【接口鉴权】，业务可根据需要是否做鉴权校验
     */
    private String callbackUrl;

    /**
     * 文件拆分扩展字段(转为JSONString)，也可在分步时，单独调用拆分接口重新发起拆分
     */
    private String extend;

    public RepoUpload(Builder builder) {
        this.file = builder.file;
        this.url = builder.url;
        this.fileName = builder.fileName;
        this.fileType = builder.fileType;
        this.parseType = builder.parseType;
        this.stepByStep = builder.stepByStep;
        this.callbackUrl = builder.callbackUrl;
        this.extend = builder.extend;
    }

    public RepoUpload() {
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getParseType() {
        return parseType;
    }

    public void setParseType(String parseType) {
        this.parseType = parseType;
    }

    public Boolean getStepByStep() {
        return stepByStep;
    }

    public void setStepByStep(Boolean stepByStep) {
        this.stepByStep = stepByStep;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public RequestBody toFormDataBody() {
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (null != file) {
            builder.addFormDataPart("file", file.getName() ,
                    RequestBody.create(MultipartBody.FORM, file));
        }
        if (!StringUtils.isNullOrEmpty(url)) {
            builder.addFormDataPart("url", url);
        }
        if (!StringUtils.isNullOrEmpty(fileName)) {
            builder.addFormDataPart("fileName", fileName);
        }
        if (!StringUtils.isNullOrEmpty(fileType)) {
            builder.addFormDataPart("fileType", fileType);
        }
        if (!StringUtils.isNullOrEmpty(parseType)) {
            builder.addFormDataPart("parseType", parseType);
        }
        if (null != stepByStep) {
            builder.addFormDataPart("stepByStep", String.valueOf(stepByStep));
        }
        if (!StringUtils.isNullOrEmpty(callbackUrl)) {
            builder.addFormDataPart("callbackUrl", callbackUrl);
        }
        if (!StringUtils.isNullOrEmpty(extend)) {
            builder.addFormDataPart("extend", extend);
        }
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {

        private File file;
        private String url;
        private String fileName;
        private String fileType = "wiki";
        private String parseType = "AUTO";
        private Boolean stepByStep;
        private String callbackUrl;
        private String extend;

        private Builder() {
        }

        public RepoUpload build() {
            return new RepoUpload(this);
        }

        public Builder file(File file) {
            this.file = file;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder fileType(String fileType) {
            this.fileType = fileType;
            return this;
        }

        public Builder parseType(String parseType) {
            this.parseType = parseType;
            return this;
        }

        public Builder stepByStep(Boolean stepByStep) {
            this.stepByStep = stepByStep;
            return this;
        }

        public Builder callbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
            return this;
        }

        public Builder extend(String extend) {
            this.extend = extend;
            return this;
        }
    }
}
