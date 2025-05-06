package cn.xfyun.model.sparkmodel.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 图像理解返回响应实体类
 *
 * @author zyding6
 **/
public class ImageUnderstandResponse {

    private Header header;
    private Payload payload;

    public ImageUnderstandResponse(Integer i, String message) {
        this.header = new Header();
        this.header.code = i;
        this.header.message = message;
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public static class Header {

        private Integer code;
        private String message;
        private String sid;
        private Integer status;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public Integer getStatus() {
            return status;
        }

        public void setStatus(Integer status) {
            this.status = status;
        }
    }

    public static class Payload {

        private Choices choices;
        private Usage usage;

        public Choices getChoices() {
            return choices;
        }

        public void setChoices(Choices choices) {
            this.choices = choices;
        }

        public Usage getUsage() {
            return usage;
        }

        public void setUsage(Usage usage) {
            this.usage = usage;
        }

        public static class Choices {

            private Integer status;
            private Integer seq;
            private List<Text> text;

            public Integer getStatus() {
                return status;
            }

            public void setStatus(Integer status) {
                this.status = status;
            }

            public Integer getSeq() {
                return seq;
            }

            public void setSeq(Integer seq) {
                this.seq = seq;
            }

            public List<Text> getText() {
                return text;
            }

            public void setText(List<Text> text) {
                this.text = text;
            }

            public static class Text {

                private String content;
                private String role;
                private Integer index;
                @SerializedName("content_type")
                private String contentType;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getRole() {
                    return role;
                }

                public void setRole(String role) {
                    this.role = role;
                }

                public Integer getIndex() {
                    return index;
                }

                public void setIndex(Integer index) {
                    this.index = index;
                }

                public String getContentType() {
                    return contentType;
                }

                public void setContentType(String contentType) {
                    this.contentType = contentType;
                }

                public static class FunctionCall {

                    private String arguments;
                    private String name;

                    public String getArguments() {
                        return arguments;
                    }

                    public void setArguments(String arguments) {
                        this.arguments = arguments;
                    }

                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }
                }
            }
        }

        public static class Usage {

            private Text text;

            public Text getText() {
                return text;
            }

            public void setText(Text text) {
                this.text = text;
            }

            public static class Text {

                @SerializedName("question_tokens")
                private Integer questionTokens;
                @SerializedName("prompt_tokens")
                private Integer promptTokens;
                @SerializedName("completion_tokens")
                private Integer completionTokens;
                @SerializedName("total_tokens")
                private Integer totalTokens;

                public Integer getQuestionTokens() {
                    return questionTokens;
                }

                public void setQuestionTokens(Integer questionTokens) {
                    this.questionTokens = questionTokens;
                }

                public Integer getPromptTokens() {
                    return promptTokens;
                }

                public void setPromptTokens(Integer promptTokens) {
                    this.promptTokens = promptTokens;
                }

                public Integer getCompletionTokens() {
                    return completionTokens;
                }

                public void setCompletionTokens(Integer completionTokens) {
                    this.completionTokens = completionTokens;
                }

                public Integer getTotalTokens() {
                    return totalTokens;
                }

                public void setTotalTokens(Integer totalTokens) {
                    this.totalTokens = totalTokens;
                }
            }
        }

        public static class Plugin {

            private List<Text> text;

            public List<Text> getText() {
                return text;
            }

            public void setText(List<Text> text) {
                this.text = text;
            }

            public static class Text {
                /**
                 * name : ifly_search
                 * content : [{"index":1,"url":"","title":"曹操（中国东汉末年权臣，曹魏政权的奠基者）_百度百科"},{"index":2,"url":"","title":"曹操是哪一年出生的？ - 百度知道"},{"index":3,"url":"","title":"曹操的一生事迹简介-历代史历史网"},{"index":4,"url":"","title":"曹操生于哪一年? - 百度知道"},{"index":5,"url":"","title":"曹操（中國東漢末年權臣，曹魏政權的奠基者）_百度百科"}]
                 * content_type : text
                 * content_meta : null
                 * role : tool
                 * status : finished
                 * invoked : {"namespace":"ifly_search","plugin_id":"ifly_search","plugin_ver":"","status_code":200,"status_msg":"Success","type":"local"}
                 */

                private String name;
                private String content;
                @SerializedName("content_type")
                private String contentType;
                @SerializedName("content_meta")
                private Object contentMeta;
                private String role;
                private String status;
                private Invoked invoked;

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public String getContentType() {
                    return contentType;
                }

                public void setContentType(String contentType) {
                    this.contentType = contentType;
                }

                public Object getContentMeta() {
                    return contentMeta;
                }

                public void setContentMeta(Object contentMeta) {
                    this.contentMeta = contentMeta;
                }

                public String getRole() {
                    return role;
                }

                public void setRole(String role) {
                    this.role = role;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public Invoked getInvoked() {
                    return invoked;
                }

                public void setInvoked(Invoked invoked) {
                    this.invoked = invoked;
                }

                public static class Invoked {
                    /**
                     * namespace : ifly_search
                     * plugin_id : ifly_search
                     * plugin_ver :
                     * status_code : 200
                     * status_msg : Success
                     * type : local
                     */

                    private String namespace;
                    @SerializedName("plugin_id")
                    private String pluginId;
                    @SerializedName("plugin_ver")
                    private String pluginVer;
                    @SerializedName("status_code")
                    private Integer statusCode;
                    @SerializedName("status_msg")
                    private String statusMsg;
                    private String type;

                    public String getNamespace() {
                        return namespace;
                    }

                    public void setNamespace(String namespace) {
                        this.namespace = namespace;
                    }

                    public String getPluginId() {
                        return pluginId;
                    }

                    public void setPluginId(String pluginId) {
                        this.pluginId = pluginId;
                    }

                    public String getPluginVer() {
                        return pluginVer;
                    }

                    public void setPluginVer(String pluginVer) {
                        this.pluginVer = pluginVer;
                    }

                    public Integer getStatusCode() {
                        return statusCode;
                    }

                    public void setStatusCode(Integer statusCode) {
                        this.statusCode = statusCode;
                    }

                    public String getStatusMsg() {
                        return statusMsg;
                    }

                    public void setStatusMsg(String statusMsg) {
                        this.statusMsg = statusMsg;
                    }

                    public String getType() {
                        return type;
                    }

                    public void setType(String type) {
                        this.type = type;
                    }
                }
            }
        }
    }
}
