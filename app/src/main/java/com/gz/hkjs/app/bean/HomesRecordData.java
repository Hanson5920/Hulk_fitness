package com.gz.hkjs.app.bean;


import com.chad.library.adapter.base.entity.SectionEntity;

import java.util.List;

/**
 * Created by administrator on 2017/5/2.
 */

public class HomesRecordData {
    /**
     * ret : 200
     * msg :
     * data : {"uid":"13","energy":"180","list_id":"13","day":"1","num":"10","times":"360","update_time":"1491840000","list":[{"title":"2017-04-11","data":[{"id":"7","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"8","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"9","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"10","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"}],"count_num":72,"count_times":144,"count_time":"04/11"},{"title":"2017-04-10","data":[{"id":"11","name":"859858","energy":"18","times":"36","ctime":"1491753600","uid":"13","list_id":"13"},{"id":"12","name":"859858","energy":"18","times":"36","ctime":"1491753600","uid":"13","list_id":"13"},{"id":"13","name":"859858","energy":"18","times":"36","ctime":"1491753600","uid":"13","list_id":"13"}],"count_num":54,"count_times":108,"count_time":"04/10"}]}
     */

    private String ret;
    private String msg;
    private DataBeanX data;

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

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * uid : 13
         * energy : 180
         * list_id : 13
         * day : 1
         * num : 10
         * times : 360
         * update_time : 1491840000
         * list : [{"title":"2017-04-11","data":[{"id":"7","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"8","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"9","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"10","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"}],"count_num":72,"count_times":144,"count_time":"04/11"},{"title":"2017-04-10","data":[{"id":"11","name":"859858","energy":"18","times":"36","ctime":"1491753600","uid":"13","list_id":"13"},{"id":"12","name":"859858","energy":"18","times":"36","ctime":"1491753600","uid":"13","list_id":"13"},{"id":"13","name":"859858","energy":"18","times":"36","ctime":"1491753600","uid":"13","list_id":"13"}],"count_num":54,"count_times":108,"count_time":"04/10"}]
         */

        private String uid;
        private String energy;
        private String list_id;
        private String day;
        private String num;
        private String times;
        private String update_time;
        private List<ListBean> list;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

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

        public String getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(String update_time) {
            this.update_time = update_time;
        }

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * title : 2017-04-11
             * data : [{"id":"7","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"8","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"9","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"},{"id":"10","name":"859858","energy":"18","times":"36","ctime":"1491840000","uid":"13","list_id":"13"}]
             * count_num : 72
             * count_times : 144
             * count_time : 04/11
             */

            private String title;
            private int count_num;
            private String count_times;
            private String count_time;
            private List<DataBean> data;


            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getCount_num() {
                return count_num;
            }

            public void setCount_num(int count_num) {
                this.count_num = count_num;
            }

            public String getCount_times() {
                return count_times;
            }

            public void setCount_times(String count_times) {
                this.count_times = count_times;
            }

            public String getCount_time() {
                return count_time;
            }

            public void setCount_time(String count_time) {
                this.count_time = count_time;
            }

            public List<DataBean> getData() {
                return data;
            }

            public void setData(List<DataBean> data) {
                this.data = data;
            }

            public static class DataBean extends SectionEntity {
                /**
                 * id : 7
                 * name : 859858
                 * energy : 18
                 * times : 36
                 * ctime : 1491840000
                 * uid : 13
                 * list_id : 13
                 */

                private String id;
                private String name;
                private String energy;
                private String times;
                private String ctime;
                private String uid;
                private String list_id;
                private HomesRecordData.DataBeanX.ListBean o;

                public DataBean(boolean isHeader,HomesRecordData.DataBeanX.ListBean o) {
                    super(isHeader, o.getTitle());
                    this.o = o;
                }


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

                public String getEnergy() {
                    return energy;
                }

                public void setEnergy(String energy) {
                    this.energy = energy;
                }

                public String getTimes() {
                    return times;
                }

                public void setTimes(String times) {
                    this.times = times;
                }

                public String getCtime() {
                    return ctime;
                }

                public void setCtime(String ctime) {
                    this.ctime = ctime;
                }

                public String getUid() {
                    return uid;
                }

                public void setUid(String uid) {
                    this.uid = uid;
                }

                public String getList_id() {
                    return list_id;
                }

                public void setList_id(String list_id) {
                    this.list_id = list_id;
                }

                public ListBean getO() {
                    return o;
                }

                public void setO(ListBean o) {
                    this.o = o;
                }
            }
        }
    }
}
