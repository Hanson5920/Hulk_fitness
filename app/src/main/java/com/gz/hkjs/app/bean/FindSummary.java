package com.gz.hkjs.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class FindSummary{


    /**
     * ret : 200
     * msg :
     * data : [{"id":"1","proportion":"0","title":"健身有什么用？看看肌肉男的女友你或许明白了","img_url":"http://img.1122.anzhuo.com/http://alipic.cnys.com/upload9/image/2017-03-23/5p4Pr3Ypd2.jpg","show_type":"2","ctime":"17秒前","content_logo_url":"http://img.1122.anzhuo.com/","img_arr":[]}]
     */

    private int ret;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * proportion : 0
         * title : 健身有什么用？看看肌肉男的女友你或许明白了
         * img_url : http://img.1122.anzhuo.com/http://alipic.cnys.com/upload9/image/2017-03-23/5p4Pr3Ypd2.jpg
         * show_type : 2
         * ctime : 17秒前
         * content_logo_url : http://img.1122.anzhuo.com/
         * img_arr : []
         */

        private String id;
        private String proportion;
        private String title;
        private String img_url;
        private String show_type;
        private String ctime;
        private String content_logo_url;
        private List<?> img_arr;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getProportion() {
            return proportion;
        }

        public void setProportion(String proportion) {
            this.proportion = proportion;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg_url() {
            return img_url;
        }

        public void setImg_url(String img_url) {
            this.img_url = img_url;
        }

        public String getShow_type() {
            return show_type;
        }

        public void setShow_type(String show_type) {
            this.show_type = show_type;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getContent_logo_url() {
            return content_logo_url;
        }

        public void setContent_logo_url(String content_logo_url) {
            this.content_logo_url = content_logo_url;
        }

        public List<?> getImg_arr() {
            return img_arr;
        }

        public void setImg_arr(List<?> img_arr) {
            this.img_arr = img_arr;
        }
    }
}
