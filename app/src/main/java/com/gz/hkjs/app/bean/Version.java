package com.gz.hkjs.app.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2017/3/23.
 */

public class Version {


    /**
     * ret : 200
     * msg : 获取最新版本信息接口
     * data : {"id":"9","ctime":"1477540289","app_version":"123123","app_size":"1","app_name":"111","app_url":"http://www.anzhuo.com/x","app_img_url":"/image/2016-10-27/581179b5a1066.JPG","package":"","apply":"0","local_md5":"54321","server_md5":"12345","apatch_url":"www.baidu.com","apatch_size":"100","desc":""}
     */

    private int ret;
    private String msg;
    private DataBean data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 9
         * ctime : 1477540289
         * app_version : 123123
         * app_size : 1
         * app_name : 111
         * app_url : http://www.anzhuo.com/x
         * app_img_url : /image/2016-10-27/581179b5a1066.JPG
         * package :
         * apply : 0
         * local_md5 : 54321
         * server_md5 : 12345
         * apatch_url : www.baidu.com
         * apatch_size : 100
         * desc :
         */

        private String id;
        private String ctime;
        private String app_version;
        private String app_size;
        private String app_name;
        private String app_url;
        private String app_img_url;
        @SerializedName("package")
        private String packageX;
        private String apply;
        private String local_md5;
        private String server_md5;
        private String apatch_url;
        private String apatch_size;
        private String desc;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getApp_version() {
            return app_version;
        }

        public void setApp_version(String app_version) {
            this.app_version = app_version;
        }

        public String getApp_size() {
            return app_size;
        }

        public void setApp_size(String app_size) {
            this.app_size = app_size;
        }

        public String getApp_name() {
            return app_name;
        }

        public void setApp_name(String app_name) {
            this.app_name = app_name;
        }

        public String getApp_url() {
            return app_url;
        }

        public void setApp_url(String app_url) {
            this.app_url = app_url;
        }

        public String getApp_img_url() {
            return app_img_url;
        }

        public void setApp_img_url(String app_img_url) {
            this.app_img_url = app_img_url;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getApply() {
            return apply;
        }

        public void setApply(String apply) {
            this.apply = apply;
        }

        public String getLocal_md5() {
            return local_md5;
        }

        public void setLocal_md5(String local_md5) {
            this.local_md5 = local_md5;
        }

        public String getServer_md5() {
            return server_md5;
        }

        public void setServer_md5(String server_md5) {
            this.server_md5 = server_md5;
        }

        public String getApatch_url() {
            return apatch_url;
        }

        public void setApatch_url(String apatch_url) {
            this.apatch_url = apatch_url;
        }

        public String getApatch_size() {
            return apatch_size;
        }

        public void setApatch_size(String apatch_size) {
            this.apatch_size = apatch_size;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
