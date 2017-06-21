package com.gz.hkjs.app.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/3/16.
 */

public class RecipesSummary implements Serializable{

    /**
     * ret : 200
     * msg :
     * data : [{"id":"14","name":"低卡瘦身清新蔬菜沙拉 ","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/FmMjBn4NJG.jpg","ingr":[{"name":"黄瓜","num":"1根"},{"name":"水萝卜","num":"1根"},{"name":"樱桃西红柿","num":"20-30粒"},{"name":"苦菊","num":"适量"},{"name":"亚麻籽油","num":"3汤匙"},{"name":"白醋","num":"3汤匙"},{"name":"披萨草","num":"1小搓"},{"name":"糖","num":"1克"},{"name":"盐","num":"1克"}],"dsc":"蔬菜沙拉我还是比较喜欢清爽型的，意式油醋汁作为沙拉调料最合适不过，低脂、低热量，蛋黄沙拉酱和千岛沙拉酱热量还是偏高，夏天快到了，对于要减肥的女士来讲还是低脂低热量的沙拉更好些。","food_tags":[{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}],"energy":"113","use_time":"10","exaggerate":"1","px":"14"},{"id":"13","name":"柠香鸡肉沙拉","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/xxKdeQ2xki.jpg","ingr":[{"name":"鸡腿1个","num":"约200克"},{"name":"秋葵","num":"2根"},{"name":"樱桃萝卜","num":"2颗"},{"name":"圣女果","num":"3颗"},{"name":"西蓝花","num":"50克"},{"name":"豌豆苗","num":"1小撮"},{"name":"草菇","num":"30克"},{"name":"柠檬汁","num":"15克"},{"name":"亚麻籽油","num":"30克"},{"name":"红杂菜","num":"少量"},{"name":"冰水","num":"两碗"},{"name":"鲜百里香","num":"少量"},{"name":"小葱","num":"少量"},{"name":"糖","num":"少量"},{"name":"姜片","num":"少量"},{"name":"黑胡椒粉","num":"少量"}],"dsc":"这盘悦颜口感酸甜又有肉鲜味的沙拉，不油不腻一人噼里啪啦吃个精光，好撑的感觉。摄入的纤维多，容易产生饱腹感，且鸡肉脂肪含量较低，脂肪中不饱和脂肪酸含量较高，用蒸再拌的方法清淡又可口。","food_tags":[{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}],"energy":"343","use_time":"20","exaggerate":"0","px":"13"},{"id":"12","name":"蓝莓柠檬水","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/PeYJKpeYWw.jpeg","ingr":[{"name":"蓝莓","num":"若干"},{"name":"柠檬","num":"若干"},{"name":"气泡水（雪碧、纯净水都可以）","num":"若干"},{"name":"薄荷叶（装饰）","num":"若干"},{"name":"冰块","num":"若干"}],"dsc":"柠檬的功效与作用：美白、减肥、降血脂、提高免疫力、降胆固醇、消脂、吸脂、燃烧脂肪、溶脂、消炎、祛斑。蓝莓的功效与作用：保护眼睛、增强视力，增强免疫力、减缓衰老，增强记忆力。","food_tags":[{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}],"energy":"68","use_time":"15","exaggerate":"0","px":"12"},{"id":"11","name":"健康低卡鸡肉卷","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/aBaAmsnsXG.jpeg","ingr":[{"name":"鸡胸肉","num":"40g"},{"name":"鸡蛋","num":"2只"},{"name":"玉米粉圆饼","num":"2张"},{"name":"生菜","num":"30g"}],"dsc":"健身的同时要注意营养均衡，否则健身的效果也会大打折扣","food_tags":[{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}],"energy":"361","use_time":"15","exaggerate":"0","px":"11"},{"id":"10","name":"鲜虾牛油果沙拉","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/jZZap2hdJ5.jpg","ingr":[{"name":"牛油果","num":"2个"},{"name":"小番茄","num":"8个"},{"name":"虾","num":"7只"},{"name":"鹌鹑蛋","num":"8只"},{"name":"生菜","num":"适量"},{"name":"胡椒粉","num":"适量"}],"dsc":"牛油果是一种营养价值很高的水果。含有多种维生素和矿物质有降低胆固醇和血脂，保护心血管和肝脏系统等保健作用 它的提炼油还可以用于制作高档护肤品。很软很顺滑的口感。随便搅和两下就能做成面包抹酱。本身味道不重，所以用来做沙拉也是很百搭的一种食材。 ","food_tags":[{"id":"1","name":"增肌"},{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}],"energy":"263","use_time":"20","exaggerate":"0","px":"10"},{"id":"9","name":"魔芋丝沙拉","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/bwKFxEQ4KC.jpeg","ingr":[{"name":"沙拉材料","num":"适量"},{"name":"魔芋丝","num":"一包"},{"name":"樱桃萝卜","num":"3个"},{"name":"萝卜面","num":"一小把"},{"name":"腌辣椒沙拉酱","num":"适量"},{"name":"腌辣椒","num":"2个"},{"name":"洋葱末","num":"10g"},{"name":"泡菜汁","num":"30ml"},{"name":"白糖","num":"5g"},{"name":"生抽","num":"15ml"},{"name":"芝麻油","num":"5ml"}],"dsc":"这道魔芋丝沙拉，其实就像是凉粉，吃起来又爽口又饱腹又不会胖哦！是夏天晚餐我的首选！泡菜汁没有可以用陈醋代替但要减量。","food_tags":[{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}],"energy":"68","use_time":"15","exaggerate":"0","px":"9"},{"id":"8","name":"鸡胸肉鸡蛋蔬菜沙拉","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/2y4fQZawz8.jpeg","ingr":[{"name":"圆生菜","num":"若干"},{"name":"苦菊","num":"若干"},{"name":"紫生菜","num":"若干"},{"name":"樱桃萝卜","num":"若干"},{"name":"西红柿","num":"若干"},{"name":"玉米粒","num":"若干"},{"name":"鸡肉","num":"若干"},{"name":"鸡蛋","num":"若干"},{"name":"全麦面包","num":"若干"},{"name":"盐","num":"适量"}],"dsc":"这是一份健康营养的早餐","food_tags":[{"id":"1","name":"增肌"},{"id":"2","name":"减脂"}],"energy":"291","use_time":"15","exaggerate":"0","px":"8"},{"id":"7","name":"金牌牛油果沙拉","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/DfZ37rfJn7.jpeg","ingr":[{"name":"牛油果","num":"1个"},{"name":"牛油生菜","num":"半棵"},{"name":"豆苗","num":"1把"},{"name":"菠萝","num":"1/4个"},{"name":"樱桃番茄","num":"150g"},{"name":"柚子","num":"2片"},{"name":"紫洋葱","num":"1/4个"},{"name":"大蒜","num":"2瓣"},{"name":"巴萨米克醋","num":"1汤匙约15ml"},{"name":"柠檬汁","num":"1个柠檬"},{"name":"初榨橄榄油","num":"10ml"},{"name":"盐","num":"2茶匙"},{"name":"现磨黑胡椒","num":"1茶匙"},{"name":"小黄瓜","num":"3根"}],"dsc":"清爽又浓郁，恰巧春季过后清扫肠胃的垃圾，低GI的食物让血糖维持平稳状态，消除疲劳，提高注意力，增加饱腹感 ","food_tags":[{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}],"energy":"185","use_time":"15","exaggerate":"0","px":"7"},{"id":"6","name":"鹰嘴豆沙拉","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/fyNdfEiHBF.jpeg","ingr":[{"name":"鹰嘴豆","num":"300克"},{"name":"菲达奶酪","num":"60克"},{"name":"油浸番茄干","num":"3-4块"},{"name":"欧芹","num":"1/4束"},{"name":"洋葱","num":"1/2个"},{"name":"坚果碎或松子仁","num":"20克"},{"name":"柠檬汁","num":"1/2个"},{"name":"橄榄油","num":"3汤匙"},{"name":"蒜头","num":"3瓣"},{"name":"姜末","num":"5克"},{"name":"盐","num":"少许"},{"name":"黑胡椒碎","num":"适量"}],"dsc":"鹰嘴豆不但可以补血、补钙，而且是糖尿病、高血压患者的最佳食品，菲达奶酪更是营养丰富，多吃不胖","food_tags":[{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}],"energy":"375","use_time":"20","exaggerate":"0","px":"6"},{"id":"5","name":"芦笋炒虾","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/52RPMHP7pc.jpg","ingr":[{"name":"芦笋","num":"250g"},{"name":"虾仁","num":"200g"},{"name":"蒜瓣","num":"4个"},{"name":"盐","num":"适量"},{"name":"胡椒","num":"适量"},{"name":"料酒","num":"一茶匙"},{"name":"橄榄油","num":"少许"}],"dsc":"芦笋是一种不可多得的富含营养又低热量的蔬菜，保护血管又降脂。低糖、低脂肪、高纤维素，而且含有丰富的微量元素。虽然蛋白质含量不高，但氨基酸组成比例适当","food_tags":[{"id":"2","name":"减脂"}],"energy":"204","use_time":"20","exaggerate":"0","px":"5"},{"id":"4","name":"枸杞炒菠菜","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-27/HrGfiXCAaB.jpg","ingr":[{"name":"菠菜","num":"500克"},{"name":"枸杞","num":"10克"},{"name":"橄榄油","num":"3ml"},{"name":"盐","num":"2克"}],"dsc":"菠菜能清理人体肠胃里的热毒，防治便秘，使人容光焕发。菠菜也中含有一种类胰岛素样物质，能使血糖保存稳定。菠菜丰富的维生素含量能防治口角炎、夜盲等维生素缺乏症。菠菜中还含大量的抗氧化剂，具有抗衰老、促进细胞繁殖作用，既能激活大脑功能，又可增强青春活力。","food_tags":[{"id":"1","name":"增肌"}],"energy":"141","use_time":"10","exaggerate":"1","px":"4"},{"id":"2","name":"南瓜土豆烧魔芋","logo_url":"http://alipic.cnys.com/upload9/image/2017-03-24/GsyyQTQnSa.jpeg","ingr":[{"name":"土豆","num":"1个"},{"name":"南瓜","num":"100g"},{"name":"魔芋","num":"100g"},{"name":"盐","num":"适量"},{"name":"泰式甜辣酱","num":"少许"}],"dsc":"家里吃火锅正好剩下这三样了～为了不浪费做成了一盘乱炖，没想到味道还挺好吃～","food_tags":[{"id":"1","name":"增肌"}],"energy":"136","use_time":"30","exaggerate":"1","px":"2"}]
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

    public static class DataBean  implements Serializable{
        /**
         * id : 14
         * name : 低卡瘦身清新蔬菜沙拉
         * logo_url : http://alipic.cnys.com/upload9/image/2017-03-27/FmMjBn4NJG.jpg
         * ingr : [{"name":"黄瓜","num":"1根"},{"name":"水萝卜","num":"1根"},{"name":"樱桃西红柿","num":"20-30粒"},{"name":"苦菊","num":"适量"},{"name":"亚麻籽油","num":"3汤匙"},{"name":"白醋","num":"3汤匙"},{"name":"披萨草","num":"1小搓"},{"name":"糖","num":"1克"},{"name":"盐","num":"1克"}]
         * dsc : 蔬菜沙拉我还是比较喜欢清爽型的，意式油醋汁作为沙拉调料最合适不过，低脂、低热量，蛋黄沙拉酱和千岛沙拉酱热量还是偏高，夏天快到了，对于要减肥的女士来讲还是低脂低热量的沙拉更好些。
         * food_tags : [{"id":"2","name":"减脂"},{"id":"3","name":"塑形"}]
         * energy : 113
         * use_time : 10
         * exaggerate : 1
         * px : 14
         */

        private String id;
        private String name;
        private String logo_url;
        private String dsc;
        private String energy;
        private String use_time;
        private String exaggerate;
        private String px;
        private List<IngrBean> ingr;
        private List<FoodTagsBean> food_tags;

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

        public String getPx() {
            return px;
        }

        public void setPx(String px) {
                this.px = px;
        }

        public List<IngrBean> getIngr() {
            return ingr;
        }

        public void setIngr(List<IngrBean> ingr) {
            this.ingr = ingr;
        }

        public List<FoodTagsBean> getFood_tags() {
            return food_tags;
        }

        public void setFood_tags(List<FoodTagsBean> food_tags) {
            this.food_tags = food_tags;
        }

        public static class IngrBean  implements Serializable{
            /**
             * name : 黄瓜
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

        public static class FoodTagsBean  implements Serializable{
            /**
             * id : 2
             * name : 减脂
             */

            private String id;
            private String name;

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
        }
    }
}
