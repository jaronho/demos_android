package com.liyuu.strategy.presenter.transaction;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.BaseRefreshAdapter;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.transaction.BillListContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.BillListBean;
import com.liyuu.strategy.ui.transaction.adapter.BillListAdapter;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

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
