package cn.xfyun.model;

/**
 * @author: rblu2
 * @desc:
 * @create: 2025-02-18 19:33
 **/

public class UserMessage extends RoleMessage {

    public UserMessage(String content) {
        super("user", content);
    }

    public static UserMessage crate(String content) {
        return new UserMessage(content);
    }

}

