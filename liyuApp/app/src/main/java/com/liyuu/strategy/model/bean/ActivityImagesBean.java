package com.liyuu.strategy.model.bean;

import java.io.Serializable;
import java.util.List;

public class ActivityImagesBean implements Serializable {

    /**
     * list : [{"image":"http://static.huijindou.com/public/uploads/images/pic1.png","title":"高杠杆","desc":"1:10"},{"image":"http://static.huijindou.com/public/uploads/images/pic2.png","title":"费率","desc":"低于0.19%"},{"image":"http://static.huijindou.com/public/uploads/images/pic3.png","title":"穿仓免赔","desc":"风险无忧"}]
     * model : http://static.huijindou.com/public/uploads/images/pic4.png
     */

    private String model;
    private List<ListBean> list;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean implements Serializable {
        /**
         * image : http://static.huijindou.com/public/uploads/images/pic1.png
         * title : 高杠杆
         * desc : 1:10
         */

        private String image;
        private String title;
        private String desc;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
