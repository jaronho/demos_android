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
public @interface StockField {
    String STOCK_NAME = "prod_name";//股票名称
    String BID_GRP = "bid_grp";//委买档位
    String OFFER_GRP = "offer_grp";//委卖档位
    String PRECLOSE_PX = "preclose_px";//昨收价
    String LAST_PX = "last_px";//最新价
    String PX_CHANGE = "px_change";//价格涨跌
    String PX_CHANGE_RATE = "px_change_rate";//涨跌幅
}
