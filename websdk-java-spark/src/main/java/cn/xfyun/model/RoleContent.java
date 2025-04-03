package cn.xfyun.model;

/**
 * @description: 大模型会话message实体类
 * @author: zyding6
 * @create: 2025/3/17 17:38
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
