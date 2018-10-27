package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeMenuBean implements Serializable {
    private List<MenuBean> menu;

    @SerializedName("index_introduction")
    private IntroImageBean indexIntroduction;

    public List<MenuBean> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuBean> menu) {
        this.menu = menu;
    }

    public IntroImageBean getIndexIntroduction() {
        return indexIntroduction;
    }

    public void setIndexIntroduction(IntroImageBean indexIntroduction) {
        this.indexIntroduction = indexIntroduction;
    }

    public static class IntroImageBean implements Serializable {
        private String imgurl;

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }
    }

    public static class MenuBean implements Serializable {
        private int id;
        private String title;
        @SerializedName("imgurl")
        private String imgUrl;
        @SerializedName("aim_url")
        private String aimUrl;
        private int type;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAimUrl() {
            return aimUrl;
        }

        public void setAimUrl(String aimUrl) {
            this.aimUrl = aimUrl;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }
}
