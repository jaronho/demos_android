package com.liyuu.strategy.ui.login;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.login.ForgetPwdContract;
import com.liyuu.strategy.presenter.login.ForgetPwdPresenter;
import com.liyuu.strategy.util.SimpleTextWatcher;
import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPwdActivity extends BaseActivity<ForgetPwdPresenter> implements ForgetPwdContract.View {
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.sms_send_view)
    TextView tvGetVerification;
    @BindView(R.id.tv_code_sure)
    TextView tvCodeSure;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;
    @BindView(R.id.edt_pwd_again)
    EditText edtRePwd;
    @BindView(R.id.tv_edit_sure)
    TextView tvEditSure;
    @BindView(R.id.layout_verify_code)
    View layoutVerify;
    @BindView(R.id.layout_reset_pwd)
    View layoutResetPwd;

    public static void start(Context context) {
        Intent intent = new Intent(context, ForgetPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle("手机验证");
        TextWatcher codeWatcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setCodeBtnEnabled();
            }
        };
        edtPhone.addTextChangedListener(codeWatcher);
        edtCode.addTextChangedListener(codeWatcher);
        TextWatcher pwdWatcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setPwdBtnEnabled();
            }
        };
        edtPwd.addTextChangedListener(pwdWatcher);
        edtRePwd.addTextChangedListener(pwdWatcher);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick({R.id.sms_send_view, R.id.tv_code_sure, R.id.tv_edit_sure})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.sms_send_view:
                mPresenter.getVerificationCode(edtPhone.getText().toString());
                break;
            case R.id.tv_code_sure:
                mPresenter.editPwd(
                        edtPhone.getText().toString(),
                        edtCode.getText().toString(),
                        null,
                        null);
                break;
            case R.id.tv_edit_sure:
                mPresenter.editPwd(
                        edtPhone.getText().toString(),
                        null,
                        edtPwd.getText().toString(),
                        edtRePwd.getText().toString());
                break;
        }
    }

    @Override
    public void setVerificationCodeText(boolean isEnable, String text) {
        tvGetVerification.setEnabled(isEnable);
        tvGetVerification.setText(text);
    }

    @Override
    public void setPwdView() {
        layoutVerify.setVisibility(View.GONE);
        layoutResetPwd.setVisibility(View.VISIBLE);
        setTitle("重置密码");
    }

    private void setCodeBtnEnabled() {
        if (TextUtils.isEmpty(edtPhone.getText()) || TextUtils.isEmpty(edtCode.getText()) ) {
            tvCodeSure.setEnabled(false);
            return;
        }
        tvCodeSure.setEnabled(true);
    }

    private void setPwdBtnEnabled() {
        if (TextUtils.isEmpty(edtPwd.getText()) || TextUtils.isEmpty(edtRePwd.getText()) ) {
            tvEditSure.setEnabled(false);
            return;
        }
        tvEditSure.setEnabled(true);
    }
}
