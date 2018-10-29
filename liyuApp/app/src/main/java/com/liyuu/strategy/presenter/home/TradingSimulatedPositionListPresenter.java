package com.liyuu.strategy.presenter.home;

import android.text.TextUtils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.liyuu.strategy.app.StockField;
import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.home.TradingSimulatedPositionListContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.SaleStockOrderBean;
import com.liyuu.strategy.model.bean.SimulatedTradingPositionBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class TradingSimulatedPositionListPresenter extends RxPresenter<TradingSimulatedPositionListContract.View>
        implements TradingSimulatedPositionListContract.Presenter {

    private final static int PAGE_SIZE = 10;
    private DataManager mDataManager;
    private int page = 1;

    @Inject
    TradingSimulatedPositionListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getTradingSimulatedPositionData(final boolean isRefresh) {
        if (isRefresh)
            page = 1;

        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("page_size", PAGE_SIZE);

        post(mDataManager.getSimulatedTradingPositionData(UrlConfig.Simulated_simulatedholdingList, map),
                new CommonSubscriber<SimulatedTradingPositionBean>(mView, UrlConfig.Simulated_simulatedholdingList) {
                    @Override
                    public void onNext(SimulatedTradingPositionBean data) {
                        super.onNext(data);
                        mView.stopRefreshLayoutAnim();

                        boolean isNullData = (data == null || data.getList() == null || data.getList().size() == 0);

                        //下拉刷新为空显示空界面
                        if (isRefresh && isNullData) {
                            mView.showEmptyPage();
                            return;
                        }

                        //再次获取数据为空关闭上拉加载更多
                        if (isNullData) {
                            mView.enableLoadMore(false);
                            return;
                        }

                        boolean isEnableLoadMore = data.getList().size() == PAGE_SIZE;
                        mView.enableLoadMore(isEnableLoadMore);

                        mView.showTradingSimulatedPositionList(isRefresh, data.getList());
                        page++;
                    }
                });
    }

    @Override
    public void sellOrder(String oid, String symbol) {
        Map<String, Object> map = new HashMap<>();
        map.put("oid", oid);
        map.put("symbol", symbol);
        post(mDataManager.saleStockOrder(UrlConfig.Simulated_simulatedSale, map),
                new CommonSubscriber<SaleStockOrderBean>(mView, UrlConfig.Simulated_simulatedSale) {
                    @Override
                    public void onNext(SaleStockOrderBean data) {
                        super.onNext(data);
                        if (data != null)
                            mView.saleSuccess();
                    }
                });
    }

    @Override
    public void refreshStockLastPrice(final String symbol) {
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
                        double stockPrice = GsonUtil.arrayGetDouble(array, position);

//                        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE);
//                        String price = GsonUtil.arrayGetString(array, position);

//                        position = GsonUtil.getStockFieldsPosition(snapshot, StockField.PX_CHANGE_RATE);
//                        String pricePercent = GsonUtil.arrayGetString(array, position);
//                        mView.showStockRealData(stockPrice, price, pricePercent);

                        mView.refreshStockLastPrice(symbol, stockPrice);
                    }
                });
    }
}
