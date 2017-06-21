package com.gz.hkjs.app.bean;

/**
 * Created by Administrator on 2017/4/18.
 */

public class TrainData {

    //拜访日期
    private String date;

    private int title;

    //拜访数量
    private int data;

    public TrainData(String date, int data,int title) {
        this.date = date;
        this.data = data;
        this.title = title;
    }

    public String getMonth() {
       String[] d= date.split("/");
        return d[0];
    }
    public String getday() {
        String[] d= date.split("/");
        return d[d.length-1];
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getData() {
        return data;
    }

    public float getMaxData(int maxData) {
        return (float) (data *(180.00/maxData));
    }

    public void setData(int data) {
        this.data = data;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}
