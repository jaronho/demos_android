package com.liyuu.strategy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.liyuu.strategy.R;
import com.liyuu.strategy.model.bean.ShareInfoBean;
import com.liyuu.strategy.ui.mine.adapter.ShareDialogAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by hlw on 2017/12/27.
 */

public class ShareDialog extends Dialog {
    @BindView(R.id.rcv_share_content)
    RecyclerView rcvContent;

    private Context context;
    private ShareInfoBean shareInfoBean;
    private ShareDialogAdapter adapter;
    private List<String> list = new ArrayList<>();
    public OnShareSuccessListener onShareSuccessListener;
    private ShareDialog shareDialog;

    public ShareDialog(@NonNull Context context, ShareInfoBean bean) {
        super(context, R.style.ThemeDeviceAlertDialogStyle);
        this.shareDialog = this;
        this.context = context;
        this.shareInfoBean = bean;
    }

    public ShareDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.shareDialog = this;
        this.context = context;
    }

    protected ShareDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.shareDialog = this;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(true);
        if (getWindow() != null)
            getWindow().setWindowAnimations(R.style.dialog_anim_style); //设置窗口弹出动画
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.gravity = Gravity.BOTTOM;//设置dialog 在布局中的位置
//        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;//就是这个属性导致不能获取焦点,默认的是FLAG_NOT_FOCUSABLE,故名思义不能获取输入焦点,
//        params.dimAmount = 0.5f;//设置对话框的透明程度背景(非布局的透明度)
        getWindow().setAttributes(params);

        list = Arrays.asList(context.getResources().getStringArray(R.array.share_name));
        rcvContent.setLayoutManager(new GridLayoutManager(context, 3));
        rcvContent.setHasFixedSize(true);

        adapter = new ShareDialogAdapter(context, shareInfoBean);
        adapter.setData(list);
        adapter.setShareDialog(this);

        rcvContent.setAdapter(adapter);

    }

    @OnClick({R.id.tv_cancel})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                if (shareDialog != null && shareDialog.isShowing())
                    shareDialog.dismiss();
                break;
        }
    }

    public void setOnShareSuccessListener(OnShareSuccessListener onShareSuccessListener) {
        this.onShareSuccessListener = onShareSuccessListener;
    }

    public interface OnShareSuccessListener {
        void onShareSuccess();
    }

}
