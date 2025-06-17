package cn.xfyun.model.document.request;

import cn.xfyun.model.common.request.RequestHeader;
import com.google.gson.annotations.SerializedName;

/**
 * @program: websdk-java
 * @description: 图片文档还原请求实体类
 * @author: zyding6
 * @create: 2025/3/25 9:37
 **/
public class PDRecRequest {

    private Payload payload;
    
    private RequestHeader header;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public RequestHeader getHeader() {
        return header;
    }

    public void setHeader(RequestHeader header) {
        this.header = header;
    }

    public static class Payload {

        private Test test;

        public Test getTest() {
            return test;
        }

        public void setTest(Test test) {
            this.test = test;
        }

        public static class Test {

            private String encoding;
            private String image;
            private int status;

            public String getEncoding() {
                return encoding;
            }

            public void setEncoding(String encoding) {
                this.encoding = encoding;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }
        }
    }
}
