package cn.xfyun.api;

import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.request.telerobot.Callout;
import cn.xfyun.model.request.telerobot.TaskCreate;
import cn.xfyun.model.request.telerobot.TaskInsert;
import cn.xfyun.model.request.telerobot.TaskQuery;
import cn.xfyun.model.response.telerobot.*;
import cn.xfyun.util.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * 客服中间件测试类
 *
 * @author : jun
 * @date : 2021年06月15日
 */
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("cn.xfyun.util.HttpConnector")
public class TelerobotClientTest {
    private static final String telerobotAPPKey = PropertiesConfig.getTelerobotAPPKey();
    private static final String telerobotAPPSecret = PropertiesConfig.getTelerobotAPPSecret();
    private static String token="0d0c020e040e030a08090d0d020d020a";
    TelerobotClient telerobotClient = new TelerobotClient.Builder(telerobotAPPKey, telerobotAPPSecret).build();

    @Test
    public void testToken() throws Exception {
        TelerobotResponse<TelerobotToken> telerobotResponse = telerobotClient.token();
        token = telerobotResponse.getResult().getToken();
        assertFalse(StringUtils.isNullOrEmpty(token));

    }

    @Test
    public void testQuery() throws Exception {
        TelerobotResponse<TelerobotQuery> telerobotResponse = telerobotClient.query(token, 0);
        System.out.println(StringUtils.gson.toJson(telerobotResponse));
        assertEquals(0, telerobotResponse.getCode());
    }

    @Test
    public void callout() throws Exception {
        Callout callout = new Callout();
        callout.setLine_num("69101460");
        callout.setRobot_id("8726");
        callout.setCall_column(new String[]{"客户手机号码"});
        callout.setCall_list(new String[][]{{"18133652338"}});
        callout.setVoice_code("60020");
        TelerobotResponse<TelerobotCallout> telerobotResponse = telerobotClient.callout(token, callout);
        assertEquals(0, telerobotResponse.getCode());
    }

    /**
     * 创建外呼任务
     * @throws Exception
     */
    @Test
    public void taskCreate() throws Exception {
        TaskCreate taskCreate = new TaskCreate();
        taskCreate.setTask_name("abelTest");
        taskCreate.setLine_num("69101460");
        taskCreate.setRobot_id("8726");
        taskCreate.setTime_begin(1624327200000L);
        TelerobotResponse<TelerobotCreate> telerobotResponse = telerobotClient.createTask(token, taskCreate);
        assertEquals(0, telerobotResponse.getCode());
    }

    /**
     * 提交任务数据
     * @throws Exception
     */
    @Test
    public void insertTask() throws Exception {
        TaskInsert taskInsert = new TaskInsert();

        TelerobotResponse telerobotResponse = telerobotClient.insertTask(token, taskInsert);
        assertEquals(0, telerobotResponse.getCode());
    }

    /**
     * 启动外呼任务
     * @throws Exception
     */
    @Test
    public void startTask() throws Exception {
        TelerobotResponse telerobotResponse = telerobotClient.startTask(token, "1234");
        assertEquals(0, telerobotResponse.getCode());
    }

    /**
     * 暂停外呼任务
     * @throws Exception
     */
    @Test
    public void pauseTask() throws Exception {
        TelerobotResponse telerobotResponse = telerobotClient.pauseTask(token, "1234");
        assertEquals(0, telerobotResponse.getCode());
    }


    /**
     * 删除外呼任务
     * @throws Exception
     */
    @Test
    public void deleteTask() throws Exception {
        TelerobotResponse telerobotResponse = telerobotClient.deleteTask(token, "1234");
        assertEquals(0, telerobotResponse.getCode());
    }

    /**
     * 启动外呼任务
     * @throws Exception
     */
    @Test
    public void queryTask() throws Exception {
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setTask_id("3847647735646336");
//        taskQuery.setTime_begin(153000000L);
//        taskQuery.setSort_name("CREATETIME");
        TelerobotResponse<TelerobotTaskQuery> telerobotResponse = telerobotClient.queryTask(token, taskQuery);
        assertEquals(0, telerobotResponse.getCode());
    }
}
