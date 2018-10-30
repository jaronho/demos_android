package com.gsclub.strategy.ui.mine.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gsclub.strategy.R;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.mine.SettingContract;
import com.gsclub.strategy.model.bean.AppUpdateBean;
import com.gsclub.strategy.presenter.mine.SettingPresenter;
import com.gsclub.strategy.ui.dialog.AlertDialog;
import com.gsclub.strategy.ui.dialog.BaseDialog;
import com.gsclub.strategy.ui.dialog.BaseDialogImpl;
import com.gsclub.strategy.ui.dialog.NormalDialog;
import com.gsclub.strategy.ui.dialog.UpdateAppDialog;
import com.gsclub.strategy.util.PreferenceUtils;
import com.gsclub.strategy.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置界面
 */
public class SettingActivity extends BaseActivity<SettingPresenter> implements SettingContract.View {
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.tv_new)
    TextView tvNew;

    public static void start(Context context) {
        context.startActivity(new Intent(context, SettingActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(getString(R.string.setting));
        showLogoutBtn();
    }

    @Override
    protected void initEventAndData() {
        mPresenter.checkUpdata(true);
    }

    @OnClick({R.id.tv_about_us, R.id.tv_feedback, R.id.tv_logout, R.id.tv_version_update})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_about_us:
                AboutUsActivity.start(this);
                break;
            case R.id.tv_feedback:
                FeedbackActivity.start(this);
                break;
            case R.id.tv_logout:
                showLogOutDialog();
                break;
            case R.id.tv_version_update:
                mPresenter.checkUpdata(false);
                break;
        }
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_LOGIN_OUT:
                showLogoutBtn();
                break;
        }
    }

    private void showLogOutDialog() {
        NormalDialog dialog = NormalDialog.newInstance("温馨提示", "是否退出登录？", true);
        dialog.setBaseImpl(new BaseDialogImpl() {
            @Override
            public void dialogCancle(String dialogName, BaseDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void dialogSure(String dialogName, BaseDialog dialog) {
                dialog.dismiss();
                mPresenter.logout();
            }
        });
//        dialog.setTextView("是否退出登录？");
//        dialog.setSureButton("退出", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                mPresenter.logout();
//            }
//        });
//        dialog.setCancelButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
        dialog.show(getSupportFragmentManager());
    }

    @Override
    public void logoutSuccess() {
//        mPresenter.removeCookiesManage(this);
        UserInfoUtil.logout();
        finishUI();
    }

    @Override
    public void showUpdate(boolean isFirst, AppUpdateBean bean) {
        if (bean == null) return;
        if (!isFirst) {
            UpdateAppDialog.update(bean);
        }
        boolean newVersion = PreferenceUtils.getBoolean(SPKeys.FILE_COMMON, SPKeys.COMMON_NEW_VERSION);
        tvNew.setVisibility(newVersion ? View.VISIBLE : View.GONE);
    }

    private void showLogoutBtn() {
        tvLogout.setVisibility((UserInfoUtil.isLogin()) ? View.VISIBLE : View.GONE);
    }
}
