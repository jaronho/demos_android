package com.liyuu.strategy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.liyuu.strategy.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditHeaderDialog extends Dialog {
    @BindView(R.id.tv_dialog_title)
    TextView tvTitle;
    private OnSelectPicture onSelectPicture;
    private OnTakePicture onTakePicture;
    private String title;

    public EditHeaderDialog(@NonNull Context context) {
        super(context, R.style.ThemeDeviceAlertDialogStyle);
    }

    public EditHeaderDialog(@NonNull Context context, String title) {
        super(context, R.style.ThemeDeviceAlertDialogStyle);
        this.title = title;
    }



    public EditHeaderDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected EditHeaderDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_header);
        getWindow().setWindowAnimations(R.style.dialog_anim_style); //设置窗口弹出动画
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;//设置dialog 在布局中的位置
        getWindow().setAttributes(params);
        if(!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_cancel, R.id.tv_select_album, R.id.tv_take_picture})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                dismissDialog();
                break;
            case R.id.tv_select_album:
                dismissDialog();
                if (onSelectPicture!=null)
                    onSelectPicture.onSelect();
                break;
            case R.id.tv_take_picture:
                dismissDialog();
                if (onTakePicture!=null)
                    onTakePicture.onTakePicture();
                break;
        }
    }

//    public void setTitle() {
//        tvTitle.setText("上传图片");
//    }

    private void dismissDialog() {
        if(isShowing()) dismiss();
    }

    public interface OnSelectPicture{
        void onSelect();
    }

    public void setOnSelectPicture(OnSelectPicture onSelectPicture) {
        this.onSelectPicture = onSelectPicture;
    }

    public interface OnTakePicture{
        void onTakePicture();
    }

    public void setOnTakePicture(OnTakePicture onTakePicture) {
        this.onTakePicture = onTakePicture;
    }
}
