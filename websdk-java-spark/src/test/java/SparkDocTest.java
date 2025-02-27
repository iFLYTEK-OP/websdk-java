import cn.xfyun.domain.SparkDoc;
import cn.xfyun.domain.SparkDocChat;
import cn.xfyun.model.UserMessage;
import org.junit.Test;

/**
 * @author: rblu2
 * @desc: 星火问答
 * @create: 2025-02-18 19:12
 **/
public class SparkDocTest {

    //上传文档，投喂知识
    @Test
    public void test1() {
        String result = SparkDoc.prepare("xx", "xx")
                .uploadFile("D:\\draft\\250224\\背影.txt");
        print(result);
    }
    //上传文档，投喂知识
    @Test
    public void test2() {
        String result = SparkDoc.prepare("xx", "xx")
                .uploadUrl("https://openres.xfyun.cn/xfyundoc/2025-02-25/51087fc6-ecb2-4fa4-820b-6a3326f06cab/1740447798025/背影.txt", "背影.txt");
        print(result);
    }

    //上传文档，投喂知识,查询文档训练状态,fieldId值，在test1上传文档后的返回里结果里获取
    @Test
    public void test3() {
        String result = SparkDoc.prepare("xx", "xx")
                .addFileId("633e74f5f1d44d638f14a2c0a7f8beca")
                .status();
        print(result);

    }

    //对话问答，（训练状态fileStatus为vectored时）
    @Test
    public void test4() throws InterruptedException {
        SparkDocChat.prepare("xx", "xx")
                .addFileId("633e74f5f1d44d638f14a2c0a7f8beca")
                .append(UserMessage.create("本文的中心思想是什么"))
                .onMessage(this::print)
                .execute();
        // 保持主线程运行，防止程序退出
        Thread.sleep(Long.MAX_VALUE);
    }

    //提交萃取任务，即对训练完成的文档生成QA对
    @Test
    public void test5() {
        String result = SparkDoc.prepare("xx", "xx")
                .extract("253fc5ccaa554007be7eec22bfbf1b1b");
        print(result);

    }


    //对大模型的返回，开发者自定义处理逻辑
    void print(String message) {
        System.out.println("receive " + message);
    }
}
