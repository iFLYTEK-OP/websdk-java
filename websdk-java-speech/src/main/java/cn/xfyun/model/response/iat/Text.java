package cn.xfyun.model.response.iat;

import com.google.gson.JsonObject;

import java.util.Arrays;

/**
 * @author <ydwang16@iflytek.com>
 * @description
 * @date 2021/3/24
 */
public class Text {

    int sn;
    int bg;
    int ed;
    String text;
    String pgs;
    int[] rg;
    boolean deleted;
    boolean ls;
    JsonObject vad;

    public int getSn() {
        return sn;
    }

    public void setSn(int sn) {
        this.sn = sn;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
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

    @Override
    public String toString() {
        return "Text{" +
                "bg=" + bg +
                ", ed=" + ed +
                ", ls=" + ls +
                ", sn=" + sn +
                ", text='" + text + '\'' +
                ", pgs=" + pgs +
                ", rg=" + Arrays.toString(rg) +
                ", deleted=" + deleted +
                ", vad=" + (vad == null ? "null" : vad.getAsJsonArray("ws").toString()) +
                '}';
    }

}
