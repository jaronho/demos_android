package com.liyuu.strategy.util;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.ui.MainActivity;
import com.liyuu.strategy.ui.login.LoginActivity;
import com.qiyukf.unicorn.api.Unicorn;
import com.tencent.android.tpush.XGPushConfig;

public class UserInfoUtil {

    /**
     * 判断用户是否登录
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_OPEN_ID));
    }

    /**
     * 判断用户是否已设置交易密码
     */
    public static boolean hasPayPwd() {
        return PreferenceUtils.getBoolean(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HAS_PAY_PWD);
    }

    /**
     * 判断用户是否已绑卡
     */
    public static boolean hasBindCard() {
        return "1".equals(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_BANK_STATUS));
    }

    /**
     * 判断用户是否登录/未登录跳转至登录界面
     *
     * @return
     */
    public static boolean isWithLogin(Context context) {
        boolean isLogin = !TextUtils.isEmpty(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_OPEN_ID));
        if (!isLogin)
            LoginActivity.start(context);
        return isLogin;
    }

    /**
     * 用户退出登录时需要进行的操作
     */
    public static void logout() {
        //清除用户的登录信息
        PreferenceUtils.cleanUserInfo();
        App.getInstance().removeCookies();
        Unicorn.logout();
        RxBus.get().send(RxBus.Code.USER_LOGIN_OUT);
    }

    /**
     * 处理code=1099的情况，请先登录
     */
    public static void doLogIn() {
        Context context = App.getInstance();
        logout();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("is_login_again", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 处理code=1098的情况，单点登录
     */
    public static void doSingleSignOn() {
        Context context = App.getInstance();
        logout();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra("is_single_sign_on", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * 反显用户手机号
     *
     * @param tel
     * @return
     */
    public static String invertTel(String tel) {
        int len = tel.length();
        if (len >= 7) {
            StringBuffer sb = new StringBuffer();
            sb.append(tel.substring(0, 3));
            sb.append("****");
            sb.append(tel.substring(len - 4, len));
            return sb.toString();
        }
        return tel;
    }

    /**
     * 重置记录下的用户操作信息
     */
    public static void resetUserOperating(Context context) {
        //将股票交易模式重置为正常交易
        PreferenceUtils.put(SPKeys.FILE_TRADE, SPKeys.TRADE_MODE_INT, 1);
    }

    public static String getPushToken() {
        String token = PreferenceUtils.getString(SPKeys.FILE_COMMON, SPKeys.XG_PUSH_TOKEN);
        if (!TextUtils.isEmpty(token)) return token;
        token = XGPushConfig.getToken(App.getInstance());
        if (TextUtils.isEmpty(token)) {
            return "";
        }
        PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.XG_PUSH_TOKEN, token);
        return token;
    }
}
