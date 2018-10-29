package com.liyuu.strategy.presenter.main;

import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.main.MineContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.util.CommonUtil;

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
