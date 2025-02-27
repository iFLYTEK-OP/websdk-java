
import cn.xfyun.domain.HttpSparkChat;
import cn.xfyun.domain.WsSparkChat;
import cn.xfyun.eum.SparkModelEum;
import cn.xfyun.model.SystemMessage;
import cn.xfyun.model.UserMessage;
import org.junit.Test;

/**
 * @author: rblu2
 * @desc: 选择模型 + 对应得apiPassword
 * @create: 2025-02-18 19:12
 **/
public class SparkTest {

    //http非流式调用，适用于简单的回答
    @Test
    public void test1() {
        String result = HttpSparkChat.prepare(SparkModelEum.LITE, "xxx")
                .append(UserMessage.create("来一个妹子喜欢听的笑话"))
                .execute();
        System.out.println("result " + result);
    }

    //http流式调用
    @Test
    public void test2() {
        HttpSparkChat.prepare(SparkModelEum.V4_ULTRA, "xxx")
                .webSearch()
                .append(SystemMessage.create("你是一个新闻工作者")).append(UserMessage.create("今日3条热点科技新闻"))
                .execute(this::print);
    }

    //WEBSOCKET调用
    @Test
    public void test3() throws InterruptedException {
        WsSparkChat.prepare(SparkModelEum.GENERAL_V35, "xx", "xx", "xx")
                .append(SystemMessage.create("你现在扮演李白")).append(UserMessage.create("你喝醉过吗"))
                .onMessage(this::print)
                .onMessageEnding(() -> print("receive data finished"))
                .execute();
        // 保持主线程运行，防止程序退出
        Thread.sleep(Long.MAX_VALUE);
    }


    //对大模型的返回，开发者自定义处理逻辑
    void print(String message) {
        System.out.println("receive " + message);
    }
}
