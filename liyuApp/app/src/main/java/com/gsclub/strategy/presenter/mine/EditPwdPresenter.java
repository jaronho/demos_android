package com.gsclub.strategy.presenter.mine;

import com.gsclub.strategy.app.UrlConfig;
import com.gsclub.strategy.base.RxPresenter;
import com.gsclub.strategy.contract.mine.EditPwdContract;
import com.gsclub.strategy.model.DataManager;
import com.gsclub.strategy.util.CommonUtil;
import com.gsclub.strategy.util.LogUtil;
import com.gsclub.strategy.util.ToastUtil;
import com.gsclub.strategy.widget.rx.CommonSubscriber;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import io.reactivex.functions.Function;

public class EditPwdPresenter extends RxPresenter<EditPwdContract.View> implements EditPwdContract.Presenter {
    private DataManager mDataManager;

    @Inject
    public EditPwdPresenter(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void editPwd(String old_pwd, String new_pwd, String re_pwd) {
        if(!CommonUtil.isCorrectPwd(old_pwd)) return;
        if(!CommonUtil.isCorrectPwd(new_pwd)) return;
        if(!CommonUtil.isCorrectPwd(re_pwd)) return;
        if (!new_pwd.equals(re_pwd)) {
            ToastUtil.showMsg("两次密码不一致");
            return;
        }
        Map<String, Object> params = new HashMap<>();
        params.put("old_pwd", old_pwd);
        params.put("new_pwd", new_pwd);
        params.put("re_pwd", re_pwd);
        String url = UrlConfig.User_chgLoginPwd;
        post(mDataManager.editPassword(url, params), new CommonSubscriber<Object>(mView, url) {
                    @Override
                    public void onNext(Object bean) {
                        LogUtil.i(bean.toString());
                        ToastUtil.showMsg("密码修改成功");//重置密码
                        mView.finishUI();
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
    }
}
