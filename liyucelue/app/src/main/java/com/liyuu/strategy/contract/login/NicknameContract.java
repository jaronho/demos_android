package com.liyuu.strategy.contract.login;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;
import com.liyuu.strategy.model.bean.UserIndexBean;

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
