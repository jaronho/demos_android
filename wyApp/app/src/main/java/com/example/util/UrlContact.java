package com.example.util;

public class UrlContact {
    /**
     * 线上正式地址
     */
//  public final static String URL_STRING = "http://twmn.16899.com";
    public final static String URL_STRING = "http://wmn.16899.com";
    public final static String WEB_URL_STRING = "http://m.16899.com";

//    public final static String URL_STRING = "http://119.254.84.227:17910";//线上测试地址
//    public final static String WEB_URL_STRING = "http://ttm.16899.com";    //线上测试地址

//    public final static String URL_STRING = "http://192.168.80.99:8097";//线下测试地址
//    public final static String WEB_URL_STRING = "http://192.168.80.99:8099";//线下测试地址


    //公共接口
    public final static String URL_CONFIGURE = URL_STRING + "/api/version/GetConfigure";

    //APP版本
    public final static String URL_VERSION = URL_STRING + "/api/version/get";

    //获取图片
    public final static String URL_AD = URL_STRING + "/api/Home/GetWelcomePageImgage";

    //发送错误信息log
    public final static String URL_RECORD_LOG = URL_STRING + "/api/version/RecordLog";

    //获取GPS
    public final static String URL_GPS = URL_STRING + "/api/user/ConvertAddr";

    //分享
    public final static String URL_PRODUCT_SHARE = URL_STRING + "/api/UserCenter/MyInviteUrl";

    /**
     * 首页接口
     */

    //首页热门商品
    public final static String URL_COMMEND_SHOPPING = URL_STRING + "/api/product/CommendProList";

    //首页优惠商品列表
    public final static String URL_COUPON_SHOPPING = URL_STRING + "/api/home/GetCouponRuleProInfo";

    //首页内容
    public final static String URL_RESOURCE_LIST = URL_STRING + "/api/Home/AdResourceList";

    //抢代金券
    public final static String URL_TAKE_COUPON = URL_STRING + "/Api/Coupon/TakeCoupon";

    //去抽奖
    public final static String URL_TAKE_LOTTERY = URL_STRING + "/api/UserCenter/LuckyDrawAddr";

    /**
     * 分类
     */
    public final static String URL_CLASSIFICATION = URL_STRING + "/api/Product/ProductCatelogType";

    /**
     * 我的购物车
     */

    //去购物车
    public final static String URL_SHOPPING_CART = URL_STRING + "/api/Order/Cart";

    /**
     * 我的农一
     */

    //我的农一
    public final static String URL_NY = URL_STRING + "/api/UserCenter/Index";

    //我的订单
    public final static String URL_ORDER = URL_STRING + "/api//UserCenter/MyOrder";

    //我的订单的button点击
    public final static String URL_ORDER_ACTION = URL_STRING + "/api/UserCenter/OrderAction";

    //积分记录
    public final static String URL_SCORE = URL_STRING + "/api/UserCenter/UserPointsAndRecordList";

    //签到
    public final static String URL_SIGN_IN = URL_STRING + "/api/UserCenter/UpdateUserPoints";

    //发送收益提现请求
    public final static String URL_SEND_FINANCE = URL_STRING + "/api/UserCenter/SendFinanceTakeVCode";

    //转入余额或提现
    public final static String URL_DO_FINANCE = URL_STRING + "/api/UserCenter/DoFinanceTake";

    /**
     * 产品详情
     */

    //产品详情
    public final static String URL_PRODUCT_DETAIL = URL_STRING + "/api/product/GetProductById";

    //秒杀产品详情
    public final static String URL_SEC_KILL_PRODUCT_DETAIL = URL_STRING + "/api/product/GetSeckillProductById";

    //产品优惠
    public final static String URL_PRODUCT_COUPON_INFO = URL_STRING + "/api/product/GetProCouponInfo";

    //产品物流
    public final static String URL_PRODUCT_STOCK = URL_STRING + "/api/product/GetProStock";

    //产品地址
    public final static String URL_PRODUCT_INTRODUCE = URL_STRING + "/Pro/Detail?proId=";

    //产品介绍
    public final static String URL_PRODUCT_ADDRESS = URL_STRING + "/api/user/GetAreaByParentId";

    //产品抢购
    public final static String URL_PRODUCT_RUSH_SHOP = URL_STRING + "/api/product/GetSpikeQualification";

    /**
     * 产品列表
     */

    //产品列表
    public final static String URL_PRODUCT_LIST = URL_STRING + "/api/Product/productlist";

    /**
     * 我的代购
     */
    //能否申请代购
    public final static String URL_CAN_APPLY = URL_STRING + "/api/usercenter/GetProtocolAreaSum";

    /**
     * 代金券
     */

    //我的代金券
    public final static String URL_MY_COUPON = URL_STRING + "/api/UserCenter/MyCoupon";

    //支付使用的代金券
    public final static String URL_PAY_COUPON = URL_STRING + "/api/pay/GetUserCoupon";

    //支付选择的代金券
    public final static String URL_CHECK_COUPON = URL_STRING + "/api/pay/CheckCoupon";

    /**
     * 支付
     */
    //提交订单
    public final static String URL_SUBMIT_ORDER = URL_STRING + "/api/Order/SubmitOrder";

    //获取地址
    public final static String URL_ORDER_ADDRESS = URL_STRING + "/api/user/GetAreaByParentId";

    /**
     * 个人信息
     */
    //获取个人信息
    public final static String URL_GET_USER_INFO = URL_STRING + "/api/UserCenter/GetUserInfo";

    //保存个人信息
    public final static String URL_SAVE_USER_INFO = URL_STRING + "/api/UserCenter/SaveUserInfo";

    //上传头像
    public final static String URL_UPLOAD_AVATAR = URL_STRING + "/api/UserCenter/UploadAvatar";

    /**
     * 我的收益
     */
    public final static String URL_PURCHASING_LIST = URL_STRING + "/api/UserCenter/PurchasingList";
}
