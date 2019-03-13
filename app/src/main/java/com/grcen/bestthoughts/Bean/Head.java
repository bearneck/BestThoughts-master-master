package com.grcen.bestthoughts.Bean;

public class Head {
    private String content;
    private String imageId;
    private int zannum;
    private int zannonum;
    private int sharenum;


    public Head(String content, String imageId, int zannum, int zannonum, int sharenum) {
        this.content = content;
        this.imageId = imageId;
        this.zannum = zannum;
        this.zannonum = zannonum;
        this.sharenum = sharenum;
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

    public int getZannonum() {
        return zannonum;
    }

    public void setZannonum(int zannonum) {
        this.zannonum = zannonum;
    }

    public int getSharenum() {
        return sharenum;
    }

    public void setSharenum(int sharenum) {
        this.sharenum = sharenum;
    }

}
