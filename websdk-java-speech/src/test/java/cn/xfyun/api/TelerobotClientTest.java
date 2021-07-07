package cn.xfyun.api;

import cn.xfyun.model.request.telerobot.Callout;
import cn.xfyun.model.request.telerobot.TaskCreate;
import cn.xfyun.model.request.telerobot.TaskInsert;
import cn.xfyun.model.request.telerobot.TaskQuery;
import cn.xfyun.model.response.telerobot.*;
import cn.xfyun.util.StringUtils;
import com.google.gson.Gson;
import config.PropertiesConfig;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

/**
 * 客服中间件测试类
 *
 * @author : jun
 * @date : 2021年06月15日
 */
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@RunWith(PowerMockRunner.class)
@PowerMockIgnore("cn.xfyun.util.HttpConnector")
public class TelerobotClientTest {
    private static final String telerobotAPPKey = PropertiesConfig.getTelerobotAPPKey();
    private static final String telerobotAPPSecret = PropertiesConfig.getTelerobotAPPSecret();
    private static String token = "";
    TelerobotClient telerobotClient = new TelerobotClient.Builder(telerobotAPPKey, telerobotAPPSecret).build();
    public static final Gson gson = new Gson();

    @Before
    public void config() throws Exception {
        TelerobotResponse<TelerobotToken> telerobotResponse = telerobotClient.token();
        token = telerobotResponse.getResult().getToken();
    }



    @Test
    public void test_01_Token() throws Exception {
        TelerobotResponse<TelerobotToken> telerobotResponse = telerobotClient.token();
        String token1 = telerobotResponse.getResult().getToken();
        System.out.println("token:" + telerobotResponse.getResult().getToken());
        assertFalse(StringUtils.isNullOrEmpty(token1));
    }

    @Test
    public void test_02_Query() throws Exception {
        TelerobotResponse<TelerobotQuery> telerobotResponse = telerobotClient.query(token, 0);
        System.out.println(StringUtils.gson.toJson(telerobotResponse));
        assertEquals(0, telerobotResponse.getCode());
    }

    @Test
    public void test_03_callout() throws Exception {
        Callout callout = new Callout();
        callout.setLine_num("69101460");
        callout.setRobot_id("20023224");
        callout.setCall_column(new String[]{"客户手机号码"});
        callout.setCall_list(new String[][]{{"18133652338"}});
        callout.setVoice_code("60020");
        TelerobotResponse<TelerobotCallout> telerobotResponse = telerobotClient.callout(token, callout);
        System.out.println(gson.toJson(telerobotResponse));
        assertTrue(telerobotResponse.getCode() >= 0);
    }

    /**
     * 创建外呼任务
     *
     * @throws Exception
     */
    @Test
    public void test_04_taskCreate() throws Exception {
        TaskCreate taskCreate = new TaskCreate();
        taskCreate.setTask_name("abelTest1");
        taskCreate.setLine_num("69101460");
        taskCreate.setRobot_id("20023224");
        taskCreate.setTime_range(new String[]{"09:00:00-12:00:00", "13:00:00-17:30:00"});
        taskCreate.setTime_begin(1625709600000L);
        TelerobotResponse<TelerobotCreate> telerobotResponse = telerobotClient.createTask(token, taskCreate);
        System.out.println(gson.toJson(telerobotResponse));
        assertTrue(telerobotResponse.getCode() >= 0);
    }

    /**
     * 提交任务数据
     *
     * @throws Exception
     */
    @Test
    public void test_05_insertTask() throws Exception {
        TaskInsert taskInsert = new TaskInsert();
        taskInsert.setTask_id("5853928459405440");
        taskInsert.setCall_column(new String[]{"客户手机号码"});
        taskInsert.setCall_list(new String[][]{{"18133652338"}});
        TelerobotResponse<TelerobotCallout> telerobotResponse = telerobotClient.insertTask(token, taskInsert);
        System.out.println(gson.toJson(telerobotResponse));
        assertTrue(telerobotResponse.getCode() >= 0);
    }

    /**
     * 启动外呼任务
     *
     * @throws Exception
     */
    @Test
    public void test_06_startTask() throws Exception {
        TelerobotResponse<String> telerobotResponse = telerobotClient.startTask(token, "5853928459405440");
        System.out.println(gson.toJson(telerobotResponse));
        assertTrue(telerobotResponse.getCode() >= 0);
    }

    /**
     * 暂停外呼任务
     *
     * @throws Exception
     */
    @Test
    public void test_07_pauseTask() throws Exception {
        TelerobotResponse<String> telerobotResponse = telerobotClient.pauseTask(token, "5853928459405440");
        System.out.println(gson.toJson(telerobotResponse));
        assertTrue(telerobotResponse.getCode() >= 0);
    }


    /**
     * 删除外呼任务
     *
     * @throws Exception
     */
    @Test
    public void test_08_deleteTask() throws Exception {
        TelerobotResponse<String> telerobotResponse = telerobotClient.deleteTask(token, "5853928459405440");
        System.out.println(gson.toJson(telerobotResponse));
        assertTrue(telerobotResponse.getCode() >= 0);
    }

    /**
     * 启动外呼任务
     *
     * @throws Exception
     */
    @Test
    public void test_09_queryTask() throws Exception {
        TaskQuery taskQuery = new TaskQuery();
        taskQuery.setTask_id("5853928459405440");
        taskQuery.setTask_name("abelTest");
        TelerobotResponse<TelerobotTaskQuery> telerobotResponse = telerobotClient.queryTask(token, taskQuery);
        System.out.println(gson.toJson(telerobotResponse));
        assertEquals(0, telerobotResponse.getCode());
    }
}
