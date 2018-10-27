package com.liyuu.strategy.prefs;

import android.text.TextUtils;

import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.model.bean.UserIndexBean;
import com.liyuu.strategy.util.PreferenceUtils;
import javax.inject.Inject;

/**
 * sharepreference 数据读取 具体实现类
 */

public class ImplPreferencesHelper implements PreferencesHelper {

    private static final String SHAREDPREFERENCES_NAME = "hongpan_sharepreference";

    @Inject
    public ImplPreferencesHelper() {

    }

    @Override
    public String getImei() {
        return PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.COMMON_UUID, "");
    }

    @Override
    public void setImei(String imei) {
        PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.COMMON_UUID, imei);
    }

    @Override
    public String getOpenId() {
        return PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_OPEN_ID, "");
    }

    @Override
    public void setUserInfo(UserIndexBean bean) {
        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_OPEN_ID, bean.getOpenId());
        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME, bean.getNickname());
        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_UID_STR, bean.getUid());
        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER, bean.getTel());
        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HEADER_URL, bean.getHeadimg());
        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_BANK_STATUS, bean.getBankStatus());
        if(!TextUtils.isEmpty(bean.getPayPwd())) {
            PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HAS_PAY_PWD, true);
        }
        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_IS_ACTIVITY, bean.getIsActivity());
        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_ACTIVITY_IMAGE, bean.getActivityImage());
    }
}
