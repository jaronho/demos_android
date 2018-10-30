package com.gsclub.strategy.presenter;


import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.NullContract;
import com.gsclub.strategy.model.DataManager;

import javax.inject.Inject;


public class NullPresenter extends RxPresenter<NullContract.View> implements NullContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public NullPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }
}
