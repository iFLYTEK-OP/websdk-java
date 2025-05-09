package cn.xfyun.model.sparkmodel;

import java.util.List;

/**
 * 大模型会话带文件实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class FileContent {

    private String role;
    private Object content;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public static class Content {

        private String type;
        private List<String> file;
        private String text;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public List<String> getFile() {
            return file;
        }

        public void setFile(List<String> file) {
            this.file = file;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
