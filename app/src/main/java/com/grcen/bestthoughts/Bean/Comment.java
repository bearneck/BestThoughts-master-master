package com.grcen.bestthoughts.Bean;

public class Comment extends ExampleBean{
    private String name;
    private String content;
    private String iconId;
    private String uid;
    private int zannum;
    private int downnum;

    public Comment(String name, String content, String iconId, String uid, int zannum, int down) {
        this.name = name;
        this.content = content;
        this.iconId = iconId;
        this.uid = uid;
        this.zannum = zannum;
        this.downnum = down;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getZannum() {
        return zannum;
    }

    public void setZannum(int zannum) {
        this.zannum = zannum;
    }

    public int getDownnum() {
        return downnum;
    }

    public void setDownnum(int downnum) {
        this.downnum = downnum;
    }

}
