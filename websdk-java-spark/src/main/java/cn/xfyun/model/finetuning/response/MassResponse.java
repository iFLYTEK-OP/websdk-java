package cn.xfyun.model.finetuning.response;

import java.util.List;

/**
 * @program: websdk-java
 * @description:
 * @author: zyding6
 * @create: 2025/3/17 16:12
 **/
public class MassResponse {


    /**
     * header : {"code":0,"message":"Success","sid":"cht000704fa@dx16ade44e4d87a1c802","status":0}
     * payload : {"choices":{"status":2,"seq":0,"text":[{"content":"xxxxs","index":0,"role":"assistant"}]},"usage":{"text":{"completion_tokens":0,"question_tokens":0,"prompt_tokens":0,"total_tokens":0}}}
     */

    private Header header;
    private Payload payload;

    public MassResponse(int code, String message) {
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
                private String reasoning_content;

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

                public String getReasoning_content() {
                    return reasoning_content;
                }

                public void setReasoning_content(String reasoning_content) {
                    this.reasoning_content = reasoning_content;
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

                private int completion_tokens;
                private int question_tokens;
                private int prompt_tokens;
                private int total_tokens;

                public int getCompletion_tokens() {
                    return completion_tokens;
                }

                public void setCompletion_tokens(int completion_tokens) {
                    this.completion_tokens = completion_tokens;
                }

                public int getQuestion_tokens() {
                    return question_tokens;
                }

                public void setQuestion_tokens(int question_tokens) {
                    this.question_tokens = question_tokens;
                }

                public int getPrompt_tokens() {
                    return prompt_tokens;
                }

                public void setPrompt_tokens(int prompt_tokens) {
                    this.prompt_tokens = prompt_tokens;
                }

                public int getTotal_tokens() {
                    return total_tokens;
                }

                public void setTotal_tokens(int total_tokens) {
                    this.total_tokens = total_tokens;
                }
            }
        }
    }
}
