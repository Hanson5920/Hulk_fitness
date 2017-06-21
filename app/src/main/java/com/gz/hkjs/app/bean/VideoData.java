package com.gz.hkjs.app.bean;

import java.util.List;

public class VideoData {


    /**
     * ret : 200
     * msg :
     * data : [{"id":"13","name":"摸膝卷腹","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-07/AGbmpHkRP8.jpg","exaggerate":"21","energy":"","wtime":"","px":"1"}]
     */

    private String ret;
    private String msg;
    private List<DataBean> data;

    public String getRet() {
        return ret;
    }

    public void setRet(String ret) {
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
         * id : 13
         * name : 摸膝卷腹
         * logo_url : http://alipic.cnys.com/upload9/image/2017-04-07/AGbmpHkRP8.jpg
         * exaggerate : 21
         * energy :
         * wtime :
         * px : 1
         */

        private String id;
        private String name;
        private String logo_url;
        private String exaggerate;
        private String energy;
        private String wtime;
        private String px;
        private String exaggerate_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo_url() {
            return logo_url;
        }

        public void setLogo_url(String logo_url) {
            this.logo_url = logo_url;
        }

        public String getExaggerate() {
            return exaggerate;
        }

        public void setExaggerate(String exaggerate) {
            this.exaggerate = exaggerate;
        }

        public String getEnergy() {
            return energy;
        }

        public void setEnergy(String energy) {
            this.energy = energy;
        }

        public String getWtime() {
            return wtime;
        }

        public void setWtime(String wtime) {
            this.wtime = wtime;
        }

        public String getPx() {
            return px;
        }

        public void setPx(String px) {
            this.px = px;
        }

        public String getExaggerate_name() {
            return exaggerate_name;
        }

        public void setExaggerate_name(String exaggerate_name) {
            this.exaggerate_name = exaggerate_name;
        }
    }
}
