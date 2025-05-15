package cn.xfyun.model.maas.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 大模型微调响应体
 *
 * @author zyding6
 **/
public class MaasResponse {


    /**
     * header : {"code":0,"message":"Success","sid":"cht000704fa@dx16ade44e4d87a1c802","status":0}
     * payload : {"choices":{"status":2,"seq":0,"text":[{"content":"xxxxs","index":0,"role":"assistant"}]},"usage":{"text":{"completion_tokens":0,"question_tokens":0,"prompt_tokens":0,"total_tokens":0}}}
     */

    private Header header;
    private Payload payload;

    public MaasResponse(int code, String message) {
        this.header = new Header();
        this.header.code = code;
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
        /**
         * code : 0
         * message : Success
         * sid : cht000704fa@dx16ade44e4d87a1c802
         * status : 0
         */

        private int code;
        private String message;
        private String sid;
        private int status;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }

    public static class Payload {
        /**
         * choices : {"status":2,"seq":0,"text":[{"content":"xxxxs","index":0,"role":"assistant"}]}
         * usage : {"text":{"completion_tokens":0,"question_tokens":0,"prompt_tokens":0,"total_tokens":0}}
         */

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
            /**
             * status : 2
             * seq : 0
             * text : [{"content":"xxxxs","index":0,"role":"assistant"}]
             */

            private int status;
            private int seq;
            private List<Text> text;

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public int getSeq() {
                return seq;
            }

            public void setSeq(int seq) {
                this.seq = seq;
            }

            public List<Text> getText() {
                return text;
            }

            public void setText(List<Text> text) {
                this.text = text;
            }

            public static class Text {
                /**
                 * content : xxxxs
                 * index : 0
                 * role : assistant
                 */

                private String content;
                private int index;
                private String role;
                @SerializedName("reasoning_content")
                private String reasoningContent;

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }

                public int getIndex() {
                    return index;
                }

                public void setIndex(int index) {
                    this.index = index;
                }

                public String getRole() {
                    return role;
                }

                public void setRole(String role) {
                    this.role = role;
                }

                public String getReasoningContent() {
                    return reasoningContent;
                }

                public void setReasoningContent(String reasoningContent) {
                    this.reasoningContent = reasoningContent;
                }
            }
        }

        public static class Usage {
            /**
             * text : {"completion_tokens":0,"question_tokens":0,"prompt_tokens":0,"total_tokens":0}
             */

            private Text text;

            public Text getText() {
                return text;
            }

            public void setText(Text text) {
                this.text = text;
            }

            public static class Text {
                /**
                 * completion_tokens : 0
                 * question_tokens : 0
                 * prompt_tokens : 0
                 * total_tokens : 0
                 */

                @SerializedName("completion_tokens")
                private int completionTokens;
                @SerializedName("question_tokens")
                private int questionTokens;
                @SerializedName("prompt_tokens")
                private int promptTokens;
                @SerializedName("total_tokens")
                private int totalTokens;

                public int getCompletionTokens() {
                    return completionTokens;
                }

                public void setCompletionTokens(int completionTokens) {
                    this.completionTokens = completionTokens;
                }

                public int getQuestionTokens() {
                    return questionTokens;
                }

                public void setQuestionTokens(int questionTokens) {
                    this.questionTokens = questionTokens;
                }

                public int getPromptTokens() {
                    return promptTokens;
                }

                public void setPromptTokens(int promptTokens) {
                    this.promptTokens = promptTokens;
                }

                public int getTotalTokens() {
                    return totalTokens;
                }

                public void setTotalTokens(int totalTokens) {
                    this.totalTokens = totalTokens;
                }
            }
        }
    }
}
