package com.liyuu.strategy.model.bean;

import java.io.Serializable;

public class WebSetBean implements Serializable {

    private String tutorial;
    private String invite;
    private String privacy;
    private String service;
    private String cooperation;
    private String customer_wechat;

    public String getTutorial() {
        return tutorial;
    }

    public void setTutorial(String tutorial) {
        this.tutorial = tutorial;
    }

    public String getInvite() {
        return invite;
    }

    public void setInvite(String invite) {
        this.invite = invite;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getCooperation() {
        return cooperation;
    }

    public void setCooperation(String cooperation) {
        this.cooperation = cooperation;
    }

    public String getCustomer_wechat() {
        return customer_wechat;
    }

    public void setCustomer_wechat(String customer_wechat) {
        this.customer_wechat = customer_wechat;
    }
}
