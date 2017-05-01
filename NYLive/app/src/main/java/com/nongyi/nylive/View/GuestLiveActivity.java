package com.nongyi.nylive.view;

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
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.RefreshView;
import com.nongyi.nylive.R;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVLiveManager;

import java.util.ArrayList;
import java.util.List;

public class GuestLiveActivity extends AppCompatActivity {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private AVRootView mAVRootView = null;
    private RefreshView mGuestView = null;
    private List<String> mGuestDatas = new ArrayList<>();
    private RefreshView mChatView = null;
    private List<String> mChatDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_guest);
        checkPermission();
        // AV视频控件
        mAVRootView = (AVRootView)findViewById(R.id.view_av_root);
        ILVLiveManager.getInstance().setAvVideoView(mAVRootView);
        mAVRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        mAVRootView.setSubMarginY(getResources().getDimensionPixelSize(R.dimen.small_area_margin_top));
        mAVRootView.setSubMarginX(getResources().getDimensionPixelSize(R.dimen.small_area_marginright));
        mAVRootView.setSubPadding(getResources().getDimensionPixelSize(R.dimen.small_area_marginbetween));
        mAVRootView.setSubWidth(getResources().getDimensionPixelSize(R.dimen.small_area_width));
        mAVRootView.setSubHeight(getResources().getDimensionPixelSize(R.dimen.small_area_height));
        // 关闭图片
        ImageView imageviewClose = (ImageView)findViewById(R.id.imageview_close);
        imageviewClose.setOnClickListener(onClickImageviewClose);
        // 聊天图片
        ImageView imageviewMessage = (ImageView)findViewById(R.id.imageview_message);
        imageviewMessage.setOnClickListener(onClickImageviewMessage);
        // 分享图片
        ImageView imageviewShare = (ImageView)findViewById(R.id.imageview_share);
        imageviewShare.setOnClickListener(onClickImageviewShare);
        // 礼物图片
        ImageView imageviewGift = (ImageView)findViewById(R.id.imageview_gift);
        imageviewGift.setOnClickListener(onClickImageviewGift);
        // 红包图片
        ImageView imageviewMoney= (ImageView)findViewById(R.id.imageview_money);
        imageviewMoney.setOnClickListener(onClickImageviewMoney);
        // 物品图片
        ImageView imageviewGoods = (ImageView)findViewById(R.id.imageview_goods);
        imageviewGoods.setOnClickListener(onClickImageviewGoods);
        // 观众列表
        mGuestView = (RefreshView)findViewById(R.id.refreshview_guests);
        mGuestView.setHorizontal(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGuestView.getView().setLayoutManager(linearLayoutManager);
        mGuestView.getView().setHasFixedSize(true);
        mGuestView.getView().setAdapter(new WrapRecyclerViewAdapter<String>(this, mGuestDatas, R.layout.chunk_live_guest) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, String data) {
            }
        });
        mGuestView.getView().addItemDecoration(new SpaceItemDecoration(true, (int)getResources().getDimension(R.dimen.guest_item_space)));
        // 聊天列表
        mChatView = (RefreshView)findViewById(R.id.refreshview_message);
        mChatView.setHorizontal(false);
        LinearLayoutManager llmMessage = new LinearLayoutManager(this);
        llmMessage.setOrientation(LinearLayoutManager.VERTICAL);
        mChatView.getView().setLayoutManager(llmMessage);
        mChatView.getView().setHasFixedSize(true);
        mChatView.getView().setAdapter(new WrapRecyclerViewAdapter<String>(this, mChatDatas, R.layout.chunk_live_chat) {
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

    // 添加观众
    private void insertGuest(String guest) {
        mGuestDatas.add(0, guest);
        mGuestView.getView().getAdapter().notifyDataSetChanged();
    }

    // 删除观众
    private void removeGuest(String guest) {
        mGuestDatas.remove(guest);
        mGuestView.getView().getAdapter().notifyDataSetChanged();
    }

    // 点击关闭
    OnClickListener onClickImageviewClose = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    // 点击聊天
    OnClickListener onClickImageviewMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(GuestLiveActivity.this, "点击聊天");
        }
    };

    // 点击分享
    OnClickListener onClickImageviewShare = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(GuestLiveActivity.this, "点击分享");
        }
    };

    // 点击礼物
    OnClickListener onClickImageviewGift = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(GuestLiveActivity.this, "点击礼物");
        }
    };

    // 点击红包
    OnClickListener onClickImageviewMoney = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(GuestLiveActivity.this, "点击红包");
        }
    };

    // 点击物品
    OnClickListener onClickImageviewGoods = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(GuestLiveActivity.this, "点击点击物品");
        }
    };
}
