package com.liyuu.strategy.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.liyuu.strategy.R;

public class LoadingDialog {

    private Dialog dialog;

    public LoadingDialog(Context context) {
        dialog = new Dialog(context, R.style.Dialog_TransparentNoTitle);
        dialog.setContentView(createView(context));
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show() {
        if(!dialog.isShowing()) {
            dialog.show();
        }
    }

    public void cancel() {
        if(dialog.isShowing()) {
            dialog.cancel();
        }
    }

    public void dismiss() {
        if(dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    private View createView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        return view;
    }

    /**
     * dp转换成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
