package com.liyuu.strategy.contract.mine;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.MessageIndexBean;

import java.util.List;

public interface MessageContract {

    interface View extends BaseView {
        void loadMessageTypes(List<MessageIndexBean> datas);
    }

    interface Presenter extends BasePresenter<MessageContract.View> {
        void getMessageIndex();
    }
}
