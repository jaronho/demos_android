package com.gsclub.strategy.contract.login;

import com.gsclub.strategy.base.BasePresenter;
import com.gsclub.strategy.base.BaseView;
import com.gsclub.strategy.model.bean.UserIndexBean;

/**
 * Created by hlw on 2018/5/22.
 * 昵称
 */

public interface NicknameContract {

    interface View extends BaseView {
        void goToMainAct(UserIndexBean bean);
    }

    interface Presenter extends BasePresenter<NicknameContract.View> {
        void commitNickname(String nickname);
    }
}
