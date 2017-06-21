package com.example.live;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.animation.AnimationUtils;

import com.example.nyapp.R;
import com.jaronho.sdk.utils.ActivityTracker;

/**
 * Author:  jaron.ho
 * Date:    2017-04-30
 * Brief:   等待转圈
 */

public class WaitDialog {
    private static Dialog mDialog = null;
    private static OnKeyListener mOnKeyListener = new OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            return true;
        }
    };

    public static void show(boolean isFullScreen) {
        if (null == mDialog) {
            Activity activity = ActivityTracker.getTopActivity();
            mDialog = new Dialog(activity, isFullScreen ? R.style.live_dialog_fullscreen : R.style.live_dialog_withtitle);
            mDialog.setOnKeyListener(mOnKeyListener);
            mDialog.setContentView(R.layout.live_dialog_wait);
            mDialog.findViewById(R.id.imageview_circle).startAnimation(AnimationUtils.loadAnimation(activity, R.anim.loading_animation));
            mDialog.show();
        }
    }

    public static void cancel() {
        if (null != mDialog) {
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
