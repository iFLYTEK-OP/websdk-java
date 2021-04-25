package cn.xfyun.model.response.iat;

import com.google.gson.JsonObject;

/**
 * @author <ydwang16@iflytek.com>
 * @description
 * @date 2021/3/24
 */
public class IatResult {
    /**
     * 保留字段，无需关心
     */
    int bg;

    /**
     * 保留字段，无需关心
     */
    int ed;

    /**
     * 返回结果的序号
     */
    int sn;

    /**
     * 听写结果
     */
    Ws[] ws;

    /**
     * 是否是最后一片结果
     */
    boolean ls;

    /**
     * 若设置了vinfo=1，还有vad返回（若同时开通并设置了dwa=wpgs，则vinfo失效）
     */
    JsonObject vad;

    /**
     * 开启wpgs会有此字段
     * 取值为 "apd"时表示该片结果是追加到前面的最终结果；取值为"rpl" 时表示替换前面的部分结果，替换范围为rg字段
     */
    String pgs;

    /**
     * 替换范围，开启wpgs会有此字段
     * 假设值为[2,5]，则代表要替换的是第2次到第5次返回的结果
     */
    int[] rg;

    public Text getText() {
        Text text = new Text();
        StringBuilder sb = new StringBuilder();
        for (Ws ws : this.ws) {
            sb.append(ws.cw[0].w);
        }
        text.sn = this.sn;
        text.text = sb.toString();
        text.sn = this.sn;
        text.rg = this.rg;
        text.pgs = this.pgs;
        text.bg = this.bg;
        text.ed = this.ed;
        text.ls = this.ls;
        text.vad = this.vad == null ? null : this.vad;
        return text;
    }

    public int getBg() {
        return bg;
    }

    public void setBg(int bg) {
        this.bg = bg;
    }

    public int getEd() {
        return ed;
    }

    public void setEd(int ed) {
        this.ed = ed;
    }

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
    }

    public Ws[] getWs() {
        return ws;
    }

    public void setWs(Ws[] ws) {
        this.ws = ws;
    }

    public boolean isLs() {
        return ls;
    }

    public void setLs(boolean ls) {
        this.ls = ls;
    }

    public JsonObject getVad() {
        return vad;
    }

    public void setVad(JsonObject vad) {
        this.vad = vad;
    }

    public String getPgs() {
        return pgs;
    }

    public void setPgs(String pgs) {
        this.pgs = pgs;
    }

    public int[] getRg() {
        return rg;
    }

    public void setRg(int[] rg) {
        this.rg = rg;
    }


    public static class Ws {
        /**
         * 中文分词
         */
        Cw[] cw;

        /**
         * 起始的端点帧偏移值，单位：帧（1帧=10ms）
         * 注：以下两种情况下bg=0，无参考意义：
         * 1)返回结果为标点符号或者为空；2)本次返回结果过长。
         */
        int bg;

        /**
         * 保留
         */
        int ed;

        public Cw[] getCw() {
            return cw;
        }

        public void setCw(Cw[] cw) {
            this.cw = cw;
        }

        public int getBg() {
            return bg;
        }

        public void setBg(int bg) {
            this.bg = bg;
        }

        public int getEd() {
            return ed;
        }

        public void setEd(int ed) {
            this.ed = ed;
        }
    }


    public static class Cw {
        /**
         * sc/wb/wc/we/wp均为保留字段，无需关心。如果解析sc字段，建议float与int数据类型都做兼容
         */
        int sc;

        /**
         * 字词
         */
        String w;

        public int getSc() {
            return sc;
        }

        public void setSc(int sc) {
            this.sc = sc;
        }

        public String getW() {
            return w;
        }

        public void setW(String w) {
            this.w = w;
        }
    }
}

