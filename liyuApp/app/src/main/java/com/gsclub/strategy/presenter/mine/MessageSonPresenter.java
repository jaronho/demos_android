package com.gsclub.strategy.presenter.mine;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.mine.MessageSonContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.model.bean.MessageSonBean;
import com.gsclub.strategy.ui.mine.adapter.MessageSonAdapter;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class MessageSonPresenter extends RxPresenter<MessageSonContract.View> implements MessageSonContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public MessageSonPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getAnnounceList(String type, MessageSonAdapter adapter) {
        Map<String, Object> params = new HashMap<>();
        params.put("page", String.valueOf(adapter.getPage()));
        params.put("page_size", String.valueOf(adapter.getPageSize()));
        params.put("type", type);
        String url = UrlConfig.User_getAnnounceList;
        post(mDataManager.fetchAnnounceListInfo(url, params),
                new CommonSubscriber<MessageSonBean>(mView, url) {
                    @Override
                    public void onNext(MessageSonBean data) {
                        mView.loadMessage(data.getList());
                    }
                });
    }
}
