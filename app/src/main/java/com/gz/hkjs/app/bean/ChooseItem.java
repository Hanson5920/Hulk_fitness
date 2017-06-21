package com.gz.hkjs.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/10.
 */

public class ChooseItem {


    /**
     * ret : 200
     * msg :
     * data : [{"title":"健身难度","parameter":"exaggerate","data":[{"id":"19","title":"基础"},{"id":"20","title":"进阶"},{"id":"21","title":"强化"},{"id":"22","title":"突破"},{"id":"23","title":"极限"}]},{"title":"健身部位","parameter":"step","data":[{"id":"18","title":"臀部"},{"id":"17","title":"腿部"},{"id":"12","title":"全身"},{"id":"13","title":"胸部"},{"id":"14","title":"背部"},{"id":"15","title":"肩膀"},{"id":"16","title":"手臂"}]},{"title":"健身器材","parameter":"apparatus","data":[{"id":"8","title":"哑铃"},{"id":"9","title":"无机械(徒手)"},{"id":"10","title":"弹力带"},{"id":"11","title":"瑜伽球"}]}]
     */

    private String ret;
    private String msg;
    private List<DataBeanX> data;

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

    public List<DataBeanX> getData() {
        return data;
    }

    public void setData(List<DataBeanX> data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * title : 健身难度
         * parameter : exaggerate
         * data : [{"id":"19","title":"基础"},{"id":"20","title":"进阶"},{"id":"21","title":"强化"},{"id":"22","title":"突破"},{"id":"23","title":"极限"}]
         */

        private String title;
        private String parameter;
        private List<DataBean> data;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * id : 19
             * title : 基础
             */

            private String parameter;

            private String id;
            private String title;

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


            public String getParameter() {
                return parameter;
            }

            public void setParameter(String parameter) {
                this.parameter = parameter;
            }

        }
    }
}
