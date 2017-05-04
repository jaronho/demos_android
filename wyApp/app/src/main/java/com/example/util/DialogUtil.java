package com.example.util;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.example.nyapp.R;

/**
 * Created by NY on 2017/2/8.
 * 图片弹窗工具类
 */

public class DialogUtil {
    private static DialogUtil mInstance = null;

    public static DialogUtil Instance() {
        if (mInstance == null) {
            mInstance = new DialogUtil();
        }
        return mInstance;
    }

    // 定义一个显示指定组件的对话框
    public void createMessageDialog(Context context, String message) {
        new AlertDialog.Builder(context)
                .setTitle("系统提示")
                .setMessage(message)
                .setCancelable(true)
                .create()
                .show();
    }

    /**
     * 显示加载对话框
     *
     * @param context
     * @return
     */
    public AlertDialog createLoadingDialog(Context context) {
        AlertDialog loadingDialog;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.view_layout_loading, null);
        ImageView iv_loading = (ImageView) view.findViewById(R.id.iv_loading_pic);

        iv_loading.setImageResource(R.drawable.product_snap_up_in);

        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomDialog);
        builder.setView(view);
        loadingDialog = builder.create();
        loadingDialog.setCanceledOnTouchOutside(false);

        return loadingDialog;
    }

    public AlertDialog createSoldOutDialog(final Activity activity, String message) {
        final AlertDialog loadingDialog;
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.view_layout_loading, null);
        ImageView iv_loading = (ImageView) view.findViewById(R.id.iv_loading_pic);
        if (message.equals("-1")) {
            iv_loading.setImageResource(R.drawable.product_sold_out);
        } else if (message.equals("-2")) {
            iv_loading.setImageResource(R.drawable.product_not_qualified);
        }
        iv_loading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R.style.CustomDialog);
        builder.setView(view);
        loadingDialog = builder.create();
        loadingDialog.setCanceledOnTouchOutside(false);
        return loadingDialog;
    }
}