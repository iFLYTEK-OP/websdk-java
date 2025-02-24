
import cn.xfyun.basic.RestResult;
import cn.xfyun.chat.SparkDoc;
import cn.xfyun.chat.SparkDocChat;
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
        RestResult data = SparkDoc.prepare("6057995a", "YjRkOTBlODAxM2U2NzIyZmMzMDhmMTdk")
                .file("D:\\draft\\250224\\背影.txt")
                .upload();
        print(data.getBody());
    }

    //上传文档，投喂知识,查询文档训练状态,fieldId值，在test1上传文档后的返回里结果里获取
    @Test
    public void test2() {
        RestResult data = SparkDoc.prepare("6057995a", "YjRkOTBlODAxM2U2NzIyZmMzMDhmMTdk")
                .addFileId("de62193c7ce34a83ad68c0a786d9a4b7")
                .status();
        print(data.getBody());

    }

    //对话问答，（训练状态fileStatus为vectored时）
    @Test
    public void test3() throws InterruptedException {
        SparkDocChat.prepare("6057995a", "YjRkOTBlODAxM2U2NzIyZmMzMDhmMTdk")
                .addFileId("253fc5ccaa554007be7eec22bfbf1b1b")
                .append(UserMessage.crate("本文的中心思想是什么"))
                .onMessage(this::print)
                .execute();
        // 保持主线程运行，防止程序退出
        Thread.sleep(Long.MAX_VALUE);
    }

    //提交萃取任务，即对训练完成的文档生成QA对
    @Test
    public void test4() {
        RestResult data = SparkDoc.prepare("6057995a", "YjRkOTBlODAxM2U2NzIyZmMzMDhmMTdk")
                .extract("253fc5ccaa554007be7eec22bfbf1b1b");
        print(data.getBody());

    }


    //对大模型的返回，开发者自定义处理逻辑
    void print(String message) {
        System.out.println("receive " + message);
    }
}
