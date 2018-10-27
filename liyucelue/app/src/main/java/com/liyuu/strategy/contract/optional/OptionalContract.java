package com.liyuu.strategy.contract.optional;

import android.graphics.drawable.Drawable;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.CustomSelectStockBean;
import com.liyuu.strategy.model.bean.OptionalTopStockBean;
import com.liyuu.strategy.ui.optional.adapter.StockSelectRecyclerAdatper;

import java.util.List;

public interface OptionalContract {
    interface View extends BaseView {
        void setTopStocks(List<OptionalTopStockBean> list);

        //显示用户添加到本地的自选股票
        void showUserLocalOptionalStockDatas(List<CustomSelectStockBean> datas);

        //更改自选股排序方式后的icon
        void changeSortIcon(int resource);
    }

    interface Presenter extends BasePresenter<View> {
        //获取生成的顶部三大板块空数据
        List<OptionalTopStockBean> getThreeStockPlateDefaultData();

        //获取顶部三大板块数据
        void getThreeStockPlateData();

        //获取用户本地自选数据本地
        void getUserLocalOptionalStockDatas();

        //获取股票最新实时数据并更新视图
        void getStocksRealDataAndRefreshList(StockSelectRecyclerAdatper adatper);

        //更改自选股的排序方式
        void changeSortMode(StockSelectRecyclerAdatper adatper);
    }
}
