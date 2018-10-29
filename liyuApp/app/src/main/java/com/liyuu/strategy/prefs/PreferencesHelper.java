package com.liyuu.strategy.prefs;

import com.liyuu.strategy.model.bean.UserIndexBean;

/**
 * sharepreference 操作接口
 */
public interface PreferencesHelper {
    //获取本机设备的唯一标识（非imei，随机生成）
    String getImei();

    void setImei(String imei);

    //获取当前使用账户的openid（类似账户唯一标识）
    String getOpenId();

    void setUserInfo(UserIndexBean bean);

}
