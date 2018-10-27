package com.liyuu.strategy.presenter.stock;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.stock.SearchStockContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.SearchStockBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Sort;


public class SearchStockPresenter extends RxPresenter<SearchStockContract.View> implements SearchStockContract.Presenter {

    private DataManager mDataManager;

    @Inject
    SearchStockPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void searchStocks(String symbol) {
        Map<String, Object> params = new HashMap<>();
        params.put("keyword", symbol);
        params.put("page", "1");
        params.put("type", "1");
        String url = UrlConfig.index_indexsearch;
        post(false, mDataManager.fetchSearchStockList(url, params), new CommonSubscriber<List<SearchStockBean>>(mView, url) {
                    @Override
                    public void onNext(List<SearchStockBean> searchStockBeans) {
                        mView.setStocksData(searchStockBeans);
                    }
                }
        );
    }

    @Override
    @SuppressWarnings("unchecked")
    public void getLocalSearchStockHistory() {
        List<SearchStockBean> list = (List<SearchStockBean>) mDataManager.dbQueryList(SearchStockBean.class, "addTime", Sort.DESCENDING);
        if (list != null && list.size() > 0) {
            mView.showSearchStockHistory(list);
        }
    }

    @Override
    public void clearLocalSearchHistory() {
        mDataManager.dbDeleteTable(SearchStockBean.class);
        mView.hideRecyclerView();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void insertData(SearchStockBean bean) {
        bean.setAddTime(System.currentTimeMillis());
        List<SearchStockBean> list = (List<SearchStockBean>) mDataManager.dbQueryList(SearchStockBean.class, "addTime", Sort.ASCENDING);
        mDataManager.dbDeleteData(SearchStockBean.class, "symbol", bean.getSymbol());//删除symbol相同的数据
        mDataManager.dbInsertData(bean);
        //股票浏览历史最多保持20条，超过的自动删除
        if (list != null && list.size() > 20) {
            for (int delete = list.size() - 21; delete >= 0; delete--)
                mDataManager.dbDeleteData(SearchStockBean.class, "addTime", list.get(delete).getAddTime());
        }
    }
}
