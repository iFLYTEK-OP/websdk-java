package cn.xfyun.model.aiui.knowledge;

import cn.xfyun.exception.BusinessException;
import cn.xfyun.util.StringUtils;

/**
 * 个性化知识库删除请求入参
 *
 * @author <zyding6@ifytek.com>
 **/
public class DeleteParam {

    private Long uid;
    private String groupId;
    private String docId;
    private String sid;
    private String repoId;

    public DeleteParam(Builder builder) {
        this.uid = builder.uid;
        this.groupId = builder.groupId;
        this.docId = builder.docId;
        this.sid = builder.sid;
        this.repoId = builder.repoId;
    }

    public DeleteParam() {
    }

    public String getRepoId() {
        return repoId;
    }

    public void setRepoId(String repoId) {
        this.repoId = repoId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDocId() {
        return docId;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String toJSONString() {
        return StringUtils.gson.toJson(this);
    }

    public void deleteCheck() {
        if (null == uid) {
            throw new BusinessException("uid不能为空");
        }
    }

    public static final class Builder {

        private Long uid;
        private String groupId;
        private String docId;
        private String sid;
        private String repoId;

        private Builder() {
        }

        public DeleteParam build() {
            return new DeleteParam(this);
        }

        public Builder uid(Long uid) {
            this.uid = uid;
            return this;
        }

        public Builder docId(String docId) {
            this.docId = docId;
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

        public Builder repoId(String repoId) {
            this.repoId = repoId;
            return this;
        }
    }
}
