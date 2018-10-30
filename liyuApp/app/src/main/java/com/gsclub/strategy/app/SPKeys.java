package com.gsclub.strategy.app;

import android.support.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Administrator on 2017-07-07.
 * sharepreference keys
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
@StringDef
public @interface SPKeys {
    /**
     * 广告存储的文件名
     */
    String FILE_AD = "file_ad";

    /**
     * 用户信息存储的文件名
     */
    String FILE_USER_INFO = "file_user_info";
    String USER_INFO_OPEN_ID = "open_id";
    String USER_INFO_NICKNAME = "user_info_nickname";
    String USER_INFO_UID_STR = "user_info_uid_str";
    String USER_INFO_PHONE_NUMBER = "user_info_phone_number";
    String USER_INFO_HEADER_URL = "user_info_header_url";
    String USER_INFO_HAS_PAY_PWD = "user_info_has_pay_pwd";
    String USER_INFO_BANK_STATUS = "user_info_bank_status";
    String USER_INFO_IS_ACTIVITY = "user_info_is_activity";
    String USER_INFO_ACTIVITY_IMAGE = "user_info_activity_image";
    String USER_INFO_COOKIE_SESSION_ID = "user_info_cookie_session_id";
    String USER_INFO_COOKIE_DOMAIN = "user_info_cookie_domain";

    /**
     * 通用信息存储
     */
    String FILE_COMMON = "file_common";
    String COMMON_UUID = "uuid";
    String GUIDE_VERSION = "guide_version";
    String XG_PUSH_TOKEN = "xg_push_token";//信鸽推送token
    String COMMON_IS_NOTIFICATION_CLICKED = "is_notification_clicked";
    String COMMON_PUSH_EXTRAS = "push_extras";
    String COMMON_NEW_VERSION = "new_version";
    String COMMON_IGNORE_VERSION = "ignore_version";
    String ACTIVITY_STATUS = "activity_status";
    String JUMP_URL = "jump_url";

    /**
     * 网址信息存储
     */
    String FILE_URL = "file_url";

    String URL_INVITE = "url_invite";//邀请
    String URL_TUTORIAL = "url_tutorial";//新手教程
    String URL_PRIVACY = "url_privacy";//隐私协议
    String URL_SERVICE = "url_service";//用户服务协议
    String URL_COOPERATION = "url_cooperation";//沪、深A股交易合作协议
    String CUSTOMER_WE_CHAT = "customer_wechat";//客服微信号

    /**
     * 交易数据信息存储
     */
    String FILE_TRADE = "file_trade";
    String TRADE_MODE_INT = "trade_mode_int";//股票交易模式 1.正常交易 2.模拟交易


    /**
     * 其他信息存储
     */
    String FILE_OTHER = "file_other";
    String OTHER_TRANSACTION_SELECT_INT = "other_transaction_select_int";//交易界面选择
}
