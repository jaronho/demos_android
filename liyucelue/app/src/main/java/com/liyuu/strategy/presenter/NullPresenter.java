package com.liyuu.strategy.presenter;


import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.NullContract;
import com.liyuu.strategy.model.DataManager;

import javax.inject.Inject;


public class NullPresenter extends RxPresenter<NullContract.View> implements NullContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public NullPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }
}
