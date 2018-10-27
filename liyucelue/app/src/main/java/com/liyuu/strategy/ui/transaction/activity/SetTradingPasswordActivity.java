package com.liyuu.strategy.ui.transaction.activity;

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

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.transaction.SetTradingPasswordContract;
import com.liyuu.strategy.contract.transaction.WithdrawContract;
import com.liyuu.strategy.presenter.transaction.SetTradingPasswordPresenter;
import com.liyuu.strategy.presenter.transaction.WithdrawPresenter;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.SimpleTextWatcher;
import com.liyuu.strategy.util.StringUtils;
import com.liyuu.strategy.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class SetTradingPasswordActivity extends BaseActivity<SetTradingPasswordPresenter> implements SetTradingPasswordContract.View {
    @BindView(R.id.tv_send_sms_note)
    TextView tvSendSmsNote;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.edt_trading_password)
    EditText edtTradePass;
    @BindView(R.id.sms_send_view)
    TextView tvGetVerification;
    @BindView(R.id.tv_edit_sure)
    TextView tvEditSure;
    @BindView(R.id.img_see)
    ImageView imgSee;

    private boolean isEditPwdCanSee = false;//密码默认不可见

    public static void start(Context context) {
        context.startActivity(new Intent(context, SetTradingPasswordActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_set_trading_passwd;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(UserInfoUtil.hasPayPwd()?R.string.update_trading_password:R.string.set_trading_password);

        String phone = UserInfoUtil.invertTel(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER));
        tvSendSmsNote.setText(String.format(getResources().getString(R.string.send_sms_note), phone));

        TextWatcher watcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setBtnEnabled();
            }
        };
        edtCode.addTextChangedListener(watcher);
        edtTradePass.addTextChangedListener(watcher);
        setPwdEdit(isEditPwdCanSee);
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.sms_send_view, R.id.img_see, R.id.tv_edit_sure})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.sms_send_view:
                mPresenter.getVerificationCode();
                break;
            case R.id.tv_edit_sure:
                mPresenter.changeTradePass(edtCode.getText().toString(), edtTradePass.getText().toString());
                break;
            case R.id.img_see:
                setPwdEdit(isEditPwdCanSee = !isEditPwdCanSee);
                break;
        }
    }

    @Override
    public void setVerificationCodeText(boolean isEnable, String text) {
        tvGetVerification.setEnabled(isEnable);
        tvGetVerification.setText(text);
    }

    private void setBtnEnabled() {
        if (TextUtils.isEmpty(edtTradePass.getText()) || TextUtils.isEmpty(edtCode.getText()) ) {
            tvEditSure.setEnabled(false);
            return;
        }
        tvEditSure.setEnabled(true);
    }

    private void setPwdEdit(boolean isCanSee) {
        int position = edtTradePass.getSelectionEnd();
        if (isCanSee) {
            imgSee.setImageResource(R.mipmap.icon_edt_can_see);
            //如果选中，显示密码
            edtTradePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            imgSee.setImageResource(R.mipmap.icon_edt_not_see);
            //否则隐藏密码
            edtTradePass.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        edtTradePass.setSelection(position);//将光标移至文字之前选中的位置
    }
}
