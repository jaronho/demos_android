package com.gsclub.strategy.tpush;

/**
 * Created by 640 on 2018/3/21 0021.
 */

public class MessageConstants {
    public static final String KEY_CLASSNAME = "classname";
    public static final String KEY_PARAMS = "params";
    public static final String KEY_EXTRAS = "extras";
    public static final String KEY_OPEN_ID = "open_id";
    public static final String KEY_PARAMS_ACTION_TYPE = "action_type";

    public static final String VALUE_SINGLE_SIGN_ON = "SingleSignOn";// 单点登录
    public static final String VALUE_WEB_VIEW_ACTIVITY = "com.gsclub.strategy.intent.WebViewActivity";// 系统消息详情或url，需要参数
    /**
     * 推送消息跳转至订单详情界面
     * 需带参数 uid oid
     */
    public static final String VALUE_POSITION_DETAIL_ACTIVITY = "gsclub.strategy.intent.PositionDetailActivity";

    /**
     * 推送消息跳转至持仓详情界面
     * 需要携带参数 oid
     */
    public static final String VALUE_SETTLEMENT_DETAIL_ACTIVITY = "com.gsclub.strategy.ui.transaction.activity.SettlementDetailActivity";

    /**
     * 推送消息跳转至交易列表界面
     * 需要参数action_type
     */
    public static final String VALUE_TRADE_LIST_CHOOSE = "com.gsclub.strategy.ui.TransactionFragment";

    /**
     * 跳转至首页
     */
    public static final String VALUE_MAINACTIVITY = "com.gsclub.strategy.ui.MainActivity";
}
