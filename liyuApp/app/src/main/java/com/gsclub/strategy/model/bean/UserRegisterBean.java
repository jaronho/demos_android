package com.gsclub.strategy.model.bean;

import java.io.Serializable;

/**
 * Created by hlw on 2018/5/23.
 */

public class UserRegisterBean implements Serializable {


    /**
     * tel : 13300000009
     * pwd : 218dbb225911693af03a713581a7227f
     */

    private String tel;
    private String pwd;

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "UserRegisterBean{" +
                "tel='" + tel + '\'' +
                ", pwd='" + pwd + '\'' +
                '}';
    }
}
