package com.nongyi.nylive;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jaronho.sdk.utils.ViewUtil;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter.MultiLayout;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.WrapRecyclerView;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVLiveManager;

import java.util.ArrayList;
import java.util.List;

public class AnchorLiveActivity extends AppCompatActivity {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private AVRootView mAVRootView;
    private WrapRecyclerView mMembersView;
    private List<String> mMemberDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_live);
        checkPermission();
        // AV视频控件
        mAVRootView = (AVRootView)findViewById(R.id.view_av_root);
        mAVRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        mAVRootView.setSubMarginX(12);
        mAVRootView.setSubMarginY(100);
        ILVLiveManager.getInstance().setAvVideoView(mAVRootView);
        // 关闭图片
        ImageView imageviewClose = (ImageView)findViewById(R.id.imageview_close);
        imageviewClose.setOnClickListener(onClickImageviewClose);
        // 闪光图片
        ImageView imageviewFlash = (ImageView)findViewById(R.id.imageview_flash);
        imageviewFlash.setOnClickListener(onClickImageviewFlash);
        // 切换相机图片
        ImageView imageviewSwitchCamera = (ImageView)findViewById(R.id.imageview_switch_camera);
        imageviewSwitchCamera.setOnClickListener(onClickImageviewSwitchCamera);
        // 美颜图片
        ImageView imageviewBeauty = (ImageView)findViewById(R.id.imageview_beauty);
        imageviewBeauty.setOnClickListener(onClickImageviewBeauty);
        // 美白图片
        ImageView imageviewWhite = (ImageView)findViewById(R.id.imageview_white);
        imageviewWhite.setOnClickListener(onClickImageviewWhite);
        // 麦克风图片
        ImageView imageviewMic = (ImageView)findViewById(R.id.imageview_mic);
        imageviewMic.setOnClickListener(onClickImageviewMic);
        // 分享图片
        ImageView imageviewShare = (ImageView)findViewById(R.id.imageview_share);
        imageviewShare.setOnClickListener(onClickImageviewShare);
        // 屏幕图片
        ImageView imageviewScreen = (ImageView)findViewById(R.id.imageview_screen);
        imageviewScreen.setOnClickListener(onClickImageviewScreen);
        // 观众列表
        mMembersView = (WrapRecyclerView)findViewById(R.id.recyclerview_members);
        mMembersView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMembersView.setLayoutManager(linearLayoutManager);
        mMembersView.setHorizontal(true);
        mMembersView.setAdapter(new WrapRecyclerViewAdapter<String>(this, mMemberDatas, new MultiLayout<String>() {
            @Override
            public int getLayoutId(int i, String data) {
                return 0;
            }
        }) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, String data) {

            }
        });
    }

    void checkPermission() {
        final List<String> permissionsList = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.CAMERA);
            if ((checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.RECORD_AUDIO);
            if ((checkSelfPermission(Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.WAKE_LOCK);
            if ((checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED))
                permissionsList.add(Manifest.permission.MODIFY_AUDIO_SETTINGS);
            if (permissionsList.size() != 0) {
                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_PHONE_PERMISSIONS);
            }
        }
    }

    // 点击关闭
    OnClickListener onClickImageviewClose = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    // 点击闪光
    OnClickListener onClickImageviewFlash = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(AnchorLiveActivity.this, "点击闪光");
        }
    };

    // 点击切换相机
    OnClickListener onClickImageviewSwitchCamera = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(AnchorLiveActivity.this, "点击切换相机");
        }
    };

    // 点击美颜
    OnClickListener onClickImageviewBeauty = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(AnchorLiveActivity.this, "点击美颜");
        }
    };

    // 点击美白
    OnClickListener onClickImageviewWhite = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(AnchorLiveActivity.this, "点击美白");
        }
    };

    // 点击麦克风
    OnClickListener onClickImageviewMic = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(AnchorLiveActivity.this, "点击麦克风");
        }
    };

    // 点击分享
    OnClickListener onClickImageviewShare = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(AnchorLiveActivity.this, "点击分享");
        }
    };

    // 点击切换全屏
    OnClickListener onClickImageviewScreen = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(AnchorLiveActivity.this, "点击切换全屏");
        }
    };
}
