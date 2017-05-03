package com.nongyi.nylive.view;

import android.Manifest;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jaronho.sdk.library.eventdispatcher.EventCenter;
import com.jaronho.sdk.library.eventdispatcher.EventDispatcher;
import com.jaronho.sdk.utils.ViewUtil;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.ChatInputDialog;
import com.jaronho.sdk.utils.view.RefreshView;
import com.nongyi.nylive.R;
import com.nongyi.nylive.bean.ChatMessage;
import com.nongyi.nylive.bean.GuestInfo;
import com.nongyi.nylive.utils.Constants;
import com.nongyi.nylive.utils.Global;
import com.tencent.TIMElem;
import com.tencent.TIMMessage;
import com.tencent.TIMTextElem;
import com.tencent.TIMUserProfile;
import com.tencent.av.sdk.AVRoomMulti;
import com.tencent.ilivesdk.ILiveCallBack;
import com.tencent.ilivesdk.ILiveConstants;
import com.tencent.ilivesdk.ILiveSDK;
import com.tencent.ilivesdk.core.ILiveLoginManager;
import com.tencent.ilivesdk.core.ILiveRoomManager;
import com.tencent.ilivesdk.view.AVRootView;
import com.tencent.ilivesdk.view.AVVideoView;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveManager;
import com.tencent.livesdk.ILVLiveRoomOption;
import com.tencent.livesdk.ILVText;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HostLiveActivity extends AppCompatActivity {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private final int MSG_UPDATE_LIVE_TIME = 1;
    private AVRootView mAVRootView = null;
    private TextView mGuestCountTextView = null;
    private RefreshView mGuestView = null;
    private List<GuestInfo> mGuestDatas = new ArrayList<>();
    private RefreshView mChatView = null;
    private List<ChatMessage> mChatDatas = new ArrayList<>();
    private HeartLayout mHeartLayout = null;
    private Dialog mQuitDialog = null;
    private Timer mLiveTimer = null;
    private long mLiveSeconds = 0;
    private TextView mLiveTimeTextView = null;
    private int mBeautyRate = 0;
    private LinearLayout mBeautyLayout = null;
    private SeekBar mBeautySeekBar = null;
    private RelativeLayout mFunctionsLayout = null;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_LIVE_TIME:
                    mLiveTimeTextView.setText(formatSeconds(mLiveSeconds));
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_host);
        checkPermission();
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
        // 飘心
        mHeartLayout = (HeartLayout)findViewById(R.id.heart_layout);
        // 直播时间
        mLiveTimeTextView = (TextView)findViewById(R.id.textview_time);
        // 功能列表
        mFunctionsLayout = (RelativeLayout)findViewById(R.id.layout_functions);
        // 观众列表
        initGuestView();
        // 聊天列表
        initChatView();
        // 初始房间
        initRoom();
        // 初始退出对话框
        initQuitDialog();
        // 初始美颜设置
        initBeautySetting();
        // 事件注册
        initEvents();
    }

    @Override
    protected void onPause() {
        super.onPause();
        ILVLiveManager.getInstance().onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ILVLiveManager.getInstance().onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ILVLiveManager.getInstance().quitRoom(null);
        if (null != mLiveTimer) {
            mLiveTimer.cancel();
            mLiveTimer = null;
        }
        EventCenter.unsubscribe(this);
    }

    @Override
    public void onBackPressed() {
        if (!mQuitDialog.isShowing()) {
            mQuitDialog.show();
        }
    }

    // 检查权限
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

    // 初始观众列表
    private void initGuestView() {
        mGuestCountTextView = (TextView)findViewById(R.id.textview_guest_count);
        mGuestView = (RefreshView)findViewById(R.id.refreshview_guests);
        mGuestView.setHorizontal(true);
        LinearLayoutManager llmGuest = new LinearLayoutManager(this);
        llmGuest.setOrientation(LinearLayoutManager.HORIZONTAL);
        mGuestView.getView().setLayoutManager(llmGuest);
        mGuestView.getView().setHasFixedSize(true);
        mGuestView.getView().setAdapter(new WrapRecyclerViewAdapter<GuestInfo>(this, mGuestDatas, R.layout.chunk_live_guest) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, GuestInfo data) {
            }
        });
        mGuestView.getView().addItemDecoration(new SpaceItemDecoration(true, (int)getResources().getDimension(R.dimen.guest_item_space)));
    }

    // 初始聊天列表
    private void initChatView() {
        mChatView = (RefreshView)findViewById(R.id.refreshview_message);
        mChatView.setHorizontal(false);
        LinearLayoutManager llmMessage = new LinearLayoutManager(this);
        llmMessage.setOrientation(LinearLayoutManager.VERTICAL);
        mChatView.getView().setLayoutManager(llmMessage);
        mChatView.getView().setHasFixedSize(true);
        mChatView.getView().setAdapter(new WrapRecyclerViewAdapter<ChatMessage>(this, mChatDatas, R.layout.chunk_live_chat) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, ChatMessage data) {
                String content = "<font color=\"#FFFF00\">" + data.getName() + ": </font>" + data.getContent();
                ViewUtil.setTextViewHtml((TextView)quickViewHolder.getView(R.id.textview_chat), content);
            }
        });
        mChatView.getView().addItemDecoration(new SpaceItemDecoration(false, (int)getResources().getDimension(R.dimen.chat_item_space)));
    }

    // 获取房间id
    private int getRoomId() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        int roomId = sp.getInt("room_id", 0);
        if (0 == roomId) {
            roomId = 1000;
        } else {
            roomId += 1;
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("room_id", roomId);
        editor.apply();
        return roomId;
    }

    // 初始房间
    private void initRoom() {
        // step1:AV视频控件
        mAVRootView = (AVRootView)findViewById(R.id.view_av_root);
        ILVLiveManager.getInstance().setAvVideoView(mAVRootView);
        mAVRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        mAVRootView.setSubMarginY(getResources().getDimensionPixelSize(R.dimen.small_area_margin_top));
        mAVRootView.setSubMarginX(getResources().getDimensionPixelSize(R.dimen.small_area_marginright));
        mAVRootView.setSubPadding(getResources().getDimensionPixelSize(R.dimen.small_area_marginbetween));
        mAVRootView.setSubWidth(getResources().getDimensionPixelSize(R.dimen.small_area_width));
        mAVRootView.setSubHeight(getResources().getDimensionPixelSize(R.dimen.small_area_height));
        mAVRootView.setSubCreatedListener(new AVRootView.onSubViewCreatedListener() {
            @Override
            public void onSubViewCreated() {
                for (int i = 1; i < ILiveConstants.MAX_AV_VIDEO_NUM; i++) {
                    final int index = i;
                    AVVideoView avVideoView = mAVRootView.getViewByIndex(index);
                    avVideoView.setGestureListener(new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapConfirmed(MotionEvent e) {
                            mAVRootView.swapVideoView(0, index);
                            return super.onSingleTapConfirmed(e);
                        }
                    });
                }
                mAVRootView.getViewByIndex(0).setGestureListener(new GestureDetector.SimpleOnGestureListener(){
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        return false;
                    }
                });
            }
        });
        // step2:创建房间配置项
        ILVLiveRoomOption hostOption = new ILVLiveRoomOption(ILiveLoginManager.getInstance().getMyUserId())
                .controlRole("LiveHost")    // 角色设置
                .autoFocus(true)
                .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)   // 支持后台模式
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)    // 权限设置
                .cameraId(ILiveConstants.FRONT_CAMERA)  // 摄像头前置后置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);    // 是否开始半自动接收
        // step3:创建房间
        ILVLiveManager.getInstance().createRoom(getRoomId(), hostOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                mAVRootView.getViewByIndex(0).setRotate(true);
                mLiveTimer = new Timer();
                mLiveTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ++mLiveSeconds;
                        mHandler.sendEmptyMessage(MSG_UPDATE_LIVE_TIME);
                    }
                }, 1000, 1000);
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                ViewUtil.showToast(HostLiveActivity.this, module + "|create fail " + errMsg + " " + errMsg);
            }
        });
    }

    // 初始退出对话框
    private void initQuitDialog() {
        mQuitDialog = new Dialog(this, R.style.dialog);
        mQuitDialog.setContentView(R.layout.dialog_quit_live);
        TextView tvSure = (TextView)mQuitDialog.findViewById(R.id.btn_sure);
        tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quitLive();
                mQuitDialog.dismiss();
            }
        });
        TextView tvCancel = (TextView)mQuitDialog.findViewById(R.id.btn_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuitDialog.cancel();
            }
        });
    }

    // 退出直播
    private void quitLive() {
        ILVCustomCmd cmd = new ILVCustomCmd();
        cmd.setCmd(Constants.AVIMCMD_EXITLIVE);
        cmd.setType(ILVText.ILVTextType.eGroupMsg);
        ILVLiveManager.getInstance().sendCustomCmd(cmd, new ILiveCallBack<TIMMessage>() {
            @Override
            public void onSuccess(TIMMessage data) {
                ILVLiveManager.getInstance().quitRoom(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        mAVRootView.clearUserView();
                        finish();
                    }
                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        Log.d("NYLive", "ILVB-SXB|quitRoom->failed:" + module + "|" + errCode + "|" + errMsg);
                        mAVRootView.clearUserView();
                        finish();
                    }
                });
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                finish();
            }
        });
    }

    // 初始美颜设置
    private void initBeautySetting() {
        mBeautyLayout = (LinearLayout)findViewById(R.id.chunk_beauty_setting);
        mBeautySeekBar = (SeekBar)(findViewById(R.id.seekbar_beauty));
        mBeautySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBeautyRate = progress;
                float value = (9.0f * progress / 100.0f);
                ILiveSDK.getInstance().getAvVideoCtrl().inputBeautyParam(value);  // 美颜
                ILiveSDK.getInstance().getAvVideoCtrl().inputWhiteningParam(value);   // 美白
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        TextView confirmTextView = (TextView)findViewById(R.id.textview_beauty_confirm);
        confirmTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeautyLayout.setVisibility(View.GONE);
                mFunctionsLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    // 事件注册
    private void initEvents() {
        EventCenter.subscribe(Constants.EVENT_NEW_TEXT_MSG, handleNewTextMsg, this);
        EventCenter.subscribe(Constants.EVENT_GUEST_ENTER_ROOM, handleGuestEnterRoom, this);
        EventCenter.subscribe(Constants.EVENT_GUEST_LEAVE_ROOM, handleGuestLeaveRoom, this);
    }

    // 格式化秒数
    private String formatSeconds(long seconds) {
        String hs, ms, ss;
        long h, m, s;
        h = seconds / 3600;
        m = (seconds % 3600) / 60;
        s = (seconds % 3600) % 60;
        if (h < 10) {
            hs = "0" + h;
        } else {
            hs = "" + h;
        }
        if (m < 10) {
            ms = "0" + m;
        } else {
            ms = "" + m;
        }
        if (s < 10) {
            ss = "0" + s;
        } else {
            ss = "" + s;
        }
        if (hs.equals("00")) {
            return ms + ":" + ss;
        } else {
            return hs + ":" + ms + ":" + ss;
        }
    }

    // 添加观众
    private void insertGuest(GuestInfo guest) {
        for (int i = 0, len = mGuestDatas.size(); i < len; ++i) {
            if (guest.getId().equals(mGuestDatas.get(i).getId())) {
                return;
            }
        }
        mGuestDatas.add(0, guest);
        mGuestView.getView().getAdapter().notifyDataSetChanged();
        mGuestCountTextView.setText(mGuestDatas.size() + "人");
    }

    // 删除观众
    private void removeGuest(GuestInfo guest) {
        for (int i = 0, len = mGuestDatas.size(); i < len; ++i) {
            if (guest.getId().equals(mGuestDatas.get(i).getId())) {
                mGuestDatas.remove(i);
                break;
            }
        }
        mGuestView.getView().getAdapter().notifyDataSetChanged();
        mGuestCountTextView.setText(mGuestDatas.size() + "人");
    }

    // 点击关闭
    private OnClickListener onClickImageviewClose = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mQuitDialog.isShowing()) {
                mQuitDialog.show();
            }
        }
    };

    // 点击聊天
    private OnClickListener onClickImageviewMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final ChatInputDialog dlg = new ChatInputDialog(HostLiveActivity.this, R.style.dialog_message_input, R.layout.dialog_chat_input);
            dlg.show();
            final EditText editTextInput = (EditText)dlg.findViewById(R.id.edittext_input);
            editTextInput.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (KeyEvent.ACTION_UP != event.getAction()) {   // 忽略其它事件
                        return false;
                    }
                    switch (keyCode) {
                        case KeyEvent.KEYCODE_ENTER:
                            Editable text = editTextInput.getText();
                            if (text.length() > 0) {
                                dlg.dismiss();
                            }
                            sendChatMessage(text.toString());
                            return true;
                    }
                    return false;
                }
            });
            TextView textViewSend = (TextView)dlg.findViewById(R.id.textview_send);
            textViewSend.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Editable text = editTextInput.getText();
                    if (text.length() > 0) {
                        dlg.dismiss();
                    }
                    sendChatMessage(text.toString());
                }
            });
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
            mFunctionsLayout.setVisibility(View.INVISIBLE);
            mBeautyLayout.setVisibility(View.VISIBLE);
            mBeautySeekBar.setProgress(mBeautyRate);
        }
    };

    // 点击切换相机
    private OnClickListener onClickImageviewSwitchCamera = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ILiveRoomManager.getInstance().enableCamera((ILiveRoomManager.getInstance().getCurCameraId() + 1) % 2, true);
        }
    };

    // 点击物品
    private OnClickListener onClickImageviewGoods = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(HostLiveActivity.this, "房间id: " + ILiveRoomManager.getInstance().getRoomId());
        }
    };

    // 消息输入监听器
    private void sendChatMessage(String msg) {
        if (0 == msg.length()) {
            ViewUtil.showToast(HostLiveActivity.this, "输入不能为空");
            return;
        }
        try {
            byte[] byteNum = msg.getBytes("utf8");
            if (byteNum.length > Global.CHAT_MESSAGE_MAX_LENGTH) {
                ViewUtil.showToast(HostLiveActivity.this, "消息输入太长");
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        TIMTextElem elem = new TIMTextElem();
        elem.setText(msg);
        TIMMessage timMessage = new TIMMessage();
        if (0 != timMessage.addElement(elem)) {
            return;
        }
        ILiveRoomManager.getInstance().sendGroupMessage(timMessage, new ILiveCallBack<TIMMessage>() {
            @Override
            public void onSuccess(TIMMessage data) {
                if (data.getElementCount() > 0) {
                    // 发送成回显示消息内容
                    TIMElem elem = data.getElement(0);
                    TIMTextElem textElem = (TIMTextElem)elem;
                    String name;
                    if (data.isSelf()) {
                        name = "我";
                    } else {
                        TIMUserProfile sendUser = data.getSenderProfile();
                        if (null != sendUser) {
                            name = sendUser.getNickName();
                        } else {
                            name = data.getSender();
                        }
                    }
                    ChatMessage cm = new ChatMessage();
                    cm.setSenderId(data.getSender());
                    cm.setName(name);
                    cm.setContent(textElem.getText());
                    refreshMessageView(cm);
                }
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ViewUtil.showToast(HostLiveActivity.this, "消息发送失败: " + module + "|" + errCode + "|" + errMsg);
            }
        });
    }

    // 刷新消息框
    private void refreshMessageView(ChatMessage cm) {
        if (mChatDatas.size() > Global.CHAT_MESSAGE_MAX_COUNT) {
            mChatDatas.remove(0);
        }
        mChatDatas.add(cm);
        mChatView.getView().getAdapter().notifyDataSetChanged();
        mChatView.getView().scrollToPosition(mChatDatas.size() - 1);
    }

    // 处理新文本消息
    private EventDispatcher.Handler handleNewTextMsg = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            refreshMessageView((ChatMessage)o);
        }
    };

    // 处理观众进入房间
    private EventDispatcher.Handler handleGuestEnterRoom = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            insertGuest((GuestInfo)o);
        }
    };

    // 处理观众离开房间
    private EventDispatcher.Handler handleGuestLeaveRoom = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            removeGuest((GuestInfo)o);
        }
    };
}
