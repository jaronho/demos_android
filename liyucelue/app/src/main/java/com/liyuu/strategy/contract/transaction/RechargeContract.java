package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.PayInfoBean;
import com.liyuu.strategy.model.bean.RechargeIndexBean;
import com.yintong.secure.model.PayInfo;

public interface RechargeContract {

    interface View extends BaseView {
        void loadData(RechargeIndexBean data);
        void recharge(PayInfoBean data);
        void doSuccess();
    }

    interface Presenter extends BasePresenter<View> {
        void getRechargeIndex();
        void getRechargeOrder(String money);
        void checkRechargeOrder(String trade_no);
    }
}
