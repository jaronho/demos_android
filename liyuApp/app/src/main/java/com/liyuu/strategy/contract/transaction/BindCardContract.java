package com.liyuu.strategy.contract.transaction;


import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.UserBankBean;

public interface BindCardContract {

    interface View extends BaseView {
        void loadData(UserBankBean data);
    }

    interface Presenter extends BasePresenter<View> {
        void getBankInfo();
        void bindCard(String real_name, String id_num, String bank_no, String bank_id, String binding_tel);
    }
}
