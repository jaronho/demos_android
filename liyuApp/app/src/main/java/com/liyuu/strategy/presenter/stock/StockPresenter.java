package com.liyuu.strategy.presenter.stock;

import android.content.Context;
import android.text.TextUtils;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.stock.StockContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.ui.stock.other.bean.SelectStockBean;
import com.liyuu.strategy.util.LogUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class StockPresenter extends RxPresenter<StockContract.View> implements StockContract.Presenter {

    private DataManager mDataManager;

    @Inject
    StockPresenter(DataManager mDataManager) {
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
                                if(response == null) return;
                                LogUtil.i(response.toString());
                                mView.showStockRealData(GsonUtil.getInstance().toJson(response));
                            }
                        }
        );
    }

//    @Override
//    public void getStockPayData(String symbol) {
//        RetrofitSendBody body = new RetrofitSendBody();
//        body.setUrl(UrlConfig.Stock_amount);
//        body.getSendData().setProdCode(symbol);
//
//        addSubscribe(true, mDataManager.fetchStockPayData(body)
//                .compose(RxUtil.<HongPanHttpResponse<StockDetailPayBean>>rxSchedulerHelper())
//                .compose(RxUtil.<StockDetailPayBean>handleHPResult(true))
//                .map(new Function<StockDetailPayBean, StockDetailPayBean>() {
//                    @Override
//                    public StockDetailPayBean apply(StockDetailPayBean bean) {
//                        if (bean != null && bean.getList() != null && bean.getList().size() > 0) {
//                            for (int i = 0, size = bean.getList().size(); i < size; i++) {
//                                if (bean.getList().get(i).getIsLock() == 0) {
//                                    bean.getList().get(i).setSelect(true);
//                                    mView.showDefaultSelect(i, bean.getList().get(i));
//                                    return bean;
//                                }
//                            }
//                        }
//                        return bean;
//                    }
//                })
//                .subscribeWith(new CommonSubscriber<StockDetailPayBean>(mView) {
//                    @Override
//                    public void onNext(StockDetailPayBean datas) {
//                        if (datas == null)
//                            return;
//                        mView.setPayStateAndNotifyMsg(datas.getStatus(), datas.getProhibitMsg());
//                        mView.showStockPay(datas.getList());
//                    }
//                }));
//    }

//    @Override
//    public void findCooperation(StockDetailPayBean.ItemBean bean, String period, String stockSymbol, String stockName) {
//        RetrofitSendBody body = new RetrofitSendBody();
//        body.setUrl(UrlConfig.Order_index);
//        body.getSendData().setPeriod(period);
//        body.getSendData().setSymbol(stockSymbol);
//        body.getSendData().setSname(stockName);
//        body.getSendData().setStockNum(String.valueOf(bean.getSymbolNum()));
//        body.getSendData().setMoney(bean.getMoney());
//        body.getSendData().setOpenId(mDataManager.getOpenId());
//
//        addSubscribe(true, mDataManager.createStockPay(body)
//                .compose(RxUtil.<HongPanHttpResponse<StockOrderDetailBean>>rxSchedulerHelper())
//                .compose(RxUtil.<StockOrderDetailBean>handleHPResult(true))
//                .subscribeWith(new CommonSubscriber<StockOrderDetailBean>(mView) {
//                    @Override
//                    public void onNext(StockOrderDetailBean bean) {
//                        if (bean == null)
//                            return;
//                        mView.startToPayFrozenAct(bean);
//                    }
//                }));
//    }

//    @Override
//    public void getStockPayLatestList(String stockCode) {
//        RetrofitSendBody body = new RetrofitSendBody();
//
//        body.setUrl(UrlConfig.Order_stockOrderNear);
//        body.getSendData().setSymbol(stockCode);
//
//        addSubscribe(false, mDataManager.getStockPayLatestList(body)
//                .compose(RxUtil.<HongPanHttpResponse<StockLatePayListBean>>rxSchedulerHelper())
//                .compose(RxUtil.<StockLatePayListBean>handleHPResult(true))
//                .map(new Function<StockLatePayListBean, StockLatePayListBean>() {
//                    @Override
//                    public StockLatePayListBean apply(StockLatePayListBean datas) {
//                        if (datas == null || datas.getOrderList() == null)
//                            return datas;
//                        List<StockLatePayListBean.SonBean> newList = new ArrayList<>();
//                        for (StockLatePayListBean.SonBean bean : datas.getOrderList()) {
//                            if (Double.parseDouble(bean.getMoney()) >= 50000.f)
//                                newList.add(bean);
//                        }
//                        mView.showBigOrder(newList);
//                        return datas;
//                    }
//                })
//                .subscribeWith(new CommonSubscriber<StockLatePayListBean>(mView) {
//                    @Override
//                    public void onNext(StockLatePayListBean datas) {
//                        mView.showPayLateList(datas.getOrderList());
//                    }
//                }));
//
//    }

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
}
