package com.gsclub.strategy.contract.optional;

import android.content.Context;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.ui.stock.other.bean.SelectStockBean;

import java.util.List;

public interface SelectStockContract {

    interface View extends BaseView {
        void showDefaultData(List<SelectStockBean> one, List<SelectStockBean> two);

        void showStockRealData(String data);

        void showAddStockView(boolean isHadAdd);
    }

    interface Presenter extends BasePresenter<View> {
        void getDefaultData(Context context);

        void getStockReal(String symbol);

        //获取k线图类型
        int getKlineType(int position);

        //查询该股票是否添加为自选股
        void queryThisStockIsSelect(String stockSymbol);

        //添加/删除自选股
        void changeAddStock(String symbol, String stockName);
    }
}
