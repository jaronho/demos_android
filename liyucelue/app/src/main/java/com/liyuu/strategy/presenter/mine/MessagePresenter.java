package com.liyuu.strategy.presenter.mine;

import com.liyuu.strategy.app.UrlConfig;
import com.liyuu.strategy.base.RxPresenter;
import com.liyuu.strategy.contract.mine.MessageContract;
import com.liyuu.strategy.model.DataManager;
import com.liyuu.strategy.model.bean.MessageIndexBean;
import com.liyuu.strategy.widget.rx.CommonSubscriber;

import java.util.List;

import javax.inject.Inject;

public class MessagePresenter extends RxPresenter<MessageContract.View> implements MessageContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public MessagePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getMessageIndex() {
        String url = UrlConfig.User_announceIndex;
        post(false, mDataManager.fetchAnnounceIndexInfo(url),
                new CommonSubscriber<List<MessageIndexBean>>(mView, url) {
                    @Override
                    public void onNext(List<MessageIndexBean> messageIndexBeans) {
                        if (messageIndexBeans == null || messageIndexBeans.size() == 0)
                            mView.finishUI();
                        mView.loadMessageTypes(messageIndexBeans);
                    }
                });
    }
}