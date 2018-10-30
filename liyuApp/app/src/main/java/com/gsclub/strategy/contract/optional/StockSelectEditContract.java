package com.gsclub.strategy.contract.optional;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.CustomSelectStockBean;
import com.gsclub.strategy.ui.optional.adapter.StockSelectEditRecyclerAdatper;

import java.util.List;

public interface StockSelectEditContract {

    interface View extends BaseView {
        void showUserLocalOptionalStockDatas(List<CustomSelectStockBean> datas);
    }

    interface Presenter extends BasePresenter<View> {

        //获取本地自选股列表
        void getUserLocalOptionalStockDatas();

        //对当前的自选股进行重新排序
        void reSortUserLocalOptionalStockDatas(StockSelectEditRecyclerAdatper adapter);

        //删除部分自选股
        void deleteSomeUserLocalOptionalStockDatas(StockSelectEditRecyclerAdatper adapter);
    }
}
