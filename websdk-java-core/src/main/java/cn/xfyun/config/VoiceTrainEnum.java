package cn.xfyun.config;

/**
 * 一句话训练枚举类
 *
 * @author zyding6
 **/
public enum VoiceTrainEnum {

    TOKEN("http://avatar-hci.xfyousheng.com/aiauth/v1/token", "一句话复刻获取token"),
    TRAIN_TEXT("http://opentrain.xfyousheng.com/voice_train/task/traintext", "获取训练文本"),
    TASK_ADD("http://opentrain.xfyousheng.com/voice_train/task/add", "创建音色训练任务"),
    AUDIO_ADD("http://opentrain.xfyousheng.com/voice_train/audio/v1/add", "向训练任务添加音频（url链接）"),
    TASK_SUBMIT("http://opentrain.xfyousheng.com/voice_train/task/submit", "音色训练任务提交训练(异步)"),
    AUDIO_SUBMIT("http://opentrain.xfyousheng.com/voice_train/task/submitWithAudio", "向训练任务添加音频（本地文件）并提交训练任务"),
    TASK_RESULT("http://opentrain.xfyousheng.com/voice_train/task/result", "根据任务id查询音色训练任务的状态");

    private final String url;
    private final String desc;

    VoiceTrainEnum(String url, String desc) {
        this.url = url;
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }

    public String getDesc() {
        return desc;
    }
}
