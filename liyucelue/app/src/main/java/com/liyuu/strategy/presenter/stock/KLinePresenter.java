package com.liyuu.strategy.presenter.stock;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.stock.KLineStockContract;
import com.liyuu.strategy.http.GsonUtil;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.widget.rx.CommonSubscriber;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class KLinePresenter extends RxPresenter<KLineStockContract.View> implements KLineStockContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public KLinePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void queryKLine(String symbol, int klineType, int dataCount) {
        Map<String, Object> params = new HashMap<>();
        params.put("prod_code", symbol);
        params.put("candle_period", String.valueOf(klineType));
        params.put("data_count", String.valueOf(dataCount));
        params.put("get_type", "offset");
        String url = UrlConfig.stock_Kline;
        post(false, mDataManager.getStockKLine(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object o) {
                        if (o != null)
                            mView.loadKLine(GsonUtil.getInstance().toJson(o));
                    }
                });
    }
}