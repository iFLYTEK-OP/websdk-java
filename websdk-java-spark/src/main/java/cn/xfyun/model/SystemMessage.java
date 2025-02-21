package cn.xfyun.model;

/**
 * @author: rblu2
 * @desc:
 * @create: 2025-02-18 19:33
 **/

public class SystemMessage extends RoleMessage {

    public SystemMessage(String content) {
        super("system", content);
    }

    public static SystemMessage crate(String content) {
        return new SystemMessage(content);
    }

}

