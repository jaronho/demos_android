package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hlw on 2018/5/18.
 */

public class BannerBean implements Serializable {
    /**
     * id : 23
     * title : 93
     * imgurl : http://p3obykbag.bkt.clouddn.com/public/uploads/images/20180504/47dadc7b2ebbba1d079c4be220e01329.png
     * aim_url :
     * jump_type : 2
     * uid : 0
     * aid : 93
     */

    private int id;
    private String title;
    @SerializedName("imgurl")
    private String imgUrl;
    @SerializedName("aim_url")
    private String aimUrl;
    private int uid;
    private int aid;
    @SerializedName("event_key")
    private String eventKey;

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

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }
}
