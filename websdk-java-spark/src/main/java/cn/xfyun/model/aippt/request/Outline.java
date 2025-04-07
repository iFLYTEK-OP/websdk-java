package cn.xfyun.model.aippt.request;

import java.util.List;

/**
 * 大纲内容请求实体类
 *
 * @author <zyding6@ifytek.com>
 **/
public class Outline {

    /**
     * PPT主标题
     */
    private String title;

    /**
     * PPT副标题
     */
    private String subTitle;

    /**
     * 章节
     */
    private List<Chapters> chapters;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public List<Chapters> getChapters() {
        return chapters;
    }

    public void setChapters(List<Chapters> chapters) {
        this.chapters = chapters;
    }

    public static class Chapters {

        /**
         * 子章节标题名称
         */
        private String chapterTitle;

        /**
         * 二级大纲
         */
        private List<ChapterContents> chapterContents;

        public String getChapterTitle() {
            return chapterTitle;
        }

        public void setChapterTitle(String chapterTitle) {
            this.chapterTitle = chapterTitle;
        }

        public List<ChapterContents> getChapterContents() {
            return chapterContents;
        }

        public void setChapterContents(List<ChapterContents> chapterContents) {
            this.chapterContents = chapterContents;
        }

        public static class ChapterContents {

            /**
             * 二级大纲标题
             */
            private String chapterTitle;

            /**
             * 二级大纲内容
             */
            private Object chapterContents;

            public String getChapterTitle() {
                return chapterTitle;
            }

            public void setChapterTitle(String chapterTitle) {
                this.chapterTitle = chapterTitle;
            }

            public Object getChapterContents() {
                return chapterContents;
            }

            public void setChapterContents(Object chapterContents) {
                this.chapterContents = chapterContents;
            }
        }
    }
}
