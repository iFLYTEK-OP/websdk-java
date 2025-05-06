package cn.xfyun.model;

/**
 * 大模型会话message实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class RoleContent {

    private String role;
    private String content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
