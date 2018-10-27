package com.liyuu.strategy.presenter.transaction;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.transaction.BankListContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.BankBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

public class BankListPresenter extends RxPresenter<BankListContract.View> implements BankListContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public BankListPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getBankList() {
        String url = UrlConfig.UserBank_bankList;
        post(mDataManager.getBankList(url), new CommonSubscriber<List<BankBean>>(mView, url) {
            @Override
            public void onNext(List<BankBean> data) {
                super.onNext(data);
                mView.loadData(data);
            }
        });
    }
}
