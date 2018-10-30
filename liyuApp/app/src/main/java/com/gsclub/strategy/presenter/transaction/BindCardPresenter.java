package com.gsclub.strategy.presenter.transaction;

import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.transaction.BindCardContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.UserBankBean;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class BindCardPresenter extends RxPresenter<BindCardContract.View> implements BindCardContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public BindCardPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getBankInfo() {
        String url = UrlConfig.UserBank_getUserBankInfo;
        post(mDataManager.getBankCardInfo(url), new CommonSubscriber<UserBankBean>(mView, url) {
            @Override
            public void onNext(UserBankBean bankInfoBean) {
                super.onNext(bankInfoBean);
                mView.loadData(bankInfoBean);
            }
        });
    }

    @Override
    public void bindCard(String real_name, String id_num, String bank_no, String bank_id, String binding_tel) {
        Map<String, Object> params = new HashMap<>();
        params.put("real_name", real_name);
        params.put("id_num", id_num);
        params.put("bank_no", bank_no);
//        params.put("bank_id", bank_id);
        params.put("binding_tel", binding_tel);
        String url = UrlConfig.UserBank_bindBank;
        post(mDataManager.bindBankCard(url, params), new CommonSubscriber<Object>(mView, url) {
            @Override
            public void onNext(Object o) {
                super.onNext(o);
                ToastUtil.showMsg("绑卡成功");
                PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_BANK_STATUS, "1");
                RxBus.get().send(RxBus.Code.USER_BANK_REFRESH);
                mView.finishUI();
            }
        });
    }
}
