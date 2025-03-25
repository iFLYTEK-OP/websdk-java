package cn.xfyun.model.response.lfasr;

import java.io.Serializable;
import java.util.List;

/**
 * 翻译结果实体类
 *
 * @author kaili23
 */
public class LfasrTransResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 段落序号
     */
    private String segId;

    /**
     * 翻译结果
     */
    private String dst;

    /**
     * 开始时间
     */
    private int bg;

    /**
     * 结束时间
     */
    private int ed;

    /**
     * 标签
     */
    private List<String> tags;

    /**
     * 角色
     */
    private List<String> roles;

    public String getSegId() {
        return segId;
    }

    public void setSegId(String segId) {
        this.segId = segId;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
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

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "TransResult{" +
                "segId='" + segId + '\'' +
                ", dst='" + dst + '\'' +
                ", bg=" + bg +
                ", ed=" + ed +
                ", tags=" + tags +
                ", roles=" + roles +
                '}';
    }

}