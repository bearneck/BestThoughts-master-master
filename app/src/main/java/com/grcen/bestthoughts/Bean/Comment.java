package com.grcen.bestthoughts.Bean;

public class Comment {
    private String name;
    private String content;
    private String iconId;
    private String uid;
    private int zannum;
    private int downnum;
    private String imageurl;

    public Comment(String name, String content, String iconId, String uid, int zannum, int down, String imageurl) {
        this.name = name;
        this.content = content;
        this.iconId = iconId;
        this.uid = uid;
        this.zannum = zannum;
        this.downnum = down;
        this.imageurl = imageurl;
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

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
