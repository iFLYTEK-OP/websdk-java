package cn.xfyun.model.simult.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * 同声传译转写后文本实体类
 *
 * @author <zyding6@ifytek.com>
 */
public class Recognition {


    /**
     * bg : 200
     * ed : 800
     * ls : false
     * pgs : rpl
     * rg : [1,1]
     * sn : 2
     * sub_end : false
     * ws : [{"bg":20,"cw":[{"rl":0,"sc":0,"w":"科大讯飞","wb":20,"wc":0,"we":40,"wp":"n"}]},{"bg":40,"cw":[{"rl":0,"sc":0,"w":"是","wb":40,"wc":0,"we":60,"wp":"n"}]}]
     */

    private Integer bg;
    private Integer ed;
    private boolean ls;
    private String pgs;
    private Integer sn;
    @SerializedName("sub_end")
    private boolean subEnd;
    private List<Integer> rg;
    private List<Ws> ws;

    public Integer getBg() {
        return bg;
    }

    public void setBg(Integer bg) {
        this.bg = bg;
    }

    public Integer getEd() {
        return ed;
    }

    public void setEd(Integer ed) {
        this.ed = ed;
    }

    public boolean isLs() {
        return ls;
    }

    public void setLs(boolean ls) {
        this.ls = ls;
    }

    public String getPgs() {
        return pgs;
    }

    public void setPgs(String pgs) {
        this.pgs = pgs;
    }

    public Integer getSn() {
        return sn;
    }

    public void setSn(Integer sn) {
        this.sn = sn;
    }

    public boolean isSubEnd() {
        return subEnd;
    }

    public void setSubEnd(boolean subEnd) {
        this.subEnd = subEnd;
    }

    public List<Integer> getRg() {
        return rg;
    }

    public void setRg(List<Integer> rg) {
        this.rg = rg;
    }

    public List<Ws> getWs() {
        return ws;
    }

    public void setWs(List<Ws> ws) {
        this.ws = ws;
    }

    public static class Ws {
        /**
         * bg : 20
         * cw : [{"rl":0,"sc":0,"w":"科大讯飞","wb":20,"wc":0,"we":40,"wp":"n"}]
         */

        private Integer bg;
        private List<Cw> cw;

        public Integer getBg() {
            return bg;
        }

        public void setBg(Integer bg) {
            this.bg = bg;
        }

        public List<Cw> getCw() {
            return cw;
        }

        public void setCw(List<Cw> cw) {
            this.cw = cw;
        }

        public static class Cw {
            /**
             * rl : 0
             * sc : 0
             * w : 科大讯飞
             * wb : 20
             * wc : 0
             * we : 40
             * wp : n
             */

            private Integer rl;
            private Integer sc;
            private String w;
            private Integer wb;
            private Integer wc;
            private Integer we;
            private String wp;

            public Integer getRl() {
                return rl;
            }

            public void setRl(Integer rl) {
                this.rl = rl;
            }

            public Integer getSc() {
                return sc;
            }

            public void setSc(Integer sc) {
                this.sc = sc;
            }

            public String getW() {
                return w;
            }

            public void setW(String w) {
                this.w = w;
            }

            public Integer getWb() {
                return wb;
            }

            public void setWb(Integer wb) {
                this.wb = wb;
            }

            public Integer getWc() {
                return wc;
            }

            public void setWc(Integer wc) {
                this.wc = wc;
            }

            public Integer getWe() {
                return we;
            }

            public void setWe(Integer we) {
                this.we = we;
            }

            public String getWp() {
                return wp;
            }

            public void setWp(String wp) {
                this.wp = wp;
            }
        }
    }
}
