package com.gsclub.strategy.presenter.login;

import android.text.TextUtils;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.login.NicknameContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class NicknamePresenter extends RxPresenter<NicknameContract.View> implements NicknameContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public NicknamePresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void commitNickname(final String nickname) {
        if (TextUtils.isEmpty(nickname)) {
            ToastUtil.showMsg("请输入昵称");
            return;
        }
        if(nickname.length() < 2) {
            ToastUtil.showLongMsg(R.string.nickname_hint);
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("content", nickname);
        params.put("type", "2");//编辑类型: 1头像 2昵称
        String url = UrlConfig.User_editUserInfo;
        post(mDataManager.editUserInfo(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object data) {
                        ToastUtil.showMsg("昵称设置成功");
                        PreferenceUtils.put(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME, nickname);
                        RxBus.get().send(RxBus.Code.EDIT_NICKNAME_SUCCESS);
                        mView.finishUI();
                    }
                });
//        LogUtil.i(data + "  " + nickname);
    }
}
