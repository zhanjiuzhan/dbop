package org.jpcl.dbop.model;

import java.io.Serializable;

/**
 * 公告信息
 * @author Administrator
 */
public class Notice implements Serializable {

    private static final long serialVersionUID = -4122368288501595755L;

    /**
     * 公告类型 1 维护公告   2 更新公告   3 提示信息
     */
    private int type;

    /**
     * 公告的标题
     */
    private String title;

    /**
     * 公告的内容
     */
    private String content;

    /**
     * 公告存在的时间 分钟
     */
    private int time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
