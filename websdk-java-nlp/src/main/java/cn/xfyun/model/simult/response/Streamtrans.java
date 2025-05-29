package cn.xfyun.model.simult.response;

/**
 * 同声传译返回参数
 *
 * @author <zyding6@ifytek.com>
 */
public class Streamtrans {


    /**
     * src : 一个面向全球的中文学习爱好者的一个
     * dst :  A global Chinese learningenthusiasts for a
     * wb : 10
     * we : 2480
     * is_final : 0
     */

    private String src;
    private String dst;
    private Integer wb;
    private Integer we;
    private Integer is_final;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

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

    public Integer getIs_final() {
        return is_final;
    }

    public void setIs_final(Integer is_final) {
        this.is_final = is_final;
    }
}
