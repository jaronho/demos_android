package com.gsclub.strategy.contract.transaction;


import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.UserBankBean;

public interface BindCardContract {

    interface View extends BaseView {
        void loadData(UserBankBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getBankInfo();
        void bindCard(String real_name, String id_num, String bank_no, String bank_id, String binding_tel);
    }
}
