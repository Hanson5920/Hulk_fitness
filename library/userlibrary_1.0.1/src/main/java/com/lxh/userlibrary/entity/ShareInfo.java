package com.lxh.userlibrary.entity;

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * 分享实体类
 * cq
 */
public class ShareInfo implements Serializable {
    /*mData.setContext("测试文本(内容)");
    mData.setImgUrl("http://img3.imgtn.bdimg.com/it/u=1681882397,3535453166&fm=11&gp=0.jpg");
    mData.setTitle("测试文本(标题)");
    mData.setUrl("http://www.djcc.com");*/
    public String title; //标题
    public String text;//文本内容
    public String url;//内容url
    public String imgUrl;//图标url
    public String id;//歌曲id
    public Bitmap imgIcon;

    public Bitmap getImgIcon() {
        return imgIcon;
    }

    public void setImgIcon(Bitmap imgIcon) {
        this.imgIcon = imgIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String context) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
