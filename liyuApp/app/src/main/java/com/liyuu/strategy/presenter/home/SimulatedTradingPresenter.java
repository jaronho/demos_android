package com.liyuu.strategy.presenter.home;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.home.SimulatedTradingContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.SimulatedInfoBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import javax.inject.Inject;

public class SimulatedTradingPresenter extends RxPresenter<SimulatedTradingContract.View> implements SimulatedTradingContract.Presenter {

    private DataManager mDataManager;

    @Inject
    SimulatedTradingPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getUserSimulatedInfo() {
        String url = UrlConfig.Simulated_simulatedaccount;
        post(false, mDataManager.fetchUserSimulatedInfo(url),
                new CommonSubscriber<SimulatedInfoBean>(mView, url) {
                    @Override
                    public void onNext(SimulatedInfoBean o) {
                        super.onNext(o);
                        if (o != null)
                            mView.showUserSimulatedInfo(o);
                    }
                });
    }
}
