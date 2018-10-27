package com.liyuu.strategy.ui.login;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.liyuu.strategy.R;
import com.liyuu.strategy.app.Constants;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.login.NicknameContract;
import com.liyuu.strategy.model.bean.UserIndexBean;
import com.liyuu.strategy.presenter.login.NicknamePresenter;
import com.liyuu.strategy.ui.MainActivity;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.SimpleTextWatcher;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 设置昵称界面
 */

public class NicknameActivity extends BaseActivity<NicknamePresenter> implements NicknameContract.View {
    @BindView(R.id.tool_bar)
    Toolbar mToolbar;
    @BindView(R.id.edit_name)
    EditText edtNickname;
    private TextView tvRight;

    /**
     * 默认的筛选条件(正则:只能输入简体中文)
     */
    private String DEFAULT_REGEX = "[^\u4E00-\u9FA5]";


    /**
     *
     */
    public static void start(Context context) {
        Intent intent = new Intent(context, NicknameActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_nickname;
    }

    @Override
    public void initUI() {
        super.initUI();
        setTitle(R.string.setting_nickname);
        tvRight = ButterKnife.findById(getToolbar(), R.id.tv_right);
        tvRight.setText(R.string.store);
        tvRight.setTextColor(getResources().getColor(R.color.text_orange_ffdc83));
        tvRight.setEnabled(false);

        TextWatcher watcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                String inputStr = str.replaceAll(DEFAULT_REGEX, "");
                edtNickname.removeTextChangedListener(this);
                // et.setText方法可能会引起键盘变化,所以用editable.replace来显示内容
                editable.replace(0, editable.length(), inputStr.trim());
                edtNickname.addTextChangedListener(this);
                setBtnEnabled();
            }
        };
        edtNickname.addTextChangedListener(watcher);
    }

    @Override
    protected void initEventAndData() {
        setNickname();
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @OnClick({R.id.tv_right})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_right:
                mPresenter.commitNickname(edtNickname.getText().toString());
                break;
        }
    }

    private void setBtnEnabled() {
        if (TextUtils.isEmpty(edtNickname.getText())) {
            tvRight.setTextColor(getResources().getColor(R.color.text_orange_ffdc83));
            tvRight.setEnabled(false);
            return;
        }
        tvRight.setTextColor(getResources().getColor(R.color.text_orange_ff8200));
        tvRight.setEnabled(true);
    }

    @Override
    public void goToMainAct(UserIndexBean bean) {
        Intent intent = new Intent(NicknameActivity.this, MainActivity.class);
        intent.putExtra("show_register_success_dialog_message", bean.getDesc());
        intent.putExtra("show_register_success_dialog", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void setNickname() {
        String nickname = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME);
        if(!TextUtils.isEmpty(nickname)) {
            edtNickname.setText(nickname);
            //            edtNickname.setSelection(nickname.length());
            //如果nickname包含某些edittext无法显示被主动剔除的字段，造成设置实际文字长度与nickname长度不一致，导致越界异常
            edtNickname.setSelection(edtNickname.getText().toString().length());
        }
    }
}
