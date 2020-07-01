package org.jpcl.dbop.model;

public class Note {
    private int id;
    private String content;
    private String name;
    private String date;
    private int is_man;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIs_man() {
        return is_man;
    }

    public void setIs_man(int is_man) {
        this.is_man = is_man;
    }
}
