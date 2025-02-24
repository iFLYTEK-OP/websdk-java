
import cn.xfyun.chat.HttpSparkChat;
import cn.xfyun.chat.WsSparkChat;
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
        String result = HttpSparkChat.prepare(SparkModelEum.LITE, "VDRBRxhysCQvIYxhMZeK:bUydDCeMlAnBSNtaRnoq")
                .append(UserMessage.crate("来一个妹子喜欢听的笑话"))
                .execute();
        System.out.println("result " + result);
    }

    //http流式调用
    @Test
    public void test2() {
        HttpSparkChat.prepare(SparkModelEum.V4_ULTRA, "VDRBRxhysCQvIYxhMZeK:bUydDCeMlAnBSNtaRnoq")
                .webSearch()
                .append(SystemMessage.crate("你是一个新闻工作者")).append(UserMessage.crate("今日3条热点娱乐新闻"))
                .execute(this::print);
    }

    //WEBSOCKET调用
    @Test
    public void test3() throws InterruptedException {
        WsSparkChat.prepare(SparkModelEum.GENERAL_V35, "6057995a", "28bb4b72196174bb290e8d60876a1013", "YjRkOTBlODAxM2U2NzIyZmMzMDhmMTdk")
                .append(SystemMessage.crate("你现在扮演李白")).append(UserMessage.crate("你喝醉过吗"))
                .onMessage(this::print)
                .execute();
        // 保持主线程运行，防止程序退出
        Thread.sleep(Long.MAX_VALUE);
    }


    //对大模型的返回，开发者自定义处理逻辑
    void print(String message) {
        System.out.println("receive " + message);
    }
}
