package com.liyuu.strategy.presenter.transaction;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.transaction.RecommendStocksContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.StockBean;
import com.liyuu.strategy.ui.transaction.adapter.RecommendStocksAdapter;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class RecommendStocksPresenter extends RxPresenter<RecommendStocksContract.View> implements RecommendStocksContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public RecommendStocksPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getStockList(RecommendStocksAdapter adapter) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", adapter.getPage());
        params.put("page_size", adapter.getPageSize());
        params.put("type", 2);// 1单条展示 2列表展示
        String url = UrlConfig.Trade_getStockList;
        post(mDataManager.getStockList(url, params), new CommonSubscriber<List<StockBean>>(mView, url) {
            @Override
            public void onNext(List<StockBean> bean) {
                super.onNext(bean);
                mView.loadData(bean);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                mView.doRefreshFinish();
            }

            @Override
            public void onError(Throwable e) {
                super.onError(e);
                mView.doRefreshFinish();
            }
        });
    }
}
