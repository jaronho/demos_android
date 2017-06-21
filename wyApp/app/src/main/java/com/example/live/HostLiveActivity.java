package com.example.live;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.classes.Share;
import com.example.nyapp.R;
import com.example.util.ShareUtil;
import com.jaronho.sdk.library.eventdispatcher.EventCenter;
import com.jaronho.sdk.library.eventdispatcher.EventDispatcher;
import com.jaronho.sdk.third.circledialog.CircleDialog;
import com.jaronho.sdk.third.circledialog.callback.ConfigButton;
import com.jaronho.sdk.third.circledialog.callback.ConfigDialog;
import com.jaronho.sdk.third.circledialog.params.ButtonParams;
import com.jaronho.sdk.third.circledialog.params.DialogParams;
import com.jaronho.sdk.utils.UtilView;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.ChatInputDialog;
import com.jaronho.sdk.utils.view.RefreshView;
import com.squareup.picasso.Picasso;
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
import com.tencent.ilivesdk.core.ILiveRoomOption;
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

import static com.example.live.ConstantsLive.AVICMD_SendAddManager;
import static com.example.live.ConstantsLive.AVICMD_SendDelManager;
import static com.example.live.ConstantsLive.AVICMD_SendForbiddenSpeak;
import static com.example.live.ConstantsLive.AVICMD_SendRemove;
import static com.example.live.ConstantsLive.AVICMD_SendResumeSpeak;
import static com.example.live.ConstantsLive.AVIMCMD_ENTERLIVE;
import static com.example.live.ConstantsLive.AVIMCMD_EXITLIVE;

public class HostLiveActivity extends AppCompatActivity implements ILiveRoomOption.onRoomDisconnectListener {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private final int MSG_UPDATE_LIVE_TIME = 1;
    private AVRootView mAVRootView = null;
    private TextView mGuestCountTextView = null;
    private int mGuestCount = 0;
    private RefreshView mGuestView = null;
    private List<UserInfo> mGuestDatas = new ArrayList<>();
    private RefreshView mChatView = null;
    private List<ChatInfo> mChatDatas = new ArrayList<>();
    private HeartLayout mHeartLayout = null;
    private Timer mLiveTimer = null;
    private long mLiveSeconds = 0;
    private TextView mLiveTimeTextView = null;
    private int mBeautyRate = 0;
    private ChannelInfo mChannelInfo = null;
    private TextView mScoreTextView = null;
    private GiftQueueManager mGiftQueueManager = null;
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
        setContentView(R.layout.live_host_room_activity);
        checkPermission();
        mChannelInfo = getIntent().getExtras().getParcelable("channel_info");
        // 主播头像
        ImageView hostPic = (ImageView)findViewById(R.id.imageview_head_icon);
        if (null != mChannelInfo.PublisherAvatar && !mChannelInfo.PublisherAvatar.isEmpty()) {
            Picasso.with(HostLiveActivity.this).load(mChannelInfo.PublisherAvatar).into(hostPic);
        }
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
        // 房间详情图片
        ImageView imageviewRoomDetails = (ImageView)findViewById(R.id.imageview_room_details);
        imageviewRoomDetails.setOnClickListener(onClickImageviewRoomDetails);
        // 积分
        mScoreTextView = (TextView)findViewById(R.id.textview_score);
        mScoreTextView.setText(String.valueOf(mChannelInfo.Integral));
        // 飘心
        mHeartLayout = (HeartLayout)findViewById(R.id.heart_layout);
        // 直播时间
        mLiveTimeTextView = (TextView)findViewById(R.id.textview_desc);
        // 礼物队列管理器
        mGiftQueueManager = new GiftQueueManager(this);
        // 观众列表
        initGuestView();
        // 聊天列表
        initChatView();
        // 初始房间
        initRoom();
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
        LiveHelper.reqUpdateChannelStatus(mChannelInfo.Id, 2);
        ILVLiveManager.getInstance().quitRoom(null);
        if (null != mLiveTimer) {
            mLiveTimer.cancel();
            mLiveTimer = null;
        }
        ILVCustomCmd customCmd = new ILVCustomCmd();
        customCmd.setCmd(AVIMCMD_EXITLIVE);
        customCmd.setParam("");
        customCmd.setType(ILVText.ILVTextType.eGroupMsg);
        ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.d("NYLive", "send exit live success, data: " + data);
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.d("NYLive", "send exit live error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
            }
        });
        EventCenter.unsubscribe(this);
    }

    @Override
    public void onBackPressed() {
        showQuitDialog();
    }

    @Override
    public void onRoomDisconnect(int errCode, String errMsg) {
        LiveHelper.toast("与房间断开连接");
        mAVRootView.clearUserView();
        finish();
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
        mGuestView.getView().setAdapter(new WrapRecyclerViewAdapter<UserInfo>(this, mGuestDatas, R.layout.live_item_guest) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, final UserInfo data) {
                if (null != data.ProfilePicture && !data.ProfilePicture.isEmpty()) {
                    Picasso.with(HostLiveActivity.this).load(data.ProfilePicture).into((ImageView)quickViewHolder.getView(R.id.imageview_guest));
                }
                quickViewHolder.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickGuest(data.UserId, data.Name);
                    }
                });
            }
        });
        mGuestView.getView().addItemDecoration(new SpaceItemDecoration(true, (int)getResources().getDimension(R.dimen.live_guest_item_space)));
        LiveHelper.reqGetChannelUsersList(mChannelInfo.Id, 1, ConstantsLive.GUEST_MAX_COUNT, new LiveHelper.Callback<UserInfo>() {
            @Override
            public void onData(UserInfo data) {
            }
            @Override
            public void onDataList(List<UserInfo> dataList) {
                mGuestCount = LiveHelper.getTempRecords();
                mGuestCountTextView.setText(mGuestCount > 0 ? mGuestCount + "人" : "无人观看");
                for (int i = 0, len = dataList.size(); i < len; ++i) {
                    mGuestDatas.add(dataList.get(i));
                }
                int count = mGuestDatas.size();
                if (count > ConstantsLive.GUEST_MAX_COUNT) {
                    for (int i = 0, len = count - ConstantsLive.GUEST_MAX_COUNT; i < len; ++i) {
                        mGuestDatas.remove(ConstantsLive.GUEST_MAX_COUNT);
                    }
                }
                mGuestView.getView().getAdapter().notifyDataSetChanged();
            }
        });
    }

    // 初始聊天列表
    private void initChatView() {
        mChatView = (RefreshView)findViewById(R.id.refreshview_message);
        mChatView.setHorizontal(false);
        LinearLayoutManager llmMessage = new LinearLayoutManager(this);
        llmMessage.setOrientation(LinearLayoutManager.VERTICAL);
        mChatView.getView().setLayoutManager(llmMessage);
        mChatView.getView().setHasFixedSize(true);
        mChatView.getView().setAdapter(new WrapRecyclerViewAdapter<ChatInfo>(this, mChatDatas, R.layout.live_item_chat) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, final ChatInfo data) {
                ImageView avaterImageView = quickViewHolder.getView(R.id.imageview_avater);
                if (null != data.getAvatar() && !data.getAvatar().isEmpty()) {
                    Picasso.with(HostLiveActivity.this).load(data.getAvatar()).into(avaterImageView);
                }
                String content = "<font color=\"#FFFF00\">" + data.getName() + ": </font>" + data.getContent();
                TextView chatTextView = quickViewHolder.getView(R.id.textview_chat);
                UtilView.setTextViewHtml(chatTextView, content);
                avaterImageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickGuest(data.getSenderId(), data.getName());
                    }
                });
                chatTextView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickGuest(data.getSenderId(), data.getName());
                    }
                });
            }
        });
        mChatView.getView().addItemDecoration(new SpaceItemDecoration(false, (int)getResources().getDimension(R.dimen.live_chat_item_space)));
    }

    // 初始房间
    private void initRoom() {
        // step1:AV视频控件
        mAVRootView = (AVRootView)findViewById(R.id.view_av_root);
        ILVLiveManager.getInstance().setAvVideoView(mAVRootView);
        mAVRootView.setGravity(AVRootView.LAYOUT_GRAVITY_RIGHT);
        mAVRootView.setSubMarginY(getResources().getDimensionPixelSize(R.dimen.live_small_area_margin_top));
        mAVRootView.setSubMarginX(getResources().getDimensionPixelSize(R.dimen.live_small_area_marginright));
        mAVRootView.setSubPadding(getResources().getDimensionPixelSize(R.dimen.live_small_area_marginbetween));
        mAVRootView.setSubWidth(getResources().getDimensionPixelSize(R.dimen.live_small_area_width));
        mAVRootView.setSubHeight(getResources().getDimensionPixelSize(R.dimen.live_small_area_height));
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
                .roomDisconnectListener(HostLiveActivity.this)
                .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)   // 支持后台模式
                .authBits(AVRoomMulti.AUTH_BITS_DEFAULT)    // 权限设置
                .cameraId(ILiveConstants.FRONT_CAMERA)  // 摄像头前置后置
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO);    // 是否开始半自动接收
        // step3:创建房间
        ILVLiveManager.getInstance().createRoom(mChannelInfo.ChannelId, hostOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.d("NYLive", "create room success");
                WaitDialog.cancel();
                mLiveTimer = new Timer();
                mLiveTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ++mLiveSeconds;
                        mHandler.sendEmptyMessage(MSG_UPDATE_LIVE_TIME);
                        if (0 == mLiveSeconds % 5) {
                            LiveHelper.reqHeartbeat(mChannelInfo.Id);
                            LiveHelper.reqGetUserIntegral(mChannelInfo.PublisherID, false, new LiveHelper.Callback<String>() {
                                @Override
                                public void onData(String data) {
                                    mScoreTextView.setText(data);
                                }
                                @Override
                                public void onDataList(List<String> dataList) {
                                }
                            });
                        }
                    }
                }, 0, 1000);
                LiveHelper.reqUpdateChannelStatus(mChannelInfo.Id, 1);
                LiveHelper.reqGetUserIntegral(mChannelInfo.PublisherID, false, new LiveHelper.Callback<String>() {
                    @Override
                    public void onData(String data) {
                        mScoreTextView.setText(data);
                    }
                    @Override
                    public void onDataList(List<String> dataList) {
                    }
                });
                ILVCustomCmd customCmd = new ILVCustomCmd();
                customCmd.setCmd(AVIMCMD_ENTERLIVE);
                customCmd.setParam("");
                customCmd.setType(ILVText.ILVTextType.eGroupMsg);
                ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.d("NYLive", "send enter live success, data: " + data);
                    }
                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        Log.d("NYLive", "send enter live error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                    }
                });
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.d("NYLive", "create room error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                LiveHelper.toast("create room error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
            }
        });
    }

    // 显示退出对话框
    private void showQuitDialog() {
        new CircleDialog.Builder(this)
                .setText("是否退出房间？")
                .setPositive("确定", new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        quitLive();
                    }
                })
                .setNegative("取消", null)
                .setCancelable(true)
                .show();
    }

    // 退出直播
    private void quitLive() {
        ILVCustomCmd cmd = new ILVCustomCmd();
        cmd.setCmd(ConstantsLive.AVIMCMD_EXITLIVE);
        cmd.setType(ILVText.ILVTextType.eGroupMsg);
        ILVLiveManager.getInstance().sendCustomCmd(cmd, new ILiveCallBack<TIMMessage>() {
            @Override
            public void onSuccess(TIMMessage data) {
                Log.d("NYLive", "exit live success");
                ILVLiveManager.getInstance().quitRoom(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.d("NYLive", "quit room success");
                        mAVRootView.clearUserView();
                        finish();
                    }
                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        Log.d("NYLive", "quit room error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                        mAVRootView.clearUserView();
                        finish();
                    }
                });
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.d("NYLive", "exit live error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                finish();
            }
        });
    }

    // 显示美颜对话框
    private void showBeautyDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.live_dialog_beauty, null, false);
        final Dialog beautyDialog = new Dialog(this, R.style.live_dialog_fullscreen);
        beautyDialog.setContentView(view);
        SeekBar beautySeekBar = (SeekBar)(beautyDialog.findViewById(R.id.seekbar_beauty));
        beautySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        beautySeekBar.setProgress(mBeautyRate);
        beautyDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    beautyDialog.dismiss();
                }
                return true;
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                beautyDialog.dismiss();
            }
        });
        beautyDialog.show();
    }

    // 事件注册
    private void initEvents() {
        EventCenter.subscribe(ConstantsLive.AVIMCMD_TEXT, handleNewTextMsg, this);
        EventCenter.subscribe(ConstantsLive.AVIMCMD_ENTERLIVE, handleGuestEnterRoom, this);
        EventCenter.subscribe(ConstantsLive.AVIMCMD_EXITLIVE, handleGuestLeaveRoom, this);
        EventCenter.subscribe(ConstantsLive.AVIMCMD_PRAISE, handlePraise, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendForbiddenSpeak, handleForbiddenSpeak, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendResumeSpeak, handleResumeSpeak, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendRemove, handleRemove, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendAddManager, handleAddManager, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendDelManager, handleDelManager, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendGift, handleSendGift, this);
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
    private void insertGuest(UserInfo guest) {
        for (int i = 0, len = mGuestDatas.size(); i < len; ++i) {
            if (guest.UserId.equals(mGuestDatas.get(i).UserId)) {
                return;
            }
        }
        int count = mGuestDatas.size();
        if (count > ConstantsLive.GUEST_MAX_COUNT) {
            mGuestDatas.remove(count - 1);
        }
        mGuestDatas.add(0, guest);
        mGuestView.getView().getAdapter().notifyDataSetChanged();
        ++mGuestCount;
        mGuestCountTextView.setText(mGuestCount > 0 ? mGuestCount + "人" : "无人观看");
    }

    // 删除观众
    private void removeGuest(UserInfo guest) {
        boolean flag = false;
        for (int i = 0, len = mGuestDatas.size(); i < len; ++i) {
            if (guest.UserId.equals(mGuestDatas.get(i).UserId)) {
                mGuestDatas.remove(i);
                flag = true;
                break;
            }
        }
        if (!flag) {
            return;
        }
        mGuestView.getView().getAdapter().notifyDataSetChanged();
        --mGuestCount;
        if (mGuestCount < 0) {
            mGuestCount = 0;
        }
        mGuestCountTextView.setText(mGuestCount > 0 ? mGuestCount + "人" : "无人观看");
    }

    // 点击关闭
    private OnClickListener onClickImageviewClose = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showQuitDialog();
        }
    };

    // 点击聊天
    private OnClickListener onClickImageviewMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final ChatInputDialog dlg = new ChatInputDialog(HostLiveActivity.this, R.style.live_dialog_message_input, R.layout.live_dialog_chat_input);
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
            LiveHelper.reqShareChannel(mChannelInfo.Id, new LiveHelper.Callback<Share.DataBean>() {
                @Override
                public void onData(Share.DataBean data) {
                    ShareUtil.initShareDate(HostLiveActivity.this, data, new ShareUtil.ShareListener() {
                        @Override
                        public void onSuccess() {
                            LiveHelper.reqGetUserIntegral(LiveHelper.getUserId(), false, new LiveHelper.Callback<String>() {
                                @Override
                                public void onData(String data) {
                                    mScoreTextView.setText(data);
                                    LiveHelper.reqInteractiveAdd(mChannelInfo.Id, LiveHelper.getUserId(), 2);
                                }
                                @Override
                                public void onDataList(List<String> dataList) {
                                }
                            });
                        }
                        @Override
                        public void onError(String err) {
                            Log.d("NYLive", "share error: " + err);
                        }
                        @Override
                        public void onCancel() {
                        }
                    });
                }
                @Override
                public void onDataList(List<Share.DataBean> dataList) {
                }
            });
        }
    };

    // 点击美颜
    private OnClickListener onClickImageviewBeauty = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showBeautyDialog();
        }
    };

    // 点击切换相机
    private OnClickListener onClickImageviewSwitchCamera = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ILiveRoomManager.getInstance().enableCamera((ILiveRoomManager.getInstance().getCurCameraId() + 1) % 2, true);
        }
    };

    // 点击房间详情
    private OnClickListener onClickImageviewRoomDetails = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new CircleDialog.Builder(HostLiveActivity.this)
                    .setTitle("房间详情")
                    .setText(mChannelInfo.Details)
                    .setCancelable(true)
                    .show();
        }
    };

    // 消息输入监听器
    private void sendChatMessage(String msg) {
        if (0 == msg.length()) {
            LiveHelper.toast("输入不能为空");
            return;
        }
        try {
            byte[] byteNum = msg.getBytes("utf8");
            if (byteNum.length > ConstantsLive.CHAT_MESSAGE_MAX_LENGTH) {
                LiveHelper.toast("消息输入太长");
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
                    ChatInfo cm = new ChatInfo();
                    cm.setSenderId(data.getSender());
                    cm.setName(name);
                    cm.setContent(textElem.getText());
                    cm.setAvatar(LiveHelper.getUserAvatar());
                    refreshMessageView(cm);
                }
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                Log.d("NYLive", "send message error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                LiveHelper.toast("消息发送失败: module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
            }
        });
    }

    // 点击观众
    private void onClickGuest(final String userId, final String name) {
        if (userId.equals(LiveHelper.getUserId())) {
            LiveHelper.toast("无法对自己进行操作");
            return;
        }
        if (userId.equals(mChannelInfo.PublisherID)) {
            LiveHelper.toast("无法对群主进行操作");
            return;
        }
        LiveHelper.reqGetAuthority(userId, mChannelInfo.Id, false, new LiveHelper.Callback<AuthorityInfo>() {
            @Override
            public void onData(final AuthorityInfo data) {
                final String[] items = {
                        data.IsManage ? "取消设置为管理员" : "设置为管理员",
                        data.IsWords ? "允许发言" : "禁止发言",
                        data.IsIn ? "允许进入房间" : "踢出房间"};
                new CircleDialog.Builder(HostLiveActivity.this)
                        .configDialog(new ConfigDialog() {
                            @Override
                            public void onConfig(DialogParams params) {
                                params.animStyle = R.style.dialogWindowAnim;    // 增加弹出动画
                            }
                        })
                        .setTitle("是否对\"" + name + "\"进行操作？")
                        .setTitleColor(Color.BLUE)
                        .setItems(items, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (0 == position) {
                                    LiveHelper.reqChannelUserUpdateStatus(userId, mChannelInfo.Id, data.IsWords, data.IsIn, !data.IsManage, new LiveHelper.Callback<AuthorityInfo>() {
                                        @Override
                                        public void onData(AuthorityInfo data) {
                                            ILVCustomCmd customCmd = new ILVCustomCmd();
                                            customCmd.setCmd(data.IsManage ? AVICMD_SendAddManager : AVICMD_SendDelManager);
                                            customCmd.setParam("{\"userid\":" + userId + "}");
                                            customCmd.setType(ILVText.ILVTextType.eGroupMsg);
                                            ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
                                                @Override
                                                public void onSuccess(Object data) {
                                                    Log.d("NYLive", "send set manager success, data: " + data);
                                                    LiveHelper.toast("操作成功");
                                                }
                                                @Override
                                                public void onError(String module, int errCode, String errMsg) {
                                                    Log.d("NYLive", "send set manager  error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                                                    LiveHelper.toast("操作失败");
                                                }
                                            });
                                        }
                                        @Override
                                        public void onDataList(List<AuthorityInfo> dataList) {
                                        }
                                    });
                                } else if (1 == position) {
                                    LiveHelper.reqChannelUserUpdateStatus(userId, mChannelInfo.Id, !data.IsWords, data.IsIn, data.IsManage, new LiveHelper.Callback<AuthorityInfo>() {
                                        @Override
                                        public void onData(AuthorityInfo data) {
                                            ILVCustomCmd customCmd = new ILVCustomCmd();
                                            customCmd.setCmd(data.IsWords ? AVICMD_SendForbiddenSpeak : AVICMD_SendResumeSpeak);
                                            customCmd.setParam("{\"userid\":" + userId + "}");
                                            customCmd.setType(ILVText.ILVTextType.eGroupMsg);
                                            ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
                                                @Override
                                                public void onSuccess(Object data) {
                                                    Log.d("NYLive", "send set words success, data: " + data);
                                                    LiveHelper.toast("操作成功");
                                                }
                                                @Override
                                                public void onError(String module, int errCode, String errMsg) {
                                                    Log.d("NYLive", "send set words  error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                                                    LiveHelper.toast("操作失败");
                                                }
                                            });
                                        }
                                        @Override
                                        public void onDataList(List<AuthorityInfo> dataList) {
                                        }
                                    });
                                } else if (2 == position) {
                                    LiveHelper.reqChannelUserUpdateStatus(userId, mChannelInfo.Id, data.IsWords, !data.IsIn, data.IsManage, new LiveHelper.Callback<AuthorityInfo>() {
                                        @Override
                                        public void onData(AuthorityInfo data) {
                                            ILVCustomCmd customCmd = new ILVCustomCmd();
                                            customCmd.setCmd(data.IsIn ? AVICMD_SendRemove : 0);
                                            customCmd.setParam("{\"userid\":" + userId + "}");
                                            customCmd.setType(ILVText.ILVTextType.eGroupMsg);
                                            ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
                                                @Override
                                                public void onSuccess(Object data) {
                                                    Log.d("NYLive", "send set in success, data: " + data);
                                                    LiveHelper.toast("操作成功");
                                                }
                                                @Override
                                                public void onError(String module, int errCode, String errMsg) {
                                                    Log.d("NYLive", "send set in error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                                                    LiveHelper.toast("操作失败");
                                                }
                                            });
                                        }
                                        @Override
                                        public void onDataList(List<AuthorityInfo> dataList) {
                                        }
                                    });
                                }
                            }
                        })
                        .setNegative("取消", null)
                        .configNegative(new ConfigButton() {
                            @Override
                            public void onConfig(ButtonParams params) {
                                params.textColor = Color.RED;   // 取消按钮字体颜色
                            }
                        })
                        .show();
            }
            @Override
            public void onDataList(List<AuthorityInfo> dataList) {
            }
        });
    }

    // 刷新消息框
    private void refreshMessageView(ChatInfo cm) {
        if (mChatDatas.size() > ConstantsLive.CHAT_MESSAGE_MAX_COUNT) {
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
            refreshMessageView((ChatInfo)o);
        }
    };

    // 处理观众进入房间
    private EventDispatcher.Handler handleGuestEnterRoom = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            insertGuest((UserInfo)o);
        }
    };

    // 处理观众离开房间
    private EventDispatcher.Handler handleGuestLeaveRoom = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            removeGuest((UserInfo)o);
        }
    };

    // 处理点赞
    private EventDispatcher.Handler handlePraise = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            mHeartLayout.addFavor();
        }
    };

    // 处理禁言
    private EventDispatcher.Handler handleForbiddenSpeak = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {

        }
    };

    // 处理恢复禁言
    private EventDispatcher.Handler handleResumeSpeak = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            String userId = (String)o;
        }
    };

    // 处理踢出房间
    private EventDispatcher.Handler handleRemove = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            String userId = (String)o;
        }
    };

    // 处理设置为管理员
    private EventDispatcher.Handler handleAddManager = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            String userId = (String)o;
        }
    };

    // 处理移除管理员
    private EventDispatcher.Handler handleDelManager = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            String userId = (String)o;
        }
    };

    // 处理发送礼物
    private EventDispatcher.Handler handleSendGift = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            GiftSendInfo info = (GiftSendInfo)o;
            Log.d("NYLive", "gift send info: " + info.toString());
            LiveHelper.reqGetUserIntegral(mChannelInfo.PublisherID, false, new LiveHelper.Callback<String>() {
                @Override
                public void onData(String data) {
                    mScoreTextView.setText(data);
                }
                @Override
                public void onDataList(List<String> dataList) {
                }
            });
            mGiftQueueManager.recvGift(info);
        }
    };
}
