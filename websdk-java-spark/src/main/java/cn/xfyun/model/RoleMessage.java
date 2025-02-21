package cn.xfyun.model;

/**
 * @author: rblu2
 * @desc:
 * @create: 2025-02-18 19:33
 **/

public abstract class RoleMessage {

    String role;

    String content;

    protected RoleMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }




}

