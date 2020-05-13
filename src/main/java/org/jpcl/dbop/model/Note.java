package org.jpcl.dbop.model;

import java.util.Date;

/**
 * @author Administrator
 */
public class Note {
    private long id;
    private String content;
    private String name;
    private Date date;
    private int isMan;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIsMan() {
        return isMan;
    }

    public void setIsMan(int isMan) {
        this.isMan = isMan;
    }
}
