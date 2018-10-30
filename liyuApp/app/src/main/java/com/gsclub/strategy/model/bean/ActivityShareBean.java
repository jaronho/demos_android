package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ActivityShareBean implements Serializable {

    /**
     * act_id : 1
     * share_type : 3
     * wx_title : 邀请好友赚钱
     * wx_desc :
     * wx_share_img : http://static.huijindou.com/public/uploads/images/20180926/888550d00cc0054dcabe0ca927659f10.jpg
     * wx_url : http://rn.huijindou.com/html/register.html?invite=MTM5MDcwNDA5OTA=
     * act_name : 邀请好友赚钱
     * url : http://www.baidu.com/
     */

    @SerializedName("act_id")
    private String actId;
    @SerializedName("share_type")
    private String shareType;
    @SerializedName("wx_title")
    private String wxTitle;
    @SerializedName("wx_desc")
    private String wxDesc;
    @SerializedName("wx_share_img")
    private String wxShareImg;
    @SerializedName("wx_url")
    private String wxUrl;
    @SerializedName("act_name")
    private String actName;
    private String url;

    public String getActId() {
        return actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getShareType() {
        return shareType;
    }

    public void setShareType(String shareType) {
        this.shareType = shareType;
    }

    public String getWxTitle() {
        return wxTitle;
    }

    public void setWxTitle(String wxTitle) {
        this.wxTitle = wxTitle;
    }

    public String getWxDesc() {
        return wxDesc;
    }

    public void setWxDesc(String wxDesc) {
        this.wxDesc = wxDesc;
    }

    public String getWxShareImg() {
        return wxShareImg;
    }

    public void setWxShareImg(String wxShareImg) {
        this.wxShareImg = wxShareImg;
    }

    public String getWxUrl() {
        return wxUrl;
    }

    public void setWxUrl(String wxUrl) {
        this.wxUrl = wxUrl;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
