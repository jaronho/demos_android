package com.liyuu.strategy.presenter.optional;

import android.content.Context;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.optional.SelectStockContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.CustomSelectStockBean;
import com.liyuu.strategy.ui.stock.other.bean.SelectStockBean;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class SelectStockPresenter extends RxPresenter<SelectStockContract.View> implements SelectStockContract.Presenter {

    private DataManager mDataManager;
    private boolean isHadAddThisStock = false;//当前股票是否被添加为自选股

    @Inject
    SelectStockPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getDefaultData(Context context) {
        List<SelectStockBean> listOne = new ArrayList<>();
        List<SelectStockBean> listTwo = new ArrayList<>();

        SelectStockBean bean;
        String[] s1 = context.getResources().getStringArray(R.array.select_stock_one);
        for (String s : s1) {
            bean = new SelectStockBean();
            bean.setItemName(s);
            listOne.add(bean);
        }

        String[] s2 = context.getResources().getStringArray(R.array.select_stock_two);
        for (String s : s2) {
            bean = new SelectStockBean();
            bean.setItemName(s);
            listTwo.add(bean);
        }

        mView.showDefaultData(listOne, listTwo);
    }

    @Override
    public void getStockReal(String symbol) {
        String fields = "last_px,px_change,px_change_rate,trade_status," +
                "open_px,business_amount,preclose_px,turnover_ratio," +
                "high_px,low_px,business_amount_in,business_amount_out,pe_rate,amplitude,market_value,circulation_value,prod_name," +
                "bid_grp,offer_grp,systime";
        //最新价,价格涨跌,涨跌幅,交易状态
        //开盘价,成交量,收盘价,换手率
        //最高价,最低价,内盘成交量,外盘成交量,市盈率,振幅,证券市值,流通市值,产品名称
        //委买档位,委卖档位,股票时间
        Map<String, Object> params = new HashMap<>();
        params.put("en_prod_code", symbol);
        params.put("fields", fields);
        String url = UrlConfig.stock_real;
        post(false, mDataManager.getStockReal(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object response) {
                        if (response == null) return;
                        LogUtil.i(response.toString());
                        mView.showStockRealData(GsonUtil.getInstance().toJson(response));
                    }
                }
        );
    }

    /**
     * 取值可以是数字1-9，表示含义如下： 1：1分钟K线 2：5分钟K线 3：15分钟K线 4：30分钟K线 5：60分钟K线 6：日K线 7：周K线 8：月K线 9：年K线
     */
    @Override
    public int getKlineType(int position) {
        int klineType = -1;
        if (position == 2)
            klineType = 6;
        else if (position == 3)
            klineType = 7;
        else if (position == 4)
            klineType = 8;
        else if (position == 5)
            klineType = 2;
        else if (position == 6)
            klineType = 3;
        else if (position == 7)
            klineType = 4;
        else if (position == 8)
            klineType = 5;
        return klineType;
    }

    @Override
    public void queryThisStockIsSelect(String stockSymbol) {
        CustomSelectStockBean bean = (CustomSelectStockBean) mDataManager.dbQueryDataByField(CustomSelectStockBean.class, "symbol", stockSymbol);
        isHadAddThisStock = !(bean == null);
        mView.showAddStockView(isHadAddThisStock);
    }

    @Override
    public void changeAddStock(String symbol, String stockName) {
        if (isHadAddThisStock) {
            //删除自选股
            mDataManager.dbDeleteData(CustomSelectStockBean.class, "symbol", symbol);
        } else {
            //添加自选股
            CustomSelectStockBean bean = new CustomSelectStockBean();
            bean.setAddTime(System.currentTimeMillis());
            bean.setSymbol(symbol);
            bean.setSname(stockName);
            mDataManager.dbInsertData(bean);
        }

        RxBus.get().send(RxBus.Code.MINE_OPTOPNAL_STOCK_DATA_CHANGED);
        isHadAddThisStock = !isHadAddThisStock;
        mView.showAddStockView(isHadAddThisStock);
    }
}
