package com.gsclub.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by hlw on 2018/5/23.
 */

public class UserIndexBean implements Serializable {

    /**
     * id : 1
     * tel : 13907040990
     * open_id : 134b85553195b3b19a24c6bd9c3e60ae
     * nickname : 什么鬼SOHO过头图木木木ou
     * headimg : http://p3obykbag.bkt.clouddn.com//public/uploads/headimg/2018-02-07/c55d37506d632ed8bf2b2cb0cd9cba3a.png
     * is_lock : 1
     * source : 24
     * platform : 2
     * user_tags : 0
     * c_time : 1517828333
     * u_time : 1518418680
     * uid : 1
     * is_lock_text : 正常
     * time_text : 2018-02-05 18:58
     */

    private String tel;
    @SerializedName("open_id")
    private String openId;
    private String nickname;
    private String headimg;
    @SerializedName("is_lock")
    private int isLock;
    private int source;
    private int platform;
    @SerializedName("user_tags")
    private int userTags;
    @SerializedName("c_time")
    private String cTime;
    @SerializedName("u_time")
    private String uTime;
    private String uid;
    @SerializedName("is_lock_text")
    private String isLockText;
    @SerializedName("time_text")
    private String timeText;
    private String desc;//注册成功后返回的成功弹窗提示语
    private String mark;//mark为1时跳转昵称界面
    @SerializedName("pay_pwd")
    private String payPwd;//交易密码
    @SerializedName("bank_status")
    private String bankStatus;//是否已绑卡1绑定2未绑定
    @SerializedName("is_activity")
    private String isActivity;//是否显示新手福利1显示
    @SerializedName("activity_image")
    private String activityImage;//新手福利banner
    @SerializedName("activity")
    private ActivityBean activityInfo;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public int getIsLock() {
        return isLock;
    }

    public void setIsLock(int isLock) {
        this.isLock = isLock;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getPlatform() {
        return platform;
    }

    public void setPlatform(int platform) {
        this.platform = platform;
    }

    public int getUserTags() {
        return userTags;
    }

    public void setUserTags(int userTags) {
        this.userTags = userTags;
    }

    public String getcTime() {
        return cTime;
    }

    public void setcTime(String cTime) {
        this.cTime = cTime;
    }

    public String getuTime() {
        return uTime;
    }

    public void setuTime(String uTime) {
        this.uTime = uTime;
    }

    public String getIsLockText() {
        return isLockText;
    }

    public void setIsLockText(String isLockText) {
        this.isLockText = isLockText;
    }

    public String getTimeText() {
        return timeText;
    }

    public void setTimeText(String timeText) {
        this.timeText = timeText;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUid() {
        return uid;
    }

    public String getMark() {
        return mark;
    }

    public void setMark(String mark) {
        this.mark = mark;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getBankStatus() {
        return bankStatus;
    }

    public void setBankStatus(String bankStatus) {
        this.bankStatus = bankStatus;
    }

    public String getIsActivity() {
        return isActivity;
    }

    public void setIsActivity(String isActivity) {
        this.isActivity = isActivity;
    }

    public String getActivityImage() {
        return activityImage;
    }

    public void setActivityImage(String activityImage) {
        this.activityImage = activityImage;
    }

    public ActivityBean getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(ActivityBean activityInfo) {
        this.activityInfo = activityInfo;
    }

    public class ActivityBean implements Serializable {
        private String title;
        private String desc;
        private String url;
        private String button;
        @SerializedName("popup_img")
        private String popupImg;

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

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getButton() {
            return button;
        }

        public void setButton(String button) {
            this.button = button;
        }

        public String getPopupImg() {
            return popupImg;
        }

        public void setPopupImg(String popupImg) {
            this.popupImg = popupImg;
        }
    }

    @Override
    public String toString() {
        return "UserIndexBean{" +
                "tel='" + tel + '\'' +
                ", openId='" + openId + '\'' +
                ", nickname='" + nickname + '\'' +
                ", headimg='" + headimg + '\'' +
                ", isLock=" + isLock +
                ", source=" + source +
                ", platform=" + platform +
                ", userTags=" + userTags +
                ", cTime=" + cTime +
                ", uTime=" + uTime +
                ", uid=" + uid +
                ", isLockText='" + isLockText + '\'' +
                ", timeText='" + timeText + '\'' +
                '}';
    }
}
