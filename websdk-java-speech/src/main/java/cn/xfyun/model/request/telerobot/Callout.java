package cn.xfyun.model.request.telerobot;

/**
 * 直接外呼参数
 *
 * @author : jun
 * @date : 2021年06月16日
 */
public class Callout {

    /**
     * 话术编号
     */
    private String robot_id;
    /**
     * 线路号码
     */
    private String line_num;
    /**
     * 外呼数据列
     */
    private String[] call_column;
    /**
     * 外呼数据行	单次上限50条
     */
    private String[][] call_list;
    /**
     * 发音人编码(非必传)
     */
    private String voice_code;


    public String getRobot_id() {
        return robot_id;
    }

    public void setRobot_id(String robot_id) {
        this.robot_id = robot_id;
    }

    public String getLine_num() {
        return line_num;
    }

    public void setLine_num(String line_num) {
        this.line_num = line_num;
    }

    public String[] getCall_column() {
        return call_column;
    }

    public void setCall_column(String[] call_column) {
        this.call_column = call_column;
    }

    public String getVoice_code() {
        return voice_code;
    }

    public void setVoice_code(String voice_code) {
        this.voice_code = voice_code;
    }

    public String[][] getCall_list() {
        return call_list;
    }

    public void setCall_list(String[][] call_list) {
        this.call_list = call_list;
    }
}
