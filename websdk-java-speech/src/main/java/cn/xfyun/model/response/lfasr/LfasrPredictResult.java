package cn.xfyun.model.response.lfasr;

import java.io.Serializable;
import java.util.List;

/**
 * 质检结果实体类
 */
public class LfasrPredictResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 关键词相关信息
     */
    private List<KeyWord> keywords;
    
    public List<KeyWord> getKeywords() {
        return keywords;
    }
    
    public void setKeywords(List<KeyWord> keywords) {
        this.keywords = keywords;
    }
    
    @Override
    public String toString() {
        return "PredictResult{" +
                "keywords=" + keywords +
                '}';
    }
    
    /**
     * 关键词信息
     */
    public static class KeyWord implements Serializable {
        
        private static final long serialVersionUID = 1L;
        
        /**
         * 质检关键词内容
         */
        private String word;
        
        /**
         * 词库标签信息
         */
        private String label;
        
        /**
         * 质检关键词出现位置时间戳信息
         */
        private List<TimeStamp> timeStamp;
        
        public String getWord() {
            return word;
        }
        
        public void setWord(String word) {
            this.word = word;
        }
        
        public String getLabel() {
            return label;
        }
        
        public void setLabel(String label) {
            this.label = label;
        }
        
        public List<TimeStamp> getTimeStamp() {
            return timeStamp;
        }
        
        public void setTimeStamp(List<TimeStamp> timeStamp) {
            this.timeStamp = timeStamp;
        }
        
        @Override
        public String toString() {
            return "KeyWord{" +
                    "word='" + word + '\'' +
                    ", label='" + label + '\'' +
                    ", timeStamp=" + timeStamp +
                    '}';
        }
        
        /**
         * 时间戳信息
         */
        public static class TimeStamp implements Serializable {
            
            private static final long serialVersionUID = 1L;
            
            /**
             * 词出现的开启位置时间戳
             */
            private Long bg;
            
            /**
             * 词出现的结束位置时间戳
             */
            private Long ed;
            
            public Long getBg() {
                return bg;
            }
            
            public void setBg(Long bg) {
                this.bg = bg;
            }
            
            public Long getEd() {
                return ed;
            }
            
            public void setEd(Long ed) {
                this.ed = ed;
            }
            
            @Override
            public String toString() {
                return "TimeStamp{" +
                        "bg=" + bg +
                        ", ed=" + ed +
                        '}';
            }
        }
    }
}