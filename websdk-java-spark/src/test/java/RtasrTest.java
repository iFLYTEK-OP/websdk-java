import cn.xfyun.domain.RealTimeAsr;
import cn.xfyun.util.EasyOperation;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Test;

import javax.sound.sampled.*;
import java.util.function.Consumer;

/**
 * @author: rblu2
 * @desc: 实时语音转写
 * @create: 2025-02-27 10:04
 **/
public class RtasrTest {

    private TargetDataLine microphone;

    //
    @Test
    public void test1() throws InterruptedException {
        RealTimeAsr realTimeAsr = RealTimeAsr.prepare("xx", "xx")
                .onResult(x -> print(getContent(x)));

        realTimeAsr.send("D:\\draft\\2502\\test_1.pcm");
        // 保持主线程运行，防止程序退出
        Thread.sleep(Long.MAX_VALUE);
    }

    //调用send(byte[] buffer)
    @Test
    public void test2() throws InterruptedException {
        RealTimeAsr realTimeAsr = RealTimeAsr.prepare("xx", "xx")
                .onResult(x -> print(getContent(x)));

        //调起麦克风
        initMicrophone();
        
        // 持续捕获1min内音频并发送 设置缓冲区大小（例如 1280 字节）
        captureAudio(realTimeAsr::send);

        //发送结束
        realTimeAsr.sendOver();

        // 保持主线程运行，防止程序退出
        Thread.sleep(Long.MAX_VALUE);
    }

    void print(String message) {
        System.out.println("receive " + message);
    }
    public String getContent(String json) {
        StringBuilder builder = new StringBuilder();
        try {
            JsonNode jsonNode = EasyOperation.objectMapper().readTree(json);
            JsonNode rtArray = jsonNode.at("/cn/st/rt");
            if (rtArray.isArray()) {
                for (JsonNode rtItem : rtArray) {
                    JsonNode wsArray = rtItem.at("/ws");
                    if (wsArray.isArray()) {
                        for (JsonNode wsItem : wsArray) {
                            JsonNode cwArray = wsItem.at("/cw");
                            if (cwArray.isArray()) {
                                for (JsonNode cwItem : cwArray) {
                                    // 提取 "w" 的值
                                    if (cwItem.has("w")) {
                                        String wValue = cwItem.get("w").asText();
                                        builder.append(wValue);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return builder.toString();
    }

    private void initMicrophone() {
        try {
            // 设置音频格式  采样率（16kHz） 采样大小（16位） 单声道 有符号数据 小端序
            AudioFormat format = new AudioFormat(16000, 16, 1, true, false);

            // 获取麦克风
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
            microphone = (TargetDataLine) AudioSystem.getLine(info);
            microphone.open(format);
            microphone.start();
        } catch (LineUnavailableException e) {
            System.err.println("Failed to initialize microphone: " + e.getMessage());
            System.exit(1);
        }
    }

    private void captureAudio(Consumer<byte[]> consumer) {
        byte[] buffer = new byte[1280];
        long current = System.currentTimeMillis();
        while ((System.currentTimeMillis() - current) < (long) 60000) {
            if (microphone.read(buffer, 0, buffer.length) > 0) {
                consumer.accept(buffer);
            }
        }
    }
}
