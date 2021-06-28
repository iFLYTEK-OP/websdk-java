package cn.xfyun.util;

/**
 * @author : iflytek
 * @date : 2021年03月15日
 */
public class SliceIdGenerator {
    /**
     * INIT_STR最后一位是`， 其ASCII大小是96， 是a的前一位
     */
    private static final String INIT_STR = "aaaaaaaaa`";

    private static final int LENGTH = INIT_STR.length();

    private final char[] ch = INIT_STR.toCharArray();

    public String getNextSliceId() {
        for (int j = LENGTH - 1; j >= 0; j--) {
            if (this.ch[j] != 'z') {
                this.ch[j] = (char) (this.ch[j] + 1);
                break;
            }
            this.ch[j] = 'a';
        }
        return new String(this.ch);
    }
}
