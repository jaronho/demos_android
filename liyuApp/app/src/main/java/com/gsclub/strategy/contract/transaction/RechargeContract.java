package com.gsclub.strategy.contract.transaction;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.PayInfoBean;
import com.gsclub.strategy.model.bean.RechargeIndexBean;
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
