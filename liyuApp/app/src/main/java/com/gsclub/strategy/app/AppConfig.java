package com.gsclub.strategy.app;

import com.gsclub.strategy.BuildConfig;

public interface AppConfig {

    String DOMAIN = BuildConfig.ENV_DEBUG ? "huijindou.com" : "huijindou.com";
    String API_IP = "http://api." + DOMAIN;
    //    String API_IP = "http://118.31.18.180:80";
    //    String API_IP = "https://api." + DOMAIN;
    String FILE_IP = API_IP + "/index/Upload/upload_img.html";

    String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:54.0) Gecko/goldfishapp android " + BuildConfig.VERSION_NAME;
    String AES_KEY = "a*jyxga#+wdfa%nd";
    String DEVICE_TYPE = "and17#$ro*id";
    String PLATFORM = "jyand$ro*id";
    String EN_KEY = "anj#*yud";
    String WX_APP_KEY = BuildConfig.ENV_DEBUG ? "wx4616a360fb2101a3" : "wxc55dfe7c6bf4e031";// 微信
    String WX_APP_SECRET = BuildConfig.ENV_DEBUG ? "e87279b620f391ded9480ab93e32cf41" : "896db2ef47f76ed0b0dd23c8d8cd9dbc";// 微信
    boolean LLENVIROMENT = false;//连连支付环境     true为测试环境，不推荐使用,false为正式环境
    String UMENG_APP_KEY = BuildConfig.ENV_DEBUG ? "5bdaf9eaf1f556fbc9000121" : "5bad85d5f1f556470400006d";//测试环境鲸鱼选股测试账号，正式环境鲸鱼选股正式环境

    /**
     * androidManifest 中配置的authorities
     */
    String AUTHROES = "com.gsclub.strategy.fileprovider";
}