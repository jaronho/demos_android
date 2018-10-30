package com.gsclub.strategy.contract.mine;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.MessageIndexBean;

import java.util.List;

public interface MessageContract {

    interface View extends BaseView {
        void loadMessageTypes(List<MessageIndexBean> datas);
    }

    interface Presenter extends BasePresenter<MessageContract.View> {
        void getMessageIndex();
    }
}
