package com.liyuu.strategy.ui.dialog;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.component.ImageLoader;
import com.liyuu.strategy.model.bean.UserIndexBean;
import com.liyuu.strategy.ui.mine.WebViewActivity;

/**
 * 预留h5活动弹窗
 */
public class H5ActivityDialog extends BaseDialog implements View.OnClickListener {
    private final static String ACTIVITY_INFO_DATA = "activity_info_data";
    private UserIndexBean.ActivityBean data;

    public static H5ActivityDialog newInstance(UserIndexBean.ActivityBean data) {
        H5ActivityDialog dialog = new H5ActivityDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(ACTIVITY_INFO_DATA, data);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //将自带的背景设置为透明
        if (getDialog() != null && getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.dialog_h5_activity, container);
        if (getArguments() != null) {
            data = (UserIndexBean.ActivityBean) getArguments().getSerializable(ACTIVITY_INFO_DATA);
            ImageView img = view.findViewById(R.id.img_dialog);
            if (!TextUtils.isEmpty(data.getPopupImg())) {
                img.setVisibility(View.VISIBLE);
                ImageLoader.load(App.getInstance(), data.getPopupImg(), img);
            }

            TextView tvTitle = view.findViewById(R.id.tv_dialog_title);
            tvTitle.setText(data.getTitle());
            TextView tvDesc = view.findViewById(R.id.tv_dialog_message);
            tvDesc.setText(data.getDesc());
            TextView tvButton = view.findViewById(R.id.tv_dialog_button);
            tvButton.setText(data.getButton());
            tvButton.setOnClickListener(this);
        }

        view.findViewById(R.id.v_close).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_close:
                dismiss();
                break;
            case R.id.tv_dialog_button:
                if (getContext() != null && data != null && !TextUtils.isEmpty(data.getUrl()))
                    WebViewActivity.start(getContext(), data.getUrl());
                dismiss();
                break;
        }
    }
}
