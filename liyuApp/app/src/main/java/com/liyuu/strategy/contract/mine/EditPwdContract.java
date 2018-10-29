package com.liyuu.strategy.contract.mine;

import com.liyuu.strategy.base.BasePresenter;
import com.liyuu.strategy.base.BaseView;

/**
 * 修改密码操作接口
 */

public interface EditPwdContract {

    interface View extends BaseView {
        //验证完手机号后，进入重置密码环节
        void setPwdView();
    }

    interface Presenter extends BasePresenter<EditPwdContract.View> {
        //修改密码
        void editPwd(String old_pwd, String new_pwd, String re_pwd);
    }
}
