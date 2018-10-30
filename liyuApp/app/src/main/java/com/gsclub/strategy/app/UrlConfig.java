package com.gsclub.strategy.app;

/**
 * Created by hlw on 2018/5/22.
 * http请求url
 */

public interface UrlConfig {

    /**
     * app版本更新
     */
    String Other_appUpdate = "Other/appUpdate";

    /**
     * 获取策略各个web也页面地址
     */
    String Other_webset = "Other/webset";

    /**
     * 注册
     */
    String User_reg = "User/reg";

    /**
     * 登录
     */
    String User_login = "User/login";

    /**
     * 退出登陆
     */
    String User_logOut = "User/logOut";

    /**
     * 用户修改昵称和头像
     */
    String User_editUserInfo = "User/editUserInfo";


    /**
     * 用户修改密码
     */
    String User_userEdit = "User/userEdit";

    /**
     * 我的-修改密码
     */
    String User_chgLoginPwd = "User/chgLoginPwd";

    /**
     * 轮播图
     */
    String index_slider = "index/slider";

    /**
     * 自媒体认证、用户协议
     */
    String index_commParams = "index/commParams";

    /**
     * 首页-大家都在买
     */
    String Index_lastbuylist = "Index/lastbuylist";

    /**
     * 发送短信
     */
    String User_sendsms = "User/sendsms";

    /**
     * 搜索文章/股票/用户
     */
    String index_indexsearch = "index/indexsearch";

    /**
     * 获取自选股模块首页三个指数
     */
    String Stock_stockIndex = "Stock/stockIndex";

    /**
     * 股票获取实时数据
     */
    String stock_real = "Stock/real";

    /**
     * 5日分时图
     */
    String Stock_trend5day = "Stock/trend5day";

    /**
     * 股票分时数据
     */
    String Stock_trend = "Stock/trend";

    /**
     * 股票获取K线数据
     */
    String stock_Kline = "Stock/Kline";

    /**
     * 公告主菜单列表
     */
    String User_announceIndex = "User/announceIndex";

    /**
     * 获取首页菜单栏
     */
    String Index_indexMenu = "Index/indexMenu";

    /**
     * 公告子菜单列表
     */
    String User_getAnnounceList = "User/getAnnounceList";

    /**
     * 上传头像
     */
    String User_uploadHeadImg = "User/uploadHeadImg";

    /**
     * 我的-发送短信验证码
     */
    String User_sendMsgCode = "User/sendMsgCode";

    /**
     * 我的-修改手机号
     */
    String User_chgTel = "User/chgTel";

    /**
     * 我的-意见反馈 图片上传接口
     */
    String User_feedbackImage = "User/feedbackImage";

    /**
     * 我的-意见反馈 接口
     */
    String User_feedback = "User/feedback";

    /**
     * 盈利榜/收益率列表
     */
    String Rank_rankList = "Rank/rankList";

    /**
     * 收益榜详情
     */
    String Rank_rankInfo = "Rank/rankInfo";

    /**
     * 收益榜交易记录
     */
    String Rank_settleInfo = "Rank/settleInfo";

    /**
     * 交易下单
     */
    String Trade_index = "Trade/index";

    /**
     * 用户账户信息
     */
    String Trade_accountDetail = "Trade/accountDetail";

    /**
     * 资金流水
     */
    String Trade_capitalDetail = "Trade/capitalDetail";

    /**
     * 委托列表
     */
    String Trade_entrustmentList = "Trade/entrustmentList";

    /**
     * 持仓列表
     */
    String Trade_holdingList = "Trade/holdingList";

    /**
     * 结算列表
     */
    String Trade_settleList = "Trade/settleList";

    /**
     * 结算详情
     */
    String Trade_settleContent = "Trade/settleContent";

    /**
     * 持仓详情
     */
    String Trade_HoldingContent = "Trade/HoldingContent";

    /**
     * 股票下单
     */
    String Trade_createOrder = "Trade/createOrder";

    /**
     * 股票卖出
     */
    String Trade_stockSale = "Trade/stockSale";

    /**
     * 止损价调整
     */
    String Trade_changeStopLoss = "Trade/changeStopLoss";

    /**
     * 递延状态修改
     */
    String Trade_changeAutoDefer = "Trade/changeAutoDefer";

    /**
     * 模拟委托买入页面参数
     */
    String Simulated_simulatedindex = "Simulated/simulatedindex";

    /**
     * 模拟资金账户信息
     */
    String Simulated_simulatedaccount = "Simulated/simulatedaccount";

    /**
     * 模拟持股列表
     */
    String Simulated_simulatedholdingList = "Simulated/simulatedholdingList";

    /**
     * 模拟股票结算列表
     */
    String Simulated_simulatedSettleList = "Simulated/simulatedSettleList";

    /**
     * 模拟股票下单
     */
    String Simulated_simulatedcreateOrder = "Simulated/simulatedcreateOrder";

    /**
     * 模拟股票卖出
     */
    String Simulated_simulatedSale = "Simulated/simulatedSale";

    /**
     * 我的战绩
     */
    String user_myRecord = "user/myRecord";

    /**
     * 历史交易记录
     */
    String user_myHistory = "user/myHistory";

    /**
     * 用户修改设置交易密码① 发送短信验证码
     */
    String User_sendPaypwdMsgCode = "User/sendPaypwdMsgCode";

    /**
     * 用户修改设置交易密码② 修改/设置交易密码
     */
    String User_changeTradePass = "User/changeTradePass";

    /**
     * 银行列表
     */
    String UserBank_bankList = "UserBank/bankList";

    /**
     * 用户绑卡信息
     */
    String UserBank_getUserBankInfo = "UserBank/getUserBankInfo";

    /**
     * 用户绑卡
     */
    String UserBank_bindBank = "UserBank/bindBank";

    /**
     * 用户充值页面
     */
    String Account_recharge_index = "Account/recharge_index";

    /**
     * 用户充值下单
     */
    String Account_recharge_order = "Account/recharge_order";

    /**
     * 用户提现页面
     */
    String Account_cash_index = "Account/cash_index";

    /**
     * 用户提现订单
     */
    String Account_cash_order = "Account/cash_order";

    /**
     * open_id登录
     */
    String User_loginByOpenId = "User/loginByOpenId";

    /**
     * 新手福利股票
     */
    String Trade_getStockList = "Trade/getStockList";

    /**
     * 活动分享信息获取
     */
    String Activity_activityShare = "Activity/activityShare";

    /**
     * 新手福利股票参数
     */
    String Trade_new_index = "Trade/new_index";

    /**
     * 新手福利banner
     */
    String Index_getActivityImages = "Index/getActivityImages";

    /**
     * APP充值订单查询接口
     */
    String Account_chk_recharge_order = "Account/chk_recharge_order";

    /**
     * 审核版本控制接口
     */
    String User_activityControl = "User/activityControl";

}

