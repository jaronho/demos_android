package com.gsclub.strategy.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.MotionEvent;

import com.gsclub.strategy.app.App;
import com.gsclub.strategy.model.bean.AppUpdateBean;
import com.gsclub.strategy.model.bean.UserIndexBean;
import com.gsclub.strategy.ui.dialog.AlertDialog;
import com.gsclub.strategy.ui.dialog.H5ActivityDialog;
import com.gsclub.strategy.ui.dialog.UpdateAppDialog;
import com.gsclub.strategy.util.UserInfoUtil;

/**
 * Created by Administrator on 2017-08-23.
 */

public class DialogActivity extends FragmentActivity {

    /**
     * 单点登陆弹窗
     */
    public static void start(int param, String message) {
        Context context = App.getInstance();
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra("dialog", param);
        intent.putExtra("message", message);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    /**
     * APP升级弹窗
     */
    public static void start(int param, AppUpdateBean data) {
        Context context = App.getInstance();
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra("dialog", param);
        if (data != null) {
            intent.putExtra("param", data);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static void start(int param, UserIndexBean.ActivityBean data) {
        Context context = App.getInstance();
        Intent intent = new Intent(context, DialogActivity.class);
        intent.putExtra("dialog", param);
        if (data != null) {
            intent.putExtra("param", data);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    public static final int DIALOG_EXIT = 1;//单点登陆退出弹窗
    public static final int DIALOG_UPDATE = 2;//app升级弹窗
    public static final int DIALOG_H5_ACTIVITY = 3;//h5活动弹窗

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int dialog = intent.getIntExtra("dialog", 0);
        switch (dialog) {
            case DIALOG_EXIT:
                String message = intent.getStringExtra("message");
                showExitDialog(message);
                break;
            case DIALOG_UPDATE:
                AppUpdateBean p = (AppUpdateBean) intent.getSerializableExtra("param");
                showUpdateDialog(p);
                break;
            case DIALOG_H5_ACTIVITY:
                UserIndexBean.ActivityBean data = (UserIndexBean.ActivityBean) intent.getSerializableExtra("param");
                showH5ActivityDialog(data);
                break;
        }
    }

    private void showExitDialog(String message) {

        AlertDialog dialog = new AlertDialog(this);
        if (TextUtils.isEmpty(message)) {
            message = "您的帐号已经在另一台设备登录，请重新登录";
        }
        dialog.setTextView(message);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setSureButton("重新登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (UserInfoUtil.isLogin()) {// 收到通知栏通知时，可能出现没有点击就去登录的情况
                    UserInfoUtil.logout();
                }
                Intent intent = new Intent(DialogActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("is_login_again", true);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        dialog.setCancelButton("关闭", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
//                PreferenceUtils.cleanUserInfo();
//                RxBus.get().send(EventCode.USER_MODIFY);
                finish();
            }
        });
        dialog.show();
    }

    private void showUpdateDialog(AppUpdateBean p) {
        UpdateAppDialog dialog = new UpdateAppDialog(this, p);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        dialog.show();
    }

    private void showH5ActivityDialog(UserIndexBean.ActivityBean data) {
        H5ActivityDialog dialog = H5ActivityDialog.newInstance(data);
        dialog.show(getSupportFragmentManager());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            finish();
            return true;
        }
        return super.onTouchEvent(event);
    }
}
