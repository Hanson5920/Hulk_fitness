package com.gz.hkjs.app.bean;

import java.util.List;

/**
 * Company: chuangxinmengxiang-chongqingwapushidai
 * User: HongBin(Hongbin1011@gmail.com)
 * Date: 2017-04-21
 * Time: 10:52
 */

public class UserHomeData {

    /**
     * ret : 200
     * msg :
     * data : {"energy":"180","list_id":"13","day":"1","num":"10","times":"360","list":[{"id":"13","name":"腹部入门","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/BZGBN8YbhB.png","exaggerate":"19","energy":"50","wtime":"247.20"}]}
     */

    private String ret;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * energy : 180
         * list_id : 13
         * day : 1
         * num : 10
         * times : 360
         * list : [{"id":"13","name":"腹部入门","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/BZGBN8YbhB.png","exaggerate":"19","energy":"50","wtime":"247.20"}]
         */

        private String energy;
        private String list_id;
        private String day;
        private String num;
        private String times;
        private List<ListBean> list;

        public String getEnergy() {
            return energy;
        }

        public void setEnergy(String energy) {
            this.energy = energy;
        }

        public String getList_id() {
            return list_id;
        }

        public void setList_id(String list_id) {
            this.list_id = list_id;
        }

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 13
             * name : 腹部入门
             * logo_url : http://alipic.cnys.com/upload9/image/2017-04-11/BZGBN8YbhB.png
             * exaggerate : 19
             * exaggerate_name   : "基础"
             * energy : 50
             * wtime : 247.20
             */

            private String id;
            private String name;
            private String logo_url;
            private String exaggerate;
            private String energy;
            private String wtime;
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

            public String getExaggerate_name() {
                return exaggerate_name;
            }

            public void setExaggerate_name(String exaggerate_name) {
                this.exaggerate_name = exaggerate_name;
            }
        }
    }
}
