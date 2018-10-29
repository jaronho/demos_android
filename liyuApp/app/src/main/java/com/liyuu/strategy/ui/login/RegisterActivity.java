package com.liyuu.strategy.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
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
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.login.RegisterContract;
import com.liyuu.strategy.model.bean.UserIndexBean;
import com.liyuu.strategy.presenter.login.RegisterPresenter;
import com.liyuu.strategy.ui.mine.WebViewActivity;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 登录页面
 */

public class RegisterActivity extends BaseActivity<RegisterPresenter> implements RegisterContract.View {
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.edit_code)
    EditText editCode;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.sms_send_view)
    TextView smsSendView;
    @BindView(R.id.agree)
    TextView agree;
    @BindView(R.id.edt_pwd)
    EditText edtPwd;
    @BindView(R.id.img_see)
    ImageView imgSee;

    boolean isFromNewsWelfare;

    private boolean isEditPwdCanSee = false;//密码默认不可见
    private boolean isConfirmProtocol = true;//默认同意协议

    public static void start(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, boolean isFromNewsWelfare) {
        Intent intent = new Intent(context, RegisterActivity.class);
        intent.putExtra("isFromNewsWelfare", isFromNewsWelfare);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_register;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(getString(R.string.register));

        TextWatcher watcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                setBtnEnabled();
            }
        };
        edtPhone.addTextChangedListener(watcher);
        editCode.addTextChangedListener(watcher);
        edtPwd.addTextChangedListener(watcher);

        setPwdEdit(isEditPwdCanSee);

        setConfirmIcon(isConfirmProtocol);
    }

    @Override
    protected void initEventAndData() {
        isFromNewsWelfare = getIntent().getBooleanExtra("isFromNewsWelfare", false);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }


    @OnClick({R.id.agree, R.id.sms_send_view, R.id.tv_login, R.id.tv_login_msg,
            R.id.img_see, R.id.tv_privacy_policy, R.id.img_confirm_read_protocol})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.agree:
                String url = PreferenceUtils.getString(SPKeys.FILE_URL, SPKeys.URL_SERVICE);
                if (TextUtils.isEmpty(url)) break;
                WebViewActivity.start(this, url);
                break;
            case R.id.sms_send_view:
                mPresenter.getVerificationCode(edtPhone.getText().toString());
                break;
            case R.id.tv_login:
                mPresenter.register(edtPhone.getText().toString(), editCode.getText().toString(), edtPwd.getText().toString());
                break;
            case R.id.tv_login_msg:
                LoginActivity.start(this);
                break;
            case R.id.img_see:
                setPwdEdit(isEditPwdCanSee = !isEditPwdCanSee);
                break;
            case R.id.tv_privacy_policy:
                String urlPolice = PreferenceUtils.getString(SPKeys.FILE_URL, SPKeys.URL_PRIVACY);
                if (TextUtils.isEmpty(urlPolice)) break;
                WebViewActivity.start(this, urlPolice);
                break;
            case R.id.img_confirm_read_protocol:
                isConfirmProtocol = !isConfirmProtocol;
                setConfirmIcon(isConfirmProtocol);
                break;
        }
    }

    @Override
    public void setVerificationCodeText(boolean isEnable, String text) {
        smsSendView.setEnabled(isEnable);
        smsSendView.setText(text);
    }

    private void setPwdEdit(boolean isCanSee) {
        int position = edtPwd.getSelectionEnd();
        if (isCanSee) {
            imgSee.setImageDrawable(getResources().getDrawable(R.mipmap.icon_edt_can_see));
            //如果选中，显示密码
            edtPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            imgSee.setImageDrawable(getResources().getDrawable(R.mipmap.icon_edt_not_see));
            //否则隐藏密码
            edtPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
        edtPwd.setSelection(position);//将光标移至文字之前选中的位置
    }

    private void setBtnEnabled() {
        if (!isConfirmProtocol) {
            tvLogin.setEnabled(false);
            return;
        }

        if (TextUtils.isEmpty(edtPhone.getText()) ||
                TextUtils.isEmpty(editCode.getText()) ||
                TextUtils.isEmpty(edtPwd.getText()) ||
                edtPwd.getText().toString().length() < 6) {
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

    @Override
    public void intentToNickAct() {
        if (!isFromNewsWelfare) {
            NicknameActivity.start(this);
        }
    }

    @Override
    public void doNewsBuying(UserIndexBean data) {
        if (isFromNewsWelfare) {
            //新手活动
            RxBus.get().send(RxBus.Code.NEWS_WELFARE_BUYING);
        } else if (data != null && data.getActivityInfo() != null) {
            //h5活动
            RxBus.get().send(RxBus.Code.USER_LOGIN_SUCCESS_WITH_ACTIVITY_DIALOG_INFO, data.getActivityInfo());
        }
    }

    private void setConfirmIcon(boolean isConfirmProtocol) {
        ImageView img = findViewById(R.id.img_confirm_read_protocol);
        img.setImageResource(isConfirmProtocol ? R.mipmap.icon_circle_box_select : R.mipmap.icon_circle_box_unselect);
        setBtnEnabled();
    }
}
