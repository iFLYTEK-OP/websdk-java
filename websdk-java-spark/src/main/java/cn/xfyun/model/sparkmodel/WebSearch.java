package cn.xfyun.model.sparkmodel;

import com.google.gson.annotations.SerializedName;

/**
 * 联网搜索工具实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class WebSearch {

    /**
     * enable：是否开启搜索功能，设置为true,
     * 模型会根据用户输入判断是否触发联网搜索，false则完全不触发；
     * 非必传，默认开启（true）
     */
    private boolean enable;

    /**
     * show_ref_label 开关控制触发联网搜索时是否返回信源信息（仅在enable为true时生效）
     * 如果开启，则先返回搜索结果，之后再返回模型回复内容
     * 非必传，默认关闭（false）
     */
    @SerializedName("show_ref_label")
    private boolean showRefLabel;

    /**
     * search_mode 控制联网搜索策略（仅在enable为true时生效）
     * normal：标准搜索模式，模型引用搜索返回的摘要内容回答问题
     * deep：深度搜索模式，模型引用搜索返回的全文内容，回复的更准确；同时会带来额外的token消耗（返回search_prompt字段）
     * 非必传，默认标准搜索（normal）
     */
    @SerializedName("search_mode")
    private String searchMode;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isShowRefLabel() {
        return showRefLabel;
    }

    public void setShowRefLabel(boolean showRefLabel) {
        this.showRefLabel = showRefLabel;
    }

    public String getSearchMode() {
        return searchMode;
    }

    public void setSearchMode(String searchMode) {
        this.searchMode = searchMode;
    }
}
