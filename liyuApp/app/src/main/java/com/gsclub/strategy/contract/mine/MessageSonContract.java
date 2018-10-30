package com.gsclub.strategy.contract.mine;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.MessageSonBean;
import com.gsclub.strategy.ui.mine.adapter.MessageSonAdapter;

import java.util.List;

public interface MessageSonContract {

    interface View extends BaseView {
        void loadMessage(List<MessageSonBean.ListBean> list);
    }

    interface Presenter extends BasePresenter<MessageSonContract.View> {
        void getAnnounceList(String type, MessageSonAdapter adapter);
    }
}
