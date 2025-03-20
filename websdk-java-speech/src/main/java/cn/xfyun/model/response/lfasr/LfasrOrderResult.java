package cn.xfyun.model.response.lfasr;

import java.io.Serializable;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

/**
 * 语音转写结果
 *
 * @author kaili23
 */
public class LfasrOrderResult implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(LfasrOrderResult.class);

    private static final long serialVersionUID = 1L;

    /**
     * 做顺滑功能的识别结果
     */
    private List<Lattice> lattice;

    /**
     * 未做顺滑功能的识别结果，当开启顺滑和后语规整后才返回
     */
    private List<Lattice> lattice2;

    /**
     * 转写结果标签信息，用于补充转写结果相关信息
     */
    private Label label;

    public List<Lattice> getLattice() {
        return lattice;
    }

    public void setLattice(List<Lattice> lattice) {
        this.lattice = lattice;
    }

    public List<Lattice> getLattice2() {
        return lattice2;
    }

    public void setLattice2(List<Lattice> lattice2) {
        this.lattice2 = lattice2;
    }

    public Label getLabel() {
        return label;
    }

    public void setLabel(Label label) {
        this.label = label;
    }

    public static class Lattice implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 单个VAD的结果的内容（lattice中返回的是字符串，lattice2中返回的是Json对象）
         */
        @SerializedName("json_1best")
        private Object json1BestRaw;

        public void setJson1BestRaw(Object json1BestRaw) {
            this.json1BestRaw = json1BestRaw;
        }

        public Object getJson1BestRaw() {
            return json1BestRaw;
        }

        public Json1Best getJson1Best() {
            if (json1BestRaw == null) {
                return null;
            }

            try {
                Gson gson = new Gson();
                if (json1BestRaw instanceof String) {
                    // 直接解析JSON字符串
                    return gson.fromJson((String) json1BestRaw, Json1Best.class);
                } else if (json1BestRaw instanceof JsonObject) {
                    // JsonObject转换为目标对象
                    return gson.fromJson(json1BestRaw.toString(), Json1Best.class);
                } else {
                    // 其他类型先转JSON字符串再解析
                    return gson.fromJson(gson.toJson(json1BestRaw), Json1Best.class);
                }
            } catch (Exception e) {
                logger.error("解析json1Best失败, 原生数据: {}", json1BestRaw, e);
                return null;
            }
        }

    }

    /**
     * json_1best对象
     */
    public static class Json1Best implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 句子信息
         */
        private SentenceInfo st;

        public SentenceInfo getSt() {
            return st;
        }

        public void setSt(SentenceInfo st) {
            this.st = st;
        }

    }

    /**
     * 句子信息
     */
    public static class SentenceInfo implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 开始时间
         */
        private String bg;

        /**
         * 结束时间
         */
        private String ed;

        /**
         * 角色ID
         */
        private String rl;

        /**
         * 识别结果
         */
        private List<RecognitionResult> rt;

        public String getBg() {
            return bg;
        }

        public void setBg(String bg) {
            this.bg = bg;
        }

        public String getEd() {
            return ed;
        }

        public void setEd(String ed) {
            this.ed = ed;
        }

        public String getRl() {
            return rl;
        }

        public void setRl(String rl) {
            this.rl = rl;
        }

        public List<RecognitionResult> getRt() {
            return rt;
        }

        public void setRt(List<RecognitionResult> rt) {
            this.rt = rt;
        }

    }

    /**
     * 识别结果
     */
    public static class RecognitionResult implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 词语识别结果集合
         */
        private List<WordResult> ws;

        public List<WordResult> getWs() {
            return ws;
        }

        public void setWs(List<WordResult> ws) {
            this.ws = ws;
        }

    }

    /**
     * 词语识别结果
     */
    public static class WordResult implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 词语开始的帧数，位置是相对bg，仅支持中、英文语种
         */
        private Integer wb;

        /**
         * 词语结束的帧数，位置是相对bg，仅支持中、英文语种
         */
        private Integer we;

        /**
         * 词语候选识别结果集合
         */
        private List<CandidateWord> cw;

        public Integer getWb() {
            return wb;
        }

        public void setWb(Integer wb) {
            this.wb = wb;
        }

        public Integer getWe() {
            return we;
        }

        public void setWe(Integer we) {
            this.we = we;
        }

        public List<CandidateWord> getCw() {
            return cw;
        }

        public void setCw(List<CandidateWord> cw) {
            this.cw = cw;
        }

    }

    /**
     * 候选词
     */
    public static class CandidateWord implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 识别结果
         */
        private String w;

        /**
         * 词语的属性
         * n：正常词
         * s：顺滑
         * p：标点
         * g：分段
         */
        private String wp;

        private String wc;

        public String getW() {
            return w;
        }

        public void setW(String w) {
            this.w = w;
        }

        public String getWp() {
            return wp;
        }

        public void setWp(String wp) {
            this.wp = wp;
        }

        public String getWc() {
            return wc;
        }

        public void setWc(String wc) {
            this.wc = wc;
        }

    }

    /**
     * 标签信息
     */
    public static class Label implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 双通道模式转写结果中角色和音频轨道对应信息
         */
        @SerializedName("rl_track")
        private List<RoleTrack> rlTrack;

        public List<RoleTrack> getRlTrack() {
            return rlTrack;
        }

        public void setRlTrack(List<RoleTrack> rlTrack) {
            this.rlTrack = rlTrack;
        }

    }

    /**
     * 角色轨道信息
     */
    public static class RoleTrack implements Serializable {

        private static final long serialVersionUID = 1L;

        /**
         * 分离的角色编号，取值正整数
         */
        private String rl;

        /**
         * 音频轨道信息 L：左声道，R：右声道
         */
        private String track;

        public String getRl() {
            return rl;
        }

        public void setRl(String rl) {
            this.rl = rl;
        }

        public String getTrack() {
            return track;
        }

        public void setTrack(String track) {
            this.track = track;
        }

    }

}