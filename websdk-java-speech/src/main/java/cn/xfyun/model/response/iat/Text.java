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
