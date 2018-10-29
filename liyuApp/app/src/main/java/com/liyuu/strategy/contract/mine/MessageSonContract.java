package com.liyuu.strategy.contract.mine;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.MessageSonBean;
import com.liyuu.strategy.ui.mine.adapter.MessageSonAdapter;

import java.util.List;

public interface MessageSonContract {

    interface View extends BaseView {
        void loadMessage(List<MessageSonBean.ListBean> list);
    }

    interface Presenter extends BasePresenter<MessageSonContract.View> {
        void getAnnounceList(String type, MessageSonAdapter adapter);
    }
}
