package com.liyuu.strategy.presenter.optional;

import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.optional.StockSelectEditContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.CustomSelectStockBean;
import com.liyuu.strategy.ui.optional.adapter.StockSelectEditRecyclerAdatper;

import java.util.List;

import javax.inject.Inject;

import io.realm.Sort;

public class StockSelectEditPresenter extends RxPresenter<StockSelectEditContract.View>
        implements StockSelectEditContract.Presenter {

    private DataManager mDataManager;

    @Inject
    StockSelectEditPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getUserLocalOptionalStockDatas() {
        List<CustomSelectStockBean> list =
                (List<CustomSelectStockBean>) mDataManager.dbQueryList(CustomSelectStockBean.class, "addTime", Sort.ASCENDING);
        mView.showUserLocalOptionalStockDatas(list);
    }

    @Override
    public void reSortUserLocalOptionalStockDatas(StockSelectEditRecyclerAdatper adapter) {
        if (adapter == null)
            return;
        mDataManager.dbDeleteTable(CustomSelectStockBean.class);
        for (int i = 0, size = adapter.getItemCount(); i < size; i++) {
            CustomSelectStockBean bean = adapter.getData().get(i);
            bean.setAddTime(System.currentTimeMillis() + i);
            mDataManager.dbInsertData(bean);
        }
        adapter.notifyDataSetChanged();
        RxBus.get().send(RxBus.Code.MINE_OPTOPNAL_STOCK_DATA_CHANGED);
    }

    @Override
    public void deleteSomeUserLocalOptionalStockDatas(StockSelectEditRecyclerAdatper adapter) {
        if (adapter == null)
            return;
        for (int i = 0, size = adapter.getItemCount(); i < size; i++) {
            CustomSelectStockBean bean = adapter.getData().get(i);
            if (!bean.isChecked()) continue;
            mDataManager.dbDeleteData(CustomSelectStockBean.class, "symbol", bean.getSymbol());
        }
        RxBus.get().send(RxBus.Code.MINE_OPTOPNAL_STOCK_DATA_CHANGED);
        getUserLocalOptionalStockDatas();
    }
}
