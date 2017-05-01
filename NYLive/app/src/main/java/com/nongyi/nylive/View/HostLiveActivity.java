package com.nongyi.nylive.View;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.jaronho.sdk.utils.ViewUtil;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.RefreshView;
import com.nongyi.nylive.R;
import com.tencent.TIMElem;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.livesdk.ILVLiveManager;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class HostLiveActivity extends AppCompatActivity {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private AVRootView mAVRootView = null;
    private RefreshView mGuestView = null;
    private List<String> mGuestDatas = new ArrayList<>();
    private RefreshView mMessageView = null;
    private List<String> mMessageDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_host);
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
        // 美颜图片
        ImageView imageviewBeauty = (ImageView)findViewById(R.id.imageview_beauty);
        imageviewBeauty.setOnClickListener(onClickImageviewBeauty);
        // 切换相机图片
        ImageView imageviewSwitchCamera = (ImageView)findViewById(R.id.imageview_switch_camera);
        imageviewSwitchCamera.setOnClickListener(onClickImageviewSwitchCamera);
        // 物品图片
        ImageView imageviewGoods = (ImageView)findViewById(R.id.imageview_goods);
        imageviewGoods.setOnClickListener(onClickImageviewGoods);
        // 观众列表
        mGuestView = (RefreshView)findViewById(R.id.refreshview_guests);
        mGuestView.setHorizontal(true);
        LinearLayoutManager llmGuest = new LinearLayoutManager(this);
        llmGuest.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGuestView.getView().setLayoutManager(llmGuest);
        mGuestView.getView().setHasFixedSize(true);
        mGuestView.getView().setAdapter(new WrapRecyclerViewAdapter<String>(this, mGuestDatas, R.layout.chunk_live_guest) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, String data) {
            }
        });
        mGuestView.getView().addItemDecoration(new SpaceItemDecoration(true, (int)getResources().getDimension(R.dimen.guest_item_space)));
        // 聊天列表
        mMessageView = (RefreshView)findViewById(R.id.refreshview_message);
        mMessageView.setHorizontal(false);
        LinearLayoutManager llmMessage = new LinearLayoutManager(this);
        llmMessage.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageView.getView().setLayoutManager(llmMessage);
        mMessageView.getView().setHasFixedSize(true);
        mMessageView.getView().setAdapter(new WrapRecyclerViewAdapter<String>(this, mMessageDatas, R.layout.chunk_live_message) {
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
    private OnClickListener onClickImageviewClose = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    // 点击聊天
    private OnClickListener onClickImageviewMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MessageInputDialog.show(HostLiveActivity.this,
                    R.style.dialog_message_input,
                    R.layout.dialog_message_input,
                    R.id.layout_message_input,
                    R.id.exittext_input,
                    R.id.textview_send,
                    mMessageInputListener);
        }
    };

    // 点击分享
    private OnClickListener onClickImageviewShare = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(HostLiveActivity.this, "点击分享");
        }
    };

    // 点击美颜
    private OnClickListener onClickImageviewBeauty = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(HostLiveActivity.this, "点击美颜");
        }
    };

    // 点击切换相机
    private OnClickListener onClickImageviewSwitchCamera = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(HostLiveActivity.this, "点击切换相机");
        }
    };

    // 点击物品
    private OnClickListener onClickImageviewGoods = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(HostLiveActivity.this, "点击点击物品");
        }
    };

    // 消息输入监听器
    private MessageInputDialog.Listener mMessageInputListener = new MessageInputDialog.Listener() {
        @Override
        public void onSend(String msg) {
            if (0 == msg.length()) {
                ViewUtil.showToast(HostLiveActivity.this, "输入不能为空");
                return;
            }
            try {
                byte[] byteNum = msg.getBytes("utf8");
                if (byteNum.length > 160) {
                    ViewUtil.showToast(HostLiveActivity.this, "消息输入太长");
                    return;
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return;
            }
            Log.d("NYLive", "消息 ====>" + msg);
            TIMTextElem elem = new TIMTextElem();
            elem.setText(msg);
            TIMMessage timMessage = new TIMMessage();
            if (0 != timMessage.addElement(elem)) {
                return;
            }
            ILiveRoomManager.getInstance().sendGroupMessage(timMessage, new ILiveCallBack<TIMMessage>() {
                @Override
                public void onSuccess(TIMMessage data) {
                    // 发送成回显示消息内容
                    for (int i = 0; i < data.getElementCount(); ++i) {
                        TIMElem elem = data.getElement(0);
                        TIMTextElem textElem = (TIMTextElem)elem;
                        String name;
                        if (data.isSelf()) {
                            name = "he1";
                        } else {
                            TIMUserProfile sendUser = data.getSenderProfile();
                            if (null != sendUser) {
                                name = sendUser.getNickName();
                            } else {
                                name = data.getSender();
                            }
                        }
                        refreshMessageView(name, textElem.getText());
                    }
                    ViewUtil.showToast(HostLiveActivity.this, "消息发送成功");
                }

                @Override
                public void onError(String module, int errCode, String errMsg) {
                    ViewUtil.showToast(HostLiveActivity.this, "消息发送失败: " + module + "|" + errCode + "|" + errMsg);
                }
            });
        }
    };

    // 刷新消息框
    private void refreshMessageView(String name, String msg) {
    }
}
