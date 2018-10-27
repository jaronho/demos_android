package com.liyuu.strategy.presenter.transaction;


import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.app.StockField;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.transaction.NewsWelfareContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.CreateSimulatedOrderBean;
import com.liyuu.strategy.model.bean.NewIndexBean;
import com.liyuu.strategy.model.bean.StockBean;
import com.liyuu.strategy.ui.MainActivity;
import com.liyuu.strategy.ui.TransactionFragment;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.ToastUtil;
import com.liyuu.strategy.util.UserInfoUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class NewsWelfarePresenter extends RxPresenter<NewsWelfareContract.View> implements NewsWelfareContract.Presenter {
    private DataManager mDataManager;
    private int payStockNumber;
    private int maxStockNumber;

    @Inject
    NewsWelfarePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }


    @Override
    public void getStockSingle() {
        Map<String, Object> params = new HashMap<>();
        params.put("type", 1);
        String url = UrlConfig.Trade_getStockList;
        post(mDataManager.getStockSingle(url, params), new CommonSubscriber<StockBean>(mView, url) {
            @Override
            public void onNext(StockBean bean) {
                super.onNext(bean);
                mView.loadData(bean);
            }
        });
    }

    @Override
    public void getNewIndex(String stockSymbol) {
        Map<String, Object> params = new HashMap<>();
        params.put("symbol", stockSymbol);
        String url = UrlConfig.Trade_new_index;
        post(mDataManager.getNewIndex(url, params), new CommonSubscriber<NewIndexBean>(mView, url) {
            @Override
            public void onNext(NewIndexBean newIndexBean) {
                super.onNext(newIndexBean);
                mView.showIndex(newIndexBean);
            }
        });
    }

    /**
     * 用户确认押金
     *
     * @param stockPrice              当前股票价格
     * @param tradeBuyStockMoney      用户押金
     * @param gearing                 杆杠系数
     * @param tradeMaxStopLossPrecent 股票交易止损系数
     */
    @Override
    public void userSelectMargin(double stockPrice, double tradeBuyStockMoney, double gearing, double tradeMaxStopLossPrecent) {
        maxStockNumber = (int) (Math.floor((tradeBuyStockMoney * gearing / stockPrice) / 100.f) * 100);//股票可购买最大量
        payStockNumber = maxStockNumber;

        double tradeStopLossMoney = tradeBuyStockMoney * tradeMaxStopLossPrecent;//用户押金*押金最大k值=用户最大亏损价钱

        mView.setStockNumberTextView(stockPrice);

        mView.setStockNumber(maxStockNumber);

        double userTurnoverMoney = stockPrice * maxStockNumber;//用户购买股票实际价格
        mView.setUserTurnover(userTurnoverMoney);

        double stopPrecent = tradeStopLossMoney / userTurnoverMoney;//止损百分比=(用户押金*k)/用户购买股票实际价格
        if (Double.isInfinite(stopPrecent))
            stopPrecent = 0.f;
        mView.setStopLossTextView(tradeStopLossMoney, stopPrecent);

        mView.setStockStopLossPriceTextView(stockPrice * (1 - stopPrecent));
    }

    @Override
    public void getStockReal(final String symbol) {
        if (TextUtils.isEmpty(symbol)) return;
        Map<String, Object> map = new HashMap<>();
        map.put("en_prod_code", symbol);
        map.put("fields", "prod_name,last_px,px_change,px_change_rate");
        String url = UrlConfig.stock_real;
        post(false, mDataManager.fetchStockReal(url, map),
                new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object s) {
                        super.onNext(s);
                        String data = GsonUtil.toString(s);
                        if (TextUtils.isEmpty(data))
                            return;
                        JsonObject snapshot = GsonUtil.getJsonObject(data, "data", "snapshot");
                        if (snapshot == null) return;
                        if (!snapshot.has(symbol)) return;
                        JsonArray array = snapshot.get(symbol).getAsJsonArray();

                        int position = GsonUtil.getStockFieldsPosition(snapshot, StockField.STOCK_NAME);
                        String stockName = GsonUtil.arrayGetString(array, position);

                        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.LAST_PX);
                        String stockPrice = GsonUtil.arrayGetString(array, position);

                        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE);
                        String price = GsonUtil.arrayGetString(array, position);

                        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE_RATE);
                        String pricePercent = GsonUtil.arrayGetString(array, position);
                        mView.showStockRealData(stockPrice, price, pricePercent);
                    }
                });
    }

    /**
     * 用户更改
     *
     * @param action                  动作 1：+100 0：不变（用于股票价格变化刷新数据）-1：-100
     * @param stockPrice              股票价格
     * @param tradeBuyStockMoney      定金
     * @param tradeMaxStopLossPercent 股票交易止损系数
     */
    @Override
    public void changeStockPayNumber(int action, double stockPrice, double tradeBuyStockMoney, double tradeMaxStopLossPercent) {
        int r = payStockNumber + action * 100;
        if (r > maxStockNumber || r <= 0) return;
        payStockNumber = r;

        double tradeStopLossMoney = tradeBuyStockMoney * tradeMaxStopLossPercent;

        mView.setStockNumberTextView(stockPrice);

        mView.setStockNumber(payStockNumber);

        double userTurnoverMoney = stockPrice * payStockNumber;
        mView.setUserTurnover(userTurnoverMoney);

        double stopPercent = tradeStopLossMoney / userTurnoverMoney;
        if (Double.isInfinite(stopPercent))
            stopPercent = 0.f;

        mView.setStopLossTextView(tradeStopLossMoney, stopPercent);

        mView.setStockStopLossPriceTextView(stockPrice * (1 - stopPercent));
    }

    @Override
    public void createOrder(String symbol, String stockName, double deposit, int period, int stockNum) {
        if (stockNum <= 0) {
            ToastUtil.showMsg("请选择购买的股票数量");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("period", period);
        map.put("deposit", deposit);
        map.put("symbol", symbol);
        map.put("sname", stockName);
        map.put("stock_num", stockNum);
        map.put("discount_fee", 0);
        String url = UrlConfig.Trade_createOrder;
        post(mDataManager.createSimulatedOrder(url, map),
                new CommonSubscriber<CreateSimulatedOrderBean>(mView, url) {
                    @Override
                    public void onNext(CreateSimulatedOrderBean data) {
                        super.onNext(data);
                        if (data != null) {
                            PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_IS_ACTIVITY, "");
                            PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_ACTIVITY_IMAGE, "");
                            RxBus.get().send(RxBus.Code.NEWS_WELFARE_BUYING_SUCCESS);
                            mView.finishUI();
                        }
                        ToastUtil.showMsg("购买成功");
                    }
                });
    }

    @Override
    public void checkStatus() {
        if (!UserInfoUtil.isLogin())
            return;
        String is_activity = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_IS_ACTIVITY);
        if (!"1".equals(is_activity) && mView != null) {
            mView.showCloseDialog();
        }
    }
}
