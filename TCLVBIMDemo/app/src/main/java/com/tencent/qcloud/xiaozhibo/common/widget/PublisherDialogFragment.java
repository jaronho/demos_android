package com.tencent.qcloud.xiaozhibo.common.widget;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.push.TCPublishSettingActivity;
import com.tencent.qcloud.xiaozhibo.videorecord.TCVideoRecordActivity;

/**
 * Created by carolsuo on 2017/3/7.
 * 短视频或者直播选择界面
 */

public class PublisherDialogFragment extends DialogFragment {

    TextView mTVLive;
    TextView mTVVideo;
    ImageView mIVClose;
    private TextView mTVUGCEditer;
    private TextView mTVUGCComposer;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Dialog dialog = new Dialog(getActivity(), R.style.BottomDialog);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_publisher);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        mTVLive = (TextView) dialog.findViewById(R.id.tv_live);
        mTVVideo = (TextView) dialog.findViewById(R.id.tv_video);
        mTVUGCEditer = (TextView) dialog.findViewById(R.id.tv_ugc_cut);
        mTVUGCComposer = (TextView) dialog.findViewById(R.id.tv_ugc_join);
        mIVClose = (ImageView) dialog.findViewById(R.id.publisher_close);

        mTVLive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(PublisherDialogFragment.this.getActivity(), TCPublishSettingActivity.class));
            }
        });

        mTVVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                startActivity(new Intent(PublisherDialogFragment.this.getActivity(), TCVideoRecordActivity.class));
            }
        });

        mTVUGCEditer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Intent intent = new Intent(TCConstants.ACTION_UGC_SINGLE_CHOOSE);
                startActivity(intent);
            }
        });

        mTVUGCComposer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(TCConstants.ACTION_UGC_MULTI_CHOOSE);
                startActivity(intent);
            }
        });

        mIVClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setAttributes(lp);

        return dialog;
    }

}
