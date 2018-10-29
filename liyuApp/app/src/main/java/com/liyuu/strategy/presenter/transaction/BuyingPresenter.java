package com.liyuu.strategy.presenter.transaction;


import android.content.Context;
import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liyuu.strategy.R;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.app.StockField;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.transaction.BuyingContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.CreateSimulatedOrderBean;
import com.liyuu.strategy.model.bean.TradeIndexBean;
import com.liyuu.strategy.util.CalculationUtil;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.ToastUtil;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class BuyingPresenter extends RxPresenter<BuyingContract.View> implements BuyingContract.Presenter {
    private DataManager mDataManager;
    private int payStockNumber;
    private int maxStockNumber;

    @Inject
    BuyingPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getStockReal(final String symbol, String fields) {
        Map<String, Object> map = new HashMap<>();
        map.put("en_prod_code", symbol);
        map.put("fields", fields);
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
                        double stockPrice = CalculationUtil.round(GsonUtil.arrayGetFloat(array, position), 2);
                        mView.setStockNumberTextView(stockPrice);

                        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE);
                        double priceFloat = CalculationUtil.round(GsonUtil.arrayGetFloat(array, position), 2);

                        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE_RATE);
                        double pricePrecent = CalculationUtil.round(GsonUtil.arrayGetFloat(array, position), 2);
                        mView.showStockRealData(data, stockName, stockPrice, priceFloat, pricePrecent);
                    }
                });
    }

    @Override
    public void getTradeIndex(String symbol) {
        String url = PreferenceUtils.getInt(SPKeys.FILE_TRADE, SPKeys.TRADE_MODE_INT, 1) == 1 ?
                UrlConfig.Trade_index : UrlConfig.Simulated_simulatedindex;

        Map<String, Object> map = new HashMap<>();
        map.put("symbol", symbol);
        post(mDataManager.fetchTradeIndex(url, map),
                new CommonSubscriber<TradeIndexBean>(mView, url) {
                    @Override
                    public void onNext(TradeIndexBean tradeIndexBean) {
                        super.onNext(tradeIndexBean);
                        if (tradeIndexBean != null && tradeIndexBean.getDepositList() != null) {
                            tradeIndexBean.getDepositList().get(0).setSelect(true);
                            String money = CalculationUtil.roundRuturnString(tradeIndexBean.getDepositList().get(0).getValue(), 2);
                            mView.setUserPayMoney(String.format("￥%s", money));
                        }
                        mView.showTradeIndex(tradeIndexBean);
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

    /**
     * 用户更改
     *
     * @param action                  动作 1：+100 0：不变（用于股票价格变化刷新数据）-1：-100
     * @param stockPrice              股票价格
     * @param tradeBuyStockMoney      定金
     * @param tradeMaxStopLossPrecent 股票交易止损系数
     */
    @Override
    public void changeStockPayNumber(int action, double stockPrice, double tradeBuyStockMoney, double tradeMaxStopLossPrecent) {
        int r = payStockNumber + action * 100;
        if (r <= maxStockNumber && r > 0) {
            payStockNumber = r;
        } else
            return;

        double tradeStopLossMoney = tradeBuyStockMoney * tradeMaxStopLossPrecent;

        mView.setStockNumberTextView(stockPrice);

        mView.setStockNumber(payStockNumber);

        double userTurnoverMoney = stockPrice * payStockNumber;
        mView.setUserTurnover(userTurnoverMoney);

        double stopPrecent = tradeStopLossMoney / userTurnoverMoney;
        if (Double.isInfinite(stopPrecent))
            stopPrecent = 0.f;

        mView.setStopLossTextView(tradeStopLossMoney, stopPrecent);

        mView.setStockStopLossPriceTextView(stockPrice * (1 - stopPrecent));
    }

    @Override
    public void createSimulatedOrder(int period, double tradeAllMoney, double tradeMoney, int stockNum,
                                     double stopLoss, double lossPrice, String symbol, String stockName) {
        if (stockNum <= 0) {
            ToastUtil.showMsg("请选择购买的股票数量");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("period", period);
        map.put("money", tradeAllMoney);
        map.put("deposit", tradeMoney);
        map.put("stock_num", stockNum);
        map.put("stop_loss", stopLoss);
        map.put("loss_price", lossPrice);
        map.put("symbol", symbol);
        map.put("sname", stockName);
        String url = UrlConfig.Simulated_simulatedcreateOrder;
        post(mDataManager.createSimulatedOrder(url, map),
                new CommonSubscriber<CreateSimulatedOrderBean>(mView, url) {
                    @Override
                    public void onNext(CreateSimulatedOrderBean data) {
                        super.onNext(data);
                        if (data != null)
                            mView.startToSimulatedTradingActivity();
                    }
                });

    }

    @Override
    public void createOrder(String symbol, String stockName, double deposit, int periodAutoStatus, int stockNum) {
        if (stockNum <= 0) {
            ToastUtil.showMsg("请选择购买的股票数量");
            return;
        }

        Map<String, Object> map = new HashMap<>();
        map.put("period_auto_status", periodAutoStatus);
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
                            mView.startToMainActivity();
                            mView.finishUI();
                        }
                        ToastUtil.showMsg("购买成功");
                    }
                });
    }

    @Override
    public void getMinuteStock(String stockCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("prod_code", stockCode);//股票代码
        params.put("fields", "last_px,avg_px,business_amount,business_balance");//股票
        String url = UrlConfig.Stock_trend;
        post(false, mDataManager.getStockMinute(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object data) {
                        mView.showMinute(GsonUtil.getInstance().toJson(data));
                    }
                }
        );
    }

    @Override
    public void loadContinueModeData(Context context, boolean isNotContinue) {
        //购买
        int status = PreferenceUtils.getInt(SPKeys.FILE_TRADE, SPKeys.TRADE_MODE_INT);
        int switchResource = 0;
        String switchMessage, continueMessage;
        if (status == 1) {
            //真实
            continueMessage = context.getResources().getString(
                    isNotContinue ?
                            R.string.real_stock_buy_continue_close_message : R.string.real_stock_buy_continue_open_message);
            switchMessage = "T+1";
            switchResource = isNotContinue ? R.mipmap.icon_deffer_off : R.mipmap.icon_deffer_on;
            mView.showContinueMode(true, switchResource, switchMessage, continueMessage);
        } else if (status == 2) {
            //虚拟
            switchMessage = "2个交易日";
            continueMessage = "下一个交易日 14：50分将发起卖出";
            mView.showContinueMode(false, switchResource, switchMessage, continueMessage);
        }
    }

}
