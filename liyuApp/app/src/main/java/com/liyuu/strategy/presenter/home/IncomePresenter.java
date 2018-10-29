package com.liyuu.strategy.presenter.home;

import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.home.IncomeContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.IncomeTypesBean;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class IncomePresenter extends RxPresenter<IncomeContract.View> implements IncomeContract.Presenter {

    private DataManager mDataManager;

    @Inject
    public IncomePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getIncomeType() {
//        post(false, mDataManager.fetchIncomeTypesInfo(UrlConfig.Stock_douniuType), new CommonSubscriber<List<IncomeTypesBean>>(mView) {
//            @Override
//            public void onNext(List<IncomeTypesBean> data) {
//                if (data == null || data.size() == 0)
//                    mView.finishUI();
//                mView.loadTabs(data);
//            }
//        });


        List<IncomeTypesBean> data = new ArrayList<>();
        IncomeTypesBean bean = new IncomeTypesBean();
        bean.setName("盈利榜");
        bean.setTypeId(1);
        data.add(bean);

        bean = new IncomeTypesBean();
        bean.setName("收益率");
        bean.setTypeId(2);
        data.add(bean);

        mView.loadTabs(data);
    }
}
