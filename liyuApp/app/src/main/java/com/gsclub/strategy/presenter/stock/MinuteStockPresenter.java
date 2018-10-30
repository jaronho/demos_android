package com.gsclub.strategy.presenter.stock;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.stock.MinuteStockContract;
import com.gsclub.strategy.http.GsonUtil;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.ui.stock.adapter.StockTradeRecyclerAdapter;
import com.gsclub.strategy.ui.stock.other.bean.DataParse;
import com.gsclub.strategy.ui.stock.other.bean.StockTradeBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class MinuteStockPresenter extends RxPresenter<MinuteStockContract.View> implements MinuteStockContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public MinuteStockPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public SparseArray<String> getDefaultXLabels() {
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "");
        xLabels.put(60, "");
        xLabels.put(121, "");
        xLabels.put(182, "");
        xLabels.put(241, "");
        return xLabels;
    }

    @Override
    public List<StockTradeBean> getDefaultList() {
        List<StockTradeBean> list = new ArrayList<StockTradeBean>();
        for (int i = 0; i < 5; i++) {
            StockTradeBean bean = new StockTradeBean();
            bean.setTradeLevel("--");
            bean.setPrice(0.f);
            bean.setNumber(0.f);
            list.add(bean);
        }
        return list;
    }

    @Override
    public String[] getMinutesCount() {
        //分时图上限为242条数据
        return new String[242];
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
    public void getStockReal(final String stockSymbol, final DataParse mData) {
        Map<String, Object> params = new HashMap<>();
        params.put("en_prod_code", stockSymbol);
        params.put("fields", "bid_grp,offer_grp,preclose_px,px_change");
        String url = UrlConfig.stock_real;
        post(false, mDataManager.getStockMinute(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object data) {
                        mView.showFiveLevelData(GsonUtil.getInstance().toJson(data));
                    }
                }
        );
    }

    @Override
    public StockTradeRecyclerAdapter initLevelAdapter(Activity activity, int color) {
        StockTradeRecyclerAdapter adatper = new StockTradeRecyclerAdapter(activity);
        adatper.setData(new ArrayList<StockTradeBean>());
        adatper.setPriceColor(color);
        return adatper;
    }

    @Override
    public void initRecyclerView(Activity activity, RecyclerView recyclerView, StockTradeRecyclerAdapter adatper) {
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        recyclerView.setAdapter(adatper);

    }

    @Override
    public void setOnChartValueSelectedListener(Chart one, final Chart two) {
        one.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                two.highlightValue(new Highlight(h.getXIndex(), 0));
            }

            @Override
            public void onNothingSelected() {
                two.highlightValue(null);
            }
        });
    }
}
