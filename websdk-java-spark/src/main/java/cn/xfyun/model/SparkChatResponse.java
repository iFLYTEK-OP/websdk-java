package cn.xfyun.model;

import java.util.List;

/**
 * @author: rblu2
 * @create: 2025-02-20 10:31
 **/
public class SparkChatResponse {
    
    private Header header;
    private Payload payload;

    public Header getHeader() {
        return header;
    }

    public SparkChatResponse header(Header header) {
        this.header = header;
        return this;
    }

    public Payload getPayload() {
        return payload;
    }

    public SparkChatResponse payload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public static class Header {
        private String code;
        private String message;
        private String sid;
        private Integer status;

        public String getCode() {
            return code;
        }

        public Header code(String code) {
            this.code = code;
            return this;
        }

        public String getMessage() {
            return message;
        }

        public Header message(String message) {
            this.message = message;
            return this;
        }

        public String getSid() {
            return sid;
        }

        public Header sid(String sid) {
            this.sid = sid;
            return this;
        }

        public Integer getStatus() {
            return status;
        }

        public Header status(Integer status) {
            this.status = status;
            return this;
        }
    }
    
    public static class Payload {

        private Choices choices;

        private Usage usage;

        public Choices getChoices() {
            return choices;
        }

        public Payload choices(Choices choices) {
            this.choices = choices;
            return this;
        }

        public Usage getUsage() {
            return usage;
        }

        public Payload usage(Usage usage) {
            this.usage = usage;
            return this;
        }

        public static class Choices {
            private Integer status;
            private Integer seq;
            
            private List<Data> text;

            public Integer getStatus() {
                return status;
            }

            public Choices status(Integer status) {
                this.status = status;
                return this;
            }

            public Integer getSeq() {
                return seq;
            }

            public Choices seq(Integer seq) {
                this.seq = seq;
                return this;
            }

            public List<Data> getText() {
                return text;
            }

            public Choices text(List<Data> text) {
                this.text = text;
                return this;
            }

            public static class Data {
                private String content;
                private String role;
                private Integer index;

                public String getContent() {
                    return content;
                }

                public Data content(String content) {
                    this.content = content;
                    return this;
                }

                public String getRole() {
                    return role;
                }

                public Data role(String role) {
                    this.role = role;
                    return this;
                }

                public Integer getIndex() {
                    return index;
                }

                public Data index(Integer index) {
                    this.index = index;
                    return this;
                }
            }

        }

        public static class Usage {
            private Text text;

            public Text getText() {
                return text;
            }

            public Usage text(Text text) {
                this.text = text;
                return this;
            }

            public static class Text {
                private Integer question_tokens;
                private Integer prompt_tokens;

                private Integer completion_tokens;
                private Integer total_tokens;

                public Integer getQuestion_tokens() {
                    return question_tokens;
                }

                public Text question_tokens(Integer question_tokens) {
                    this.question_tokens = question_tokens;
                    return this;
                }

                public Integer getPrompt_tokens() {
                    return prompt_tokens;
                }

                public Text prompt_tokens(Integer prompt_tokens) {
                    this.prompt_tokens = prompt_tokens;
                    return this;
                }

                public Integer getCompletion_tokens() {
                    return completion_tokens;
                }

                public Text completion_tokens(Integer completion_tokens) {
                    this.completion_tokens = completion_tokens;
                    return this;
                }

                public Integer getTotal_tokens() {
                    return total_tokens;
                }

                public Text total_tokens(Integer total_tokens) {
                    this.total_tokens = total_tokens;
                    return this;
                }
            }
        }
    }
    

}
