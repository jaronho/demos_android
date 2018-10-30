package com.gsclub.strategy.presenter.main;

import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.main.MineContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.util.CommonUtil;

import javax.inject.Inject;

public class MinePresenter extends RxPresenter<MineContract.View>
        implements MineContract.Presenter {
    private DataManager mDataManager;

    @Inject
    MinePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void attachView(MineContract.View view) {
        super.attachView(view);
    }

    @Override
    public void checkStatus() {
        boolean isChecking = CommonUtil.isReview();
        mView.checkViewChange(isChecking);
    }
}
