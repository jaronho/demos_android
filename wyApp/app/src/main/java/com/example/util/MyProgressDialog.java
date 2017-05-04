package com.example.util;


import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.nyapp.R;


/**
 * �Զ���dialog
 */
public class MyProgressDialog {
    private static Dialog loadingDialog;

    /**
     * ��ʾһ���ȴ��
     *
     * @param context               上下文
     * @param isCancel�Ƿ����÷���ȡ��
     * @param isRight               true�������ұ�false������
     */
    public static void show(Context context, boolean isCancel, boolean isRight) {

        creatDialog(context, "", isCancel, isRight);

    }

    /**
     * ��ʾһ���ȴ��
     *
     * @param context�����Ļ���
     * @param msg�ȴ�������
     * @param isCancel�Ƿ����÷���ȡ��
     * @param isRight               true�������ұ�false������
     */

    public static void show(Context context, String msg, boolean isCancel,
                            boolean isRight) {
        creatDialog(context, msg, isCancel, isRight);
    }

    private static void creatDialog(Context context, String msg,
                                    boolean isCancel, boolean isRight) {

        LinearLayout.LayoutParams wrap_content = new LinearLayout.LayoutParams(

                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        LinearLayout.LayoutParams wrap_content0 = new LinearLayout.LayoutParams(

                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        LinearLayout main = new LinearLayout(context);

        main.setBackgroundResource(R.drawable.relative);

        if (isRight) {

            main.setOrientation(LinearLayout.HORIZONTAL);

            wrap_content.setMargins(10, 0, 35, 0);

            wrap_content0.setMargins(35, 25, 0, 25);

        } else {

            main.setOrientation(LinearLayout.VERTICAL);

            wrap_content.setMargins(10, 5, 10, 15);

            wrap_content0.setMargins(35, 25, 35, 0);

        }

        main.setGravity(Gravity.CENTER);

        ImageView spaceshipImage = new ImageView(context);

        spaceshipImage.setImageResource(R.drawable.publicloading);

        TextView tipTextView = new TextView(context);

        tipTextView.setText("数据加载中。。。。");

        // ������ת����

        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
                context,

                R.anim.loading_animation);

        // ʹ��ImageView��ʾ����

        spaceshipImage.startAnimation(hyperspaceJumpAnimation);

        if (msg != null && !"".equals(msg))

            tipTextView.setText(msg);// ���ü�����Ϣ,�������Ĭ��ֵ

        loadingDialog = new Dialog(context, R.style.loading_dialog);

        loadingDialog.setCancelable(isCancel);// �Ƿ�����÷��ؼ�ȡ��

        main.addView(spaceshipImage, wrap_content0);

        main.addView(tipTextView, wrap_content);

        LinearLayout.LayoutParams fill_parent = new LinearLayout.LayoutParams(

                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);

        loadingDialog.setContentView(main, fill_parent);// ���ò���

        loadingDialog.show();
    }

    public static void cancel() {
        loadingDialog.dismiss();
    }

}
