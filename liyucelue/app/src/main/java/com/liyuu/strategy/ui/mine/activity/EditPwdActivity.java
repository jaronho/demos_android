package com.liyuu.strategy.ui.mine.activity;

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
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.mine.EditPwdContract;
import com.liyuu.strategy.presenter.mine.EditPwdPresenter;
import com.liyuu.strategy.ui.login.ForgetPwdActivity;
import com.liyuu.strategy.util.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPwdActivity extends BaseActivity<EditPwdPresenter> implements EditPwdContract.View {
    @BindView(R.id.edt_old_pwd)
    EditText edtOldPwd;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;
    @BindView(R.id.edt_pwd_again)
    EditText edtRePwd;
    @BindView(R.id.tv_edit_sure)
    TextView tvEditSure;

    public static void start(Context context) {
        Intent intent = new Intent(context, EditPwdActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_pwd;
    }

    @Override
    protected void initEventAndData() {

    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle("修改密码");
        TextWatcher pwdWatcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setPwdBtnEnabled();
            }
        };
        edtOldPwd.addTextChangedListener(pwdWatcher);
        edtPwd.addTextChangedListener(pwdWatcher);
        edtRePwd.addTextChangedListener(pwdWatcher);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick({R.id.tv_edit_sure, R.id.tv_forget_pwd})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_edit_sure:
                mPresenter.editPwd(
                        edtOldPwd.getText().toString(),
                        edtPwd.getText().toString(),
                        edtRePwd.getText().toString());
                break;
            case R.id.tv_forget_pwd:
                ForgetPwdActivity.start(this);
                break;
        }
    }

    @Override
    public void setPwdView() {
    }

    private void setPwdBtnEnabled() {
        if (TextUtils.isEmpty(edtOldPwd.getText()) || TextUtils.isEmpty(edtPwd.getText()) || TextUtils.isEmpty(edtRePwd.getText()) ) {
            tvEditSure.setEnabled(false);
            return;
        }
        tvEditSure.setEnabled(true);
    }

    @Override
    public void onEventAccept(int code, Object object) {
        super.onEventAccept(code, object);
        switch (code) {
            case RxBus.Code.FORGET_PWD_RESET_SUCCESS:
                finishUI();
                break;
        }
    }
}
