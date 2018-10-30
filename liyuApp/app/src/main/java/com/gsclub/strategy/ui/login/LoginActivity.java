package com.gsclub.strategy.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.gsclub.strategy.R;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.component.RxBus;
import com.gsclub.strategy.contract.login.LoginContract;
import com.gsclub.strategy.presenter.login.LoginPresenter;
import com.gsclub.strategy.util.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */

public class LoginActivity extends BaseActivity<LoginPresenter> implements LoginContract.View {
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_verification)
    EditText edtVerification;
    @BindView(R.id.tv_Login)
    TextView tvLogin;
    @BindView(R.id.img_see)
    ImageView imgSee;

    private boolean isEditPwdCanSee = false;//密码默认不可见

    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_login;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(getString(R.string.login));

        TextWatcher watcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setBtnEnabled();
            }
        };
        edtPhone.addTextChangedListener(watcher);
        edtVerification.addTextChangedListener(watcher);
        setPwdEdit(isEditPwdCanSee);
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    @OnClick({R.id.tv_Login, R.id.tv_forget_pwd, R.id.img_see, R.id.tv_register, R.id.iv_close})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_Login:
                mPresenter.login(edtPhone.getText().toString(), edtVerification.getText().toString());
                break;
            case R.id.tv_forget_pwd:
                ForgetPwdActivity.start(this);
                break;
            case R.id.img_see:
                setPwdEdit(isEditPwdCanSee = !isEditPwdCanSee);
                break;
            case R.id.tv_register:
                RegisterActivity.start(this);
                break;
            case R.id.iv_close:
                finishUI();
                break;
        }
    }

    @Override
    public void intentToNickAct() {
        NicknameActivity.start(this);
    }

    private void setPwdEdit(boolean isCanSee) {
        int position = edtVerification.getSelectionEnd();
        if (isCanSee) {
            imgSee.setImageDrawable(getResources().getDrawable(R.mipmap.icon_edt_can_see));
            //如果选中，显示密码
            edtVerification.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            imgSee.setImageDrawable(getResources().getDrawable(R.mipmap.icon_edt_not_see));
            //否则隐藏密码
            edtVerification.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        edtVerification.setSelection(position);//将光标移至文字之前选中的位置
    }

    private void setBtnEnabled() {
        if (TextUtils.isEmpty(edtPhone.getText()) || TextUtils.isEmpty(edtVerification.getText()) ) {
            tvLogin.setEnabled(false);
            return;
        }
        tvLogin.setEnabled(true);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN:
                finishUI();
                break;
        }
    }
}
