package com.nongyi.nylive.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.Editable;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Author:  jaron.ho
 * Date:    2017-04-30
 * Brief:   聊天输入框
 */

public class ChatInputDialog extends Dialog {
    private InputMethodManager mImm = null;
    private EditText mMessageInput = null;
    private int mLastDiff = 0;
    private Listener mListener = null;

    public ChatInputDialog(Context context, int themeResId, int layoutId, int parentId, int exitTextId, int sendId) {
        super(context, themeResId);
        setContentView(layoutId);
        mImm = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        LinearLayout layoutMessageInput = (LinearLayout)findViewById(parentId);
        layoutMessageInput.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalMessageInput);
        layoutMessageInput.setOnClickListener(onClickMessageInput);
        mMessageInput = (EditText)findViewById(exitTextId);
        mMessageInput.setOnKeyListener(onKeyMessageInput);
        if (sendId > 0) {
            View viewSend = findViewById(sendId);
            if (null != viewSend) {
                viewSend.setOnClickListener(onClickSend);
            }
        }
    }

    @Override
    public void cancel() {
        super.cancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void show() {
        super.show();
        new Timer().schedule(new TimerTask() {
            public void run() {
                mImm.showSoftInput(mMessageInput, InputMethodManager.SHOW_FORCED);
            }
        }, 200);
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalMessageInput = new ViewTreeObserver.OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            // 获取当前界面可视部分
            getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
            // 获取屏幕的高度
            int screenHeight = getWindow().getDecorView().getRootView().getHeight();
            // 此处就是用来获取键盘的高度的,在键盘没有弹出的时候,此高度为0键盘弹出的时候为一个正数
            int heightDifference = screenHeight - r.bottom;
            if (heightDifference <= 0 && mLastDiff > 0) {
                mImm.hideSoftInputFromWindow(mMessageInput.getWindowToken(), 0);
                dismiss();
            }
            mLastDiff = heightDifference;
        }
    };

    private View.OnClickListener onClickMessageInput = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mImm.hideSoftInputFromWindow(mMessageInput.getWindowToken(), 0);
            dismiss();
        }
    };

    private View.OnKeyListener onKeyMessageInput = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (KeyEvent.ACTION_UP != event.getAction()) {   // 忽略其它事件
                return false;
            }
            switch (keyCode) {
                case KeyEvent.KEYCODE_ENTER:
                    Editable text = mMessageInput.getText();
                    if (text.length() > 0) {
                        mImm.showSoftInput(mMessageInput, InputMethodManager.SHOW_FORCED);
                        mImm.hideSoftInputFromWindow(mMessageInput.getWindowToken(), 0);
                        dismiss();
                    }
                    if (null != mListener) {
                        mListener.onSend(text.toString());
                    }
                    return true;
            }
            return false;
        }
    };

    private View.OnClickListener onClickSend = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Editable text = mMessageInput.getText();
            if (text.length() > 0) {
                mImm.showSoftInput(mMessageInput, InputMethodManager.SHOW_FORCED);
                mImm.hideSoftInputFromWindow(mMessageInput.getWindowToken(), 0);
                dismiss();
            }
            if (null != mListener) {
                mListener.onSend(text.toString());
            }
        }
    };

    /**
     * 功  能: 显示消息输入对话框
     * 参  数: activity - 活动
     *         themeResId - 主题资源id
     *         layoutId - 布局id
     *         parentId - 输入框父节点id
     *         exitTextId - 输入框id
     *         sendId - 发送id,可以<=0
     *         listener - 监听器
     * 返回值: ChatInputDialog
     */
    public static ChatInputDialog show(Activity activity, int themeResId, int layoutId, int parentId, int exitTextId, int sendId, Listener listener) {
        ChatInputDialog mid = new ChatInputDialog(activity, themeResId, layoutId, parentId, exitTextId, sendId);
        WindowManager windowManager = activity.getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mid.getWindow().getAttributes();
        Point p = new Point();
        display.getSize(p);
        lp.width = p.x; // 设置宽度
        mid.getWindow().setAttributes(lp);
        mid.setCancelable(true);
        mid.show();
        mid.mListener = listener;
        return mid;
    }

    /**
     * Brief:   消息发送监听器
     */
    public interface Listener {
        void onSend(String msg);
    }
}