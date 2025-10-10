package cn.xfyun.model.sparkmodel;

import java.util.List;

/**
 * 大模型工具实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class FunctionCall {

    /**
     * function名称
     * 用户输入命中后，会返回该名称
     */
    private String name;

    /**
     * function功能描述
     * 描述function功能即可，越详细越有助于大模型理解该function
     */
    private String description;

    /**
     * function参数列表
     */
    private Parameters parameters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Parameters getParameters() {
        return parameters;
    }

    public void setParameters(Parameters parameters) {
        this.parameters = parameters;
    }

    public static class Parameters {

        /**
         * 参数类型
         */
        private String type;

        /**
         * 参数信息描述
         * 该内容由用户定义，命中该方法时需要返回哪些参数
         */
        private Object properties;

        /**
         * 必须返回的参数列表
         * 该内容由用户定义，命中方法时必须返回的字段
         */
        private List<String> required;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Object getProperties() {
            return properties;
        }

        public void setProperties(Object properties) {
            this.properties = properties;
        }

        public List<String> getRequired() {
            return required;
        }

        public void setRequired(List<String> required) {
            this.required = required;
        }

        public static class Field {

            /**
             * 字段类型
             */
            private String type;

            /**
             * 字段描述
             */
            private String description;

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }
        }
    }
}
