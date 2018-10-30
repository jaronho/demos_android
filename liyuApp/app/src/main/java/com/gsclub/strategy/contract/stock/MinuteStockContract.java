package com.gsclub.strategy.contract.stock;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import com.github.mikephil.charting.charts.Chart;
import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.ui.stock.adapter.StockTradeRecyclerAdapter;
import com.gsclub.strategy.ui.stock.other.bean.DataParse;
import com.gsclub.strategy.ui.stock.other.bean.StockTradeBean;

import java.util.List;

public interface MinuteStockContract {

    interface View extends BaseView {
        void showMinute(String data);

        void showFiveLevelData(String data);
    }

    interface Presenter extends BasePresenter<View> {
        SparseArray<String> getDefaultXLabels();

        List<StockTradeBean> getDefaultList();

        String[] getMinutesCount();

        void getMinuteStock(String stockCode);

        void getStockReal(String stockSymbol, DataParse mData);

        StockTradeRecyclerAdapter initLevelAdapter(Activity activity, int color);

        void initRecyclerView(Activity activity, RecyclerView recyclerView, StockTradeRecyclerAdapter adatper);

        void setOnChartValueSelectedListener(Chart one, Chart two);
    }
}
