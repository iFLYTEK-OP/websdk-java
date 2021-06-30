package cn.xfyun.model.response.telerobot;

import cn.xfyun.model.response.telerobot.vo.Line;
import cn.xfyun.model.response.telerobot.vo.Robot;
import cn.xfyun.model.response.telerobot.vo.Url;
import cn.xfyun.model.response.telerobot.vo.Voice;

import java.util.List;

/**
 * 查询配置
 *
 * @author : jun
 * @date : 2021年06月21日
 */
public class TelerobotQuery {
    private List<Line> lines;
    private List<Robot> robots;
    private List<Url> urls;
    private List<Voice> voices;

    public List<Line> getLines() {
        return lines;
    }

    public void setLines(List<Line> lines) {
        this.lines = lines;
    }

    public List<Robot> getRobots() {
        return robots;
    }

    public void setRobots(List<Robot> robots) {
        this.robots = robots;
    }

    public List<Url> getUrls() {
        return urls;
    }

    public void setUrls(List<Url> urls) {
        this.urls = urls;
    }

    public List<Voice> getVoices() {
        return voices;
    }

    public void setVoices(List<Voice> voices) {
        this.voices = voices;
    }
}
