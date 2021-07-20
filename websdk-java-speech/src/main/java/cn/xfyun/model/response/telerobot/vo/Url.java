package cn.xfyun.model.response.telerobot.vo;

/**
 * 接口
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class Url {

    /**
     * 接口URL	应用方提供给平台回调的接口服务url地址，用于结果数据推送和呼入话术上下文动态数据获取（可选）。
     */
    private String url;
    /**
     * 接口模块	详见结果数据推送 和 呼入话术上下文动态数据获取 中的接口模块名称。
     */
    private String url_module;
}
