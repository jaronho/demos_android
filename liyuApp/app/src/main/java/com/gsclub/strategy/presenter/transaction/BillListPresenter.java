package com.gsclub.strategy.presenter.transaction;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.BaseRefreshAdapter;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.transaction.BillListContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.BillListBean;
import com.gsclub.strategy.ui.transaction.adapter.BillListAdapter;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class BillListPresenter extends RxPresenter<BillListContract.View> implements BillListContract.Presenter {

    private DataManager mDataManager;
    private int page = 1;

    @Inject
    BillListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getList(int type, BillListAdapter adapter) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", adapter.getPage());
        map.put("page_size", adapter.getPageSize());
        map.put("type", type);
        String url = UrlConfig.Trade_capitalDetail;
        post(false, mDataManager.fetchBillsInfo(url, map),
                new CommonSubscriber<BillListBean>(mView, url) {
                    @Override
                    public void onNext(BillListBean data) {
                        super.onNext(data);
                        mView.loadData(data.getList());
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        mView.doRefreshFinish();
                    }
                });
    }
}
