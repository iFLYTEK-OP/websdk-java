package cn.xfyun.model.aiui.knowledge.response;

/**
 * 创建知识库返回数据
 *
 * @author <zyding6@ifytek.com>
 */
public class GroupData {

    private String groupId;
    private String description;
    private String updateTime;
    private Integer capacity;
    private Long uid;
    private Integer repoType;
    private String createTime;
    private Integer isTop;
    private String name;
    private Integer id;
    private String repoConfig;
    private String fromSource;
    private Integer status;

    // Getters and Setters
    public String getGroupId() { return groupId; }
    public void setGroupId(String groupId) { this.groupId = groupId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUpdateTime() { return updateTime; }
    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Long getUid() { return uid; }
    public void setUid(Long uid) { this.uid = uid; }

    public Integer getRepoType() { return repoType; }
    public void setRepoType(Integer repoType) { this.repoType = repoType; }

    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }

    public Integer getIsTop() { return isTop; }
    public void setIsTop(Integer isTop) { this.isTop = isTop; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRepoConfig() { return repoConfig; }
    public void setRepoConfig(String repoConfig) { this.repoConfig = repoConfig; }

    public String getFromSource() { return fromSource; }
    public void setFromSource(String fromSource) { this.fromSource = fromSource; }

    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}