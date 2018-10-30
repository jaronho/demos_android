package com.gsclub.strategy.presenter.home;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.home.SimulatedTradingContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.SimulatedInfoBean;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

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
