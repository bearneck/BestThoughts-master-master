package com.grcen.bestthoughts.Bean;

import android.util.Log;

public class Picture {
    private String name;
    private String content;
    private String imageId;
    private String iconId;
    private String uid;
    private int data_id;
    private int zannum;
    private int contentnum;
    private int sharenum;
    private int down;
    private int soureid;

    public Picture(String name, String content, int zannum, int contentnum, int sharenum, String iconId, String imageId, int down, String uid, int soureid) {
        this.name = name;
        this.content = content;
        this.zannum = zannum;
        this.contentnum = contentnum;
        this.sharenum = sharenum;
        this.iconId = iconId;
        this.imageId = imageId;
        this.down = down;
        this.uid = uid;
        this.soureid = soureid;

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

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public int getZannum() {
        return zannum;
    }

    public void setZannum(int zannum) {
        this.zannum = zannum;
    }

    public int getContentnum() {
        return contentnum;
    }

    public void setContentnum(int contentnum) {
        this.contentnum = contentnum;
    }

    public int getSharenum() {
        return sharenum;
    }

    public void setSharenum(int sharenum) {
        this.sharenum = sharenum;
    }

    public String getIconId() {
        return iconId;
    }

    public void setIconId(String iconId) {
        this.iconId = iconId;
    }

    public int getData_id() {
        return data_id;
    }

    public void setData_id(int data_id) {
        this.data_id = data_id;
    }

    public int getDown() {
        return down;
    }

    public void setDown(int down) {
        this.down = down;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getSoureid() {
        return soureid;
    }

    public void setSoureid(int soureid) {
        this.soureid = soureid;
    }
}
