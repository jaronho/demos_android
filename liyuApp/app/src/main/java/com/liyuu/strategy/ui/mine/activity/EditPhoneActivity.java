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
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.mine.EditPhoneContract;
import com.liyuu.strategy.presenter.mine.EditPhonePresenter;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.SimpleTextWatcher;
import com.liyuu.strategy.util.UserInfoUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPhoneActivity extends BaseActivity<EditPhonePresenter> implements EditPhoneContract.View {

    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edt_code)
    EditText edtCode;
    @BindView(R.id.sms_send_view)
    TextView tvGetVerification;
    @BindView(R.id.tv_edit_sure)
    TextView tvEditSure;
    @BindView(R.id.tv_note_edit_phone)
    TextView tvNoteEditPhone;
    @BindView(R.id.tv_phone_num_entry)
    TextView tvPhoneNumEntry;

    private int tel_type = 5;//5原手机号 6新手机号

    public static void start(Context context) {
        Intent intent = new Intent(context, EditPhoneActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_phone;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle("修改手机");

        String phone = UserInfoUtil.invertTel(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER));
        edtPhone.setText(phone);
        edtPhone.setTextColor(getResources().getColor(R.color.text_grey_b3b3b3));
        TextWatcher codeWatcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setBtnEnabled();
            }
        };
        edtPhone.addTextChangedListener(codeWatcher);
        edtCode.addTextChangedListener(codeWatcher);
    }

    @Override
    protected void initEventAndData() {

    }

    @OnClick({R.id.sms_send_view, R.id.tv_edit_sure})
    void onClick(View view) {
        String phone = tel_type == 5?PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_PHONE_NUMBER):edtPhone.getText().toString();
        switch (view.getId()) {
            case R.id.sms_send_view:
                mPresenter.getVerificationCode(phone, tel_type);
                break;
            case R.id.tv_edit_sure:
                mPresenter.verifyPhone(phone, edtCode.getText().toString(), tel_type);
                break;
        }
    }

    @Override
    public void setVerificationCodeText(boolean isEnable, String text) {
        tvGetVerification.setEnabled(isEnable);
        tvGetVerification.setText(text);
    }

    @Override
    public void setNewPhone() {
        edtPhone.setText("");
        edtCode.setText("");
        edtPhone.setTextColor(getResources().getColor(R.color.text_black_333333));
        edtPhone.setEnabled(true);
        edtPhone.requestFocus();
        tvPhoneNumEntry.setText(R.string.new_phone_number);
        tel_type = 6;
        tvNoteEditPhone.setText(R.string.verify_new_phone);
        tvEditSure.setText("确认");
    }

    private void setBtnEnabled() {
        if (TextUtils.isEmpty(edtPhone.getText()) || TextUtils.isEmpty(edtCode.getText()) ) {
            tvEditSure.setEnabled(false);
            return;
        }
        tvEditSure.setEnabled(true);
    }
}
