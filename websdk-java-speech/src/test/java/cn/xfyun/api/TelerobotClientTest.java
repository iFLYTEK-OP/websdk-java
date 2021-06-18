package cn.xfyun.api;

import cn.xfyun.config.PropertiesConfig;
import cn.xfyun.model.request.telerobot.Callout;
import cn.xfyun.util.StringUtils;
import org.junit.Test;

/**
 * TODO
 *
 * @author : jun
 * @date : 2021年06月15日
 */
public class TelerobotClientTest {
    private static final String telerobotAPPKey = PropertiesConfig.getTelerobotAPPKey();
    private static final String telerobotAPPSecret = PropertiesConfig.getTelerobotAPPSecret();


    @Test
    public void testToken() throws Exception {
        TelerobotClient telerobotClient = new TelerobotClient.Builder(telerobotAPPKey, telerobotAPPSecret).build();
        System.out.println(telerobotClient.token().toString());
    }

    @Test
    public void test() throws Exception {
        TelerobotClient telerobotClient = new TelerobotClient.Builder(telerobotAPPKey, telerobotAPPSecret).build();
        System.out.println(telerobotClient.query("0f0408000d0f030f060c0c0c0e080f00", 0).toString());
    }

    @Test
    public void callout() throws Exception {
        TelerobotClient telerobotClient = new TelerobotClient.Builder(telerobotAPPKey, telerobotAPPSecret).build();
        Callout callout = new Callout();
        callout.setLine_num("69101338");
        callout.setRobot_id("719");
        callout.setCall_column(new String[]{"客户手机号码","姓名"});
        callout.setCall_list(new String[][]{{"18133652338","张先生"}});
        callout.setVoice_code("60030");
        System.out.println(StringUtils.gson.toJson(callout));
        System.out.println(telerobotClient.callout("0f0408000d0f030f060c0c0c0e080f00", callout).toString());
    }
}
