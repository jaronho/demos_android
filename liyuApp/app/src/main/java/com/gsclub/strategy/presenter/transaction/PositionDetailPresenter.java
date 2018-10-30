package com.gsclub.strategy.presenter.transaction;


import android.text.TextUtils;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.transaction.PositionDetailContract;
import com.gsclub.strategy.http.GsonUtil;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.PositionDetailBean;
import com.gsclub.strategy.util.LogUtil;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class PositionDetailPresenter extends RxPresenter<PositionDetailContract.View>
        implements PositionDetailContract.Presenter {
    private DataManager mDataManager;

    @Inject
    PositionDetailPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getPositionDetail(String oid) {
        Map<String, Object> params = new HashMap<>();
        params.put("oid", oid);
        String url = UrlConfig.Trade_HoldingContent;
        post(mDataManager.fetchPositionDetail(url, params),
                new CommonSubscriber<PositionDetailBean>(mView, url) {
                    @Override
                    public void onNext(PositionDetailBean o) {
                        super.onNext(o);
                        if (o != null)
                            mView.showPositionDetail(o);
                    }
                });
    }

    @Override
    public void changeStockStopLossPrice(String oid, double changeStopLossPrice) {
        Map<String, Object> params = new HashMap<>();
        params.put("oid", oid);
        params.put("loss_price", changeStopLossPrice);
        String url = UrlConfig.Trade_changeStopLoss;
        post(mDataManager.fetchData(url, params),
                new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        if (o != null)
                            mView.changeStockStopLossPriceSuccess(GsonUtil.toString(o));
                    }
                });
    }

    @Override
    public void changeStockAutoDeferred(String oid, boolean isDeferred) {
        Map<String, Object> params = new HashMap<>();
        params.put("oid", oid);
        //递延状态 1递延 2不递延
        params.put("status", isDeferred ? "1" : "2");
        String url = UrlConfig.Trade_changeAutoDefer;
        post(mDataManager.fetchData(url, params),
                new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object o) {
                        super.onNext(o);
                        if (o == null)
                            return;
                        try {
                            mView.changeStockAutoDeferredSuccess(GsonUtil.toString(o));
                            String result = GsonUtil.toString(o);
                            JSONObject object = new JSONObject(result);
                            String cannot_defer_desc = object.optString("cannot_defer_desc");
//                            String is_defer = object.optString("is_defer");//1可递延  2不可递延
                            if (!TextUtils.isEmpty(cannot_defer_desc))
                                ToastUtil.showMsg(cannot_defer_desc);
                        } catch (JSONException e) {
                            LogUtil.e(e.getMessage());
                        }

                    }
                });
    }
}
