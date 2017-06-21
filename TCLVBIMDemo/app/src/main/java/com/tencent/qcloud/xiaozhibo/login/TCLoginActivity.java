package com.tencent.qcloud.xiaozhibo.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCUtils;
import com.tencent.qcloud.xiaozhibo.mainui.TCMainActivity;
import com.tencent.qcloud.xiaozhibo.userinfo.ITCUserInfoMgrListener;
import com.tencent.qcloud.xiaozhibo.userinfo.TCUserInfoMgr;

/**
 * Created by RTMP on 2016/8/1
 */
public class TCLoginActivity extends Activity implements TCLoginMgr.TCLoginCallback {

    private static final String TAG = TCLoginActivity.class.getSimpleName();

    private TCLoginMgr mTCLoginMgr;

    //共用控件
    private RelativeLayout rootRelativeLayout;

    private ProgressBar progressBar;

    private EditText etPassword;

    private AutoCompleteTextView etLogin;

    private Button btnLogin;

    private TextInputLayout tilLogin, tilPassword;

    private TextView tvRegister;

    private boolean bIsGuest = false; //是不是游客登录

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        rootRelativeLayout = (RelativeLayout) findViewById(R.id.rl_login_root);

        mTCLoginMgr = TCLoginMgr.getInstance();

        if (null != rootRelativeLayout) {
            ViewTarget<RelativeLayout, GlideDrawable> viewTarget = new ViewTarget<RelativeLayout, GlideDrawable>(rootRelativeLayout) {
                @Override
                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                    this.view.setBackgroundDrawable(resource.getCurrent());
                }
            };

            Glide.with(getApplicationContext()) // safer!
                    .load(R.drawable.bg_dark)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(viewTarget);
        }

        etLogin = (AutoCompleteTextView) findViewById(R.id.et_login);

        etPassword = (EditText) findViewById(R.id.et_password);

        tvRegister = (TextView) findViewById(R.id.btn_register);

        btnLogin = (Button) findViewById(R.id.btn_login);

        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        tilLogin = (TextInputLayout) findViewById(R.id.til_login);

        tilPassword = (TextInputLayout) findViewById(R.id.til_password);

        userNameLoginViewInit();

        //检测是否存在缓存
        if (TCUtils.isNetworkAvailable(this)) {
            mTCLoginMgr.setTCLoginCallback(this);
            //返回true表示存在本地缓存，进行登录操作，显示loadingFragment
            if (TCLoginMgr.getInstance().checkCacheAndLogin()) {
                OnProcessFragment loadinfFragment = new OnProcessFragment();
                loadinfFragment.show(getFragmentManager(), "");
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //设置登录回调,resume设置回调避免被registerActivity冲掉
        mTCLoginMgr.setTCLoginCallback(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //删除登录回调
        mTCLoginMgr.removeTCLoginCallback();
    }

    /**
     * 用户名密码登录界面init
     */
    public void userNameLoginViewInit() {

        etLogin.setInputType(EditorInfo.TYPE_CLASS_TEXT);

        etLogin.setText("");
        etLogin.setError(null, null);

        etPassword.setText("");
        etPassword.setError(null, null);

        tilLogin.setHint(getString(R.string.activity_login_username));

        tilPassword.setHint(getString(R.string.activity_login_password));

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //注册界面 phoneView 与 normalView跳转逻辑一致
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(), TCRegisterActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //调用normal登录逻辑
                showOnLoading(true);

                attemptNormalLogin(etLogin.getText().toString(), etPassword.getText().toString());
                bIsGuest = false;

            }
        });
    }

    /**
     * trigger loading模式
     *
     * @param active
     */
    public void showOnLoading(boolean active) {
        if (active) {
            progressBar.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.INVISIBLE);
            etLogin.setEnabled(false);
            etPassword.setEnabled(false);
            tvRegister.setClickable(false);
        } else {
            progressBar.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            etLogin.setEnabled(true);
            etPassword.setEnabled(true);
            tvRegister.setClickable(true);
            tvRegister.setTextColor(getResources().getColor(R.color.colorTransparentGray));
        }

    }

    public void showLoginError(String errorString) {
        etLogin.setError(errorString);
        showOnLoading(false);
    }

    public void showPasswordError(String errorString) {
        etPassword.setError(errorString);
        showOnLoading(false);
    }

    /**
     * 登录成功后被调用，跳转至TCMainActivity
     */
    public void jumpToHomeActivity() {
        Intent intent = new Intent(this, TCMainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 用户名密码登录
     *
     * @param username 用户名
     * @param password 密码
     */
    public void attemptNormalLogin(String username, String password) {

        if (TCUtils.isUsernameVaild(username)) {
            if (TCUtils.isPasswordValid(password)) {
                if (TCUtils.isNetworkAvailable(this)) {
                    //调用LoginHelper进行普通登录
                    mTCLoginMgr.pwdLogin(username, password);
                } else {
                    Toast.makeText(getApplicationContext(), "当前无网络连接", Toast.LENGTH_SHORT).show();
                    showOnLoading(false);
                }
            } else {
                showPasswordError("密码长度应为8-16位");
            }
        } else {
            showLoginError("用户名不符合规范");
        }
    }


    /**
     * IMSDK登录成功
     */
    @Override
    public void onSuccess() {
        TCUserInfoMgr.getInstance().setUserId(mTCLoginMgr.getLastUserInfo().identifier, new ITCUserInfoMgrListener() {
            @Override
            public void OnQueryUserInfo(int error, String errorMsg) {
                // TODO: 16/8/10
            }

            @Override
            public void OnSetUserInfo(int error, String errorMsg) {
                if (0 != error)
                    Toast.makeText(getApplicationContext(), "设置 User ID 失败" + errorMsg, Toast.LENGTH_LONG).show();
            }
        });

        Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
        mTCLoginMgr.removeTCLoginCallback();
        showOnLoading(false);
        if (bIsGuest) {
//            showEditNicknameDia();
        } else {
            jumpToHomeActivity();
        }
    }

    /**
     * 失败
     *
     * @param errCode errCode
     * @param msg     msg
     */
    @Override
    public void onFailure(int errCode, String msg) {
        Log.d(TAG, "Login Error errCode:" + errCode + " msg:" + msg);
        showOnLoading(false);
        //被踢下线后弹窗显示被踢
        if (6208 == errCode) {
            TCUtils.showKickOutDialog(this);
        }
        Toast.makeText(getApplicationContext(), "登录失败" + msg, Toast.LENGTH_SHORT).show();

    }
}
