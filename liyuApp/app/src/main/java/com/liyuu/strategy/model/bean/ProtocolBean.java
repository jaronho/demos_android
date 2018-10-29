package com.liyuu.strategy.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProtocolBean implements Serializable {
    @SerializedName("user_agreement")
    private String userAgreement;

    public String getUserAgreement() {
        return userAgreement;
    }

    public void setUserAgreement(String userAgreement) {
        this.userAgreement = userAgreement;
    }
}
