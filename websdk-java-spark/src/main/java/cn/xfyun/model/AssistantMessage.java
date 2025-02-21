package cn.xfyun.model;

/**
 * @author: rblu2
 * @desc:
 * @create: 2025-02-18 19:33
 **/

public class AssistantMessage extends RoleMessage {

    public AssistantMessage(String content) {
        super("assistant", content);
    }

    public static AssistantMessage crate(String content) {
        return new AssistantMessage(content);
    }

}

