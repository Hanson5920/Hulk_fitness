package com.gz.hkjs.app.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class RecipesDetail {

    /**
     * ret : 200
     * msg :
     * data : {"id":"88","name":"凉拌苦瓜","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/nSKW7FnaWr.png","dsc":"夏季天气燥热，让人容易口干舌燥，火气大。所以在夏季，吃一些清火清热，生津补气的食物，可以缓解身体的燥热，让身体变得舒适、健康","energy":"49","use_time":"20","exaggerate":"0","step":[{"name":"1","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/fA7mazfMyB.png","dsc":"苦瓜一根，清洗干净外皮","px_num":"1"},{"name":"2","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/kzhMCaXYyG.png","dsc":"将苦瓜对半切开，用小勺子挖干净里面的白色瓜瓤","px_num":"2"},{"name":"3","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/tMQy3MpKE7.png","dsc":"将苦瓜切成非常薄的片，越薄越好","px_num":"3"},{"name":"4","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/RCJFZDBpFQ.png","dsc":"锅里水烧开后放入苦瓜焯40秒。苦瓜焯的时间久了也会很苦。因为切的很薄，焯水时间大概40秒也足够了","px_num":"4"},{"name":"5","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/yKWTmRdnkj.png","dsc":"焯过水的苦瓜马上捞出放冷水里冰一下","px_num":"5"},{"name":"6","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/JsG2HbA335.png","dsc":"在苦瓜里加入少许加入少许盐搅拌均匀放置一会能把苦瓜里的水分逼出来一部分，然后用手将苦瓜在捏一下，捏去苦瓜里的部分苦水分","px_num":"6"},{"name":"7","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/BimKadB3yH.png","dsc":"温水泡软几个枸杞，其实不放枸杞也可以，只是放了更好看些","px_num":"7"},{"name":"8","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/rsiPajSHfa.png","dsc":"将苦瓜放在大碗里，然后加入少许盐，白糖、芝麻油、熟芝麻拌均匀即可","px_num":"8"}],"ingredients":[{"name":"苦瓜","num":"1根"},{"name":"盐","num":"5克"},{"name":"糖","num":"3克"},{"name":"芝麻油","num":"10克"},{"name":"熟芝麻","num":"10克"},{"name":"枸杞","num":"几粒"}],"is_collection":2}
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
         * id : 88
         * name : 凉拌苦瓜
         * logo_url : http://alipic.cnys.com/upload9/image/2017-04-11/nSKW7FnaWr.png
         * dsc : 夏季天气燥热，让人容易口干舌燥，火气大。所以在夏季，吃一些清火清热，生津补气的食物，可以缓解身体的燥热，让身体变得舒适、健康
         * energy : 49
         * use_time : 20
         * exaggerate : 0
         * step : [{"name":"1","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/fA7mazfMyB.png","dsc":"苦瓜一根，清洗干净外皮","px_num":"1"},{"name":"2","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/kzhMCaXYyG.png","dsc":"将苦瓜对半切开，用小勺子挖干净里面的白色瓜瓤","px_num":"2"},{"name":"3","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/tMQy3MpKE7.png","dsc":"将苦瓜切成非常薄的片，越薄越好","px_num":"3"},{"name":"4","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/RCJFZDBpFQ.png","dsc":"锅里水烧开后放入苦瓜焯40秒。苦瓜焯的时间久了也会很苦。因为切的很薄，焯水时间大概40秒也足够了","px_num":"4"},{"name":"5","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/yKWTmRdnkj.png","dsc":"焯过水的苦瓜马上捞出放冷水里冰一下","px_num":"5"},{"name":"6","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/JsG2HbA335.png","dsc":"在苦瓜里加入少许加入少许盐搅拌均匀放置一会能把苦瓜里的水分逼出来一部分，然后用手将苦瓜在捏一下，捏去苦瓜里的部分苦水分","px_num":"6"},{"name":"7","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/BimKadB3yH.png","dsc":"温水泡软几个枸杞，其实不放枸杞也可以，只是放了更好看些","px_num":"7"},{"name":"8","logo_url":"http://alipic.cnys.com/upload9/image/2017-04-11/rsiPajSHfa.png","dsc":"将苦瓜放在大碗里，然后加入少许盐，白糖、芝麻油、熟芝麻拌均匀即可","px_num":"8"}]
         * ingredients : [{"name":"苦瓜","num":"1根"},{"name":"盐","num":"5克"},{"name":"糖","num":"3克"},{"name":"芝麻油","num":"10克"},{"name":"熟芝麻","num":"10克"},{"name":"枸杞","num":"几粒"}]
         * is_collection : 2
         */

        private String id;
        private String name;
        private String logo_url;
        private String dsc;
        private String energy;
        private String use_time;
        private String exaggerate;
        private int is_collection;
        private List<StepBean> step;
        private List<IngredientsBean> ingredients;

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

        public String getDsc() {
            return dsc;
        }

        public void setDsc(String dsc) {
            this.dsc = dsc;
        }

        public String getEnergy() {
            return energy;
        }

        public void setEnergy(String energy) {
            this.energy = energy;
        }

        public String getUse_time() {
            return use_time;
        }

        public void setUse_time(String use_time) {
            this.use_time = use_time;
        }

        public String getExaggerate() {
            return exaggerate;
        }

        public void setExaggerate(String exaggerate) {
            this.exaggerate = exaggerate;
        }

        public int getIs_collection() {
            return is_collection;
        }

        public void setIs_collection(int is_collection) {
            this.is_collection = is_collection;
        }

        public List<StepBean> getStep() {
            return step;
        }

        public void setStep(List<StepBean> step) {
            this.step = step;
        }

        public List<IngredientsBean> getIngredients() {
            return ingredients;
        }

        public void setIngredients(List<IngredientsBean> ingredients) {
            this.ingredients = ingredients;
        }

        public static class StepBean {
            /**
             * name : 1
             * logo_url : http://alipic.cnys.com/upload9/image/2017-04-11/fA7mazfMyB.png
             * dsc : 苦瓜一根，清洗干净外皮
             * px_num : 1
             */

            private String name;
            private String logo_url;
            private String dsc;
            private String px_num;

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

            public String getDsc() {
                return dsc;
            }

            public void setDsc(String dsc) {
                this.dsc = dsc;
            }

            public String getPx_num() {
                return px_num;
            }

            public void setPx_num(String px_num) {
                this.px_num = px_num;
            }
        }

        public static class IngredientsBean {
            /**
             * name : 苦瓜
             * num : 1根
             */

            private String name;
            private String num;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getNum() {
                return num;
            }

            public void setNum(String num) {
                this.num = num;
            }
        }
    }
}
