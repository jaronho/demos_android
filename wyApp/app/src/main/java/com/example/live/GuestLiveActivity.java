package com.example.live;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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
import static com.example.live.ConstantsLive.AVICMD_SendGift;
import static com.example.live.ConstantsLive.AVICMD_SendRemove;
import static com.example.live.ConstantsLive.AVICMD_SendResumeSpeak;
import static com.example.live.ConstantsLive.AVIMCMD_ENTERLIVE;
import static com.example.live.ConstantsLive.AVIMCMD_EXITLIVE;

public class GuestLiveActivity extends AppCompatActivity implements ILiveRoomOption.onRoomDisconnectListener {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
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
    private ChannelInfo mChannelInfo = null;
    private TextView mScoreTextView = null;
    private GiftInfo mGiftInfo = null;
    private ImageView mGiftSelectImage = null;
    private GiftQueueManager mGiftQueueManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.live_guest_room_activity);
        checkPermission();
        mChannelInfo = getIntent().getExtras().getParcelable("channel_info");
        // 主播头像
        ImageView hostPic = (ImageView)findViewById(R.id.imageview_head_icon);
        if (null != mChannelInfo.PublisherAvatar && !mChannelInfo.PublisherAvatar.isEmpty()) {
            Picasso.with(GuestLiveActivity.this).load(mChannelInfo.PublisherAvatar).into(hostPic);
        }
        TextView textviewSign = (TextView)findViewById(R.id.textview_desc);
        textviewSign.setText(mChannelInfo.Title);
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
        // 收藏图片
        ImageView imageviewCollect= (ImageView)findViewById(R.id.imageview_collect);
        imageviewCollect.setOnClickListener(onClickImageviewCollect);
        // 房间详情图片
        ImageView imageviewRoomDetails = (ImageView)findViewById(R.id.imageview_room_details);
        imageviewRoomDetails.setOnClickListener(onClickImageviewRoomDetails);
        // 积分
        mScoreTextView = (TextView)findViewById(R.id.textview_score);
        mScoreTextView.setText(String.valueOf(mChannelInfo.Integral));
        // 飘心
        mHeartLayout = (HeartLayout)findViewById(R.id.heart_layout);
        ImageView likeImageView = (ImageView)findViewById(R.id.imageview_like);
        likeImageView.setOnClickListener(onClickImageViewLike);
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
        ILVLiveManager.getInstance().quitRoom(null);
        LiveHelper.reqChannelUserLeave(mChannelInfo.Id, LiveHelper.getUserId());
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
                    Picasso.with(GuestLiveActivity.this).load(data.ProfilePicture).into((ImageView)quickViewHolder.getView(R.id.imageview_guest));
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
                mGuestCount = LiveHelper.getTempRecords() + 1;
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
                UserInfo ui = new UserInfo();
                ui.UserId = LiveHelper.getUserId();
                ui.ProfilePicture = LiveHelper.getUserAvatar();
                insertGuest(ui);
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
                    Picasso.with(GuestLiveActivity.this).load(data.getAvatar()).into(avaterImageView);
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
        // step2:进入房间配置选项
        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(mChannelInfo.PublisherID)
                .controlRole("NormalGuest")    // 角色设置
                .autoCamera(false)
                .roomDisconnectListener(this)
                .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO)
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO)
                .autoMic(false);
        // step3:进入房间
        int ret = ILVLiveManager.getInstance().joinRoom(mChannelInfo.ChannelId, memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
                Log.d("NYLive", "join room success");
                mLiveTimer = new Timer();
                mLiveTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        ++mLiveSeconds;
                        if (0 == mLiveSeconds % 5) {
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
                LiveHelper.reqGetUserIntegral(mChannelInfo.PublisherID, false, new LiveHelper.Callback<String>() {
                    @Override
                    public void onData(String data) {
                        mScoreTextView.setText(data);
                    }
                    @Override
                    public void onDataList(List<String> dataList) {
                    }
                });
                LiveHelper.reqChannelUserAdd(mChannelInfo.Id, LiveHelper.getUserId());
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
                Log.d("NYLive", "join room error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                if (10010 == errCode) {
                    LiveHelper.toast("房间不存在");
                } else {
                    LiveHelper.toast("join room error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                }
                mAVRootView.clearUserView();
                finish();
            }
        });
        checkEnterReturn(ret);
    }

    // 检查进入房间返回类型
    private void checkEnterReturn(int iRet){
        if (ILiveConstants.NO_ERR != iRet){
            Log.d("NYLive", "join room error, ret: " + iRet);
            LiveHelper.toast("join room error, ret: " + iRet);
            if (ILiveConstants.ERR_ALREADY_IN_ROOM == iRet){     // 上次房间未退出处理做退出处理
                ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
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
            } else {
                mAVRootView.clearUserView();
                finish();
            }
        }
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
        cmd.setCmd(AVIMCMD_EXITLIVE);
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

    // 事件注册
    private void initEvents() {
        EventCenter.subscribe(ConstantsLive.AVIMCMD_TEXT, handleNewTextMsg, this);
        EventCenter.subscribe(AVIMCMD_ENTERLIVE, handleGuestEnterRoom, this);
        EventCenter.subscribe(AVIMCMD_EXITLIVE, handleGuestLeaveRoom, this);
        EventCenter.subscribe(ConstantsLive.AVIMCMD_PRAISE, handlePraise, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendForbiddenSpeak, handleForbiddenSpeak, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendResumeSpeak, handleResumeSpeak, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendRemove, handleRemove, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendAddManager, handleAddManager, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendDelManager, handleDelManager, this);
        EventCenter.subscribe(ConstantsLive.AVICMD_SendGift, handleSendGift, this);
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

    // 显示礼物对话框
    private void showGiftDialog(final String score, List<GiftInfo> dataList) {
        mGiftInfo = null;
        View view = LayoutInflater.from(this).inflate(R.layout.live_dialog_gift, null, false);
        final Dialog giftDialog = new Dialog(this, R.style.live_dialog_fullscreen);
        giftDialog.setContentView(view);
        giftDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_BACK == keyCode) {
                    giftDialog.dismiss();
                }
                return true;
            }
        });
        // 积分
        final TextView scoreTextView = (TextView)view.findViewById(R.id.textview_score);
        scoreTextView.setText(score);
        // 发送
        TextView sendTextView = (TextView)view.findViewById(R.id.textview_send);
        sendTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == mGiftInfo) {
                    LiveHelper.toast("请选择要送的礼物");
                    return;
                }
                if (mGiftInfo.Money > Integer.parseInt(score)) {
                    LiveHelper.toast("您当前积分不够");
                    return;
                }
                final GiftSendInfo info = new GiftSendInfo();
                info.fromId = LiveHelper.getUserId();
	            info.fromNickname = LiveHelper.getNickname();
	            info.fromPic = LiveHelper.getUserAvatar();
	            info.giftId = String.valueOf(mGiftInfo.Id);
	            info.giftName = mGiftInfo.Name;
	            info.giftPic = mGiftInfo.Pic;
	            info.giftNumber = "1";
                ILVCustomCmd customCmd = new ILVCustomCmd();
                customCmd.setCmd(AVICMD_SendGift);
                customCmd.setParam(info.toString());
                customCmd.setType(ILVText.ILVTextType.eGroupMsg);
                ILVLiveManager.getInstance().sendCustomCmd(customCmd, new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        Log.d("NYLive", "send gift success, data: " + data);
                        LiveHelper.reqSendGiving(LiveHelper.getUserId(), mChannelInfo.PublisherID, mChannelInfo.Id, mGiftInfo.Id, 1, new LiveHelper.Callback<Boolean>() {
                            @Override
                            public void onData(Boolean data) {
                                LiveHelper.reqGetUserIntegral(LiveHelper.getUserId(), false, new LiveHelper.Callback<String>() {
                                    @Override
                                    public void onData(String data) {
                                        scoreTextView.setText(data);
                                    }
                                    @Override
                                    public void onDataList(List<String> dataList) {
                                    }
                                });
                                info.fromNickname = "我";
                                mGiftQueueManager.recvGift(info);
                            }
                            @Override
                            public void onDataList(List<Boolean> dataList) {
                            }
                        });
                    }
                    @Override
                    public void onError(String module, int errCode, String errMsg) {
                        Log.d("NYLive", "send gift error, module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                    }
                });
            }
        });
        // 礼物列表
        RefreshView refreshViewGiftList = (RefreshView)view.findViewById(R.id.refreshview_gift_list);
        refreshViewGiftList.setHorizontal(false);
        GridLayoutManager glmMessage = new GridLayoutManager(this, 4);
        glmMessage.setOrientation(LinearLayoutManager.VERTICAL);
        refreshViewGiftList.getView().setLayoutManager(glmMessage);
        refreshViewGiftList.getView().setHasFixedSize(true);
        refreshViewGiftList.getView().setAdapter(new WrapRecyclerViewAdapter<GiftInfo>(this, dataList, R.layout.live_item_gift) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, final GiftInfo data) {
                final ImageView selectImageView = quickViewHolder.getView(R.id.imageview_select);
                quickViewHolder.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (null != mGiftSelectImage && selectImageView != mGiftSelectImage) {
                            mGiftSelectImage.setVisibility(View.INVISIBLE);
                        }
                        mGiftSelectImage = selectImageView;
                        mGiftSelectImage.setVisibility(View.VISIBLE);
                        mGiftInfo = data;
                    }
                });
                if (null != data.Pic && !data.Pic.isEmpty()) {
                    Picasso.with(GuestLiveActivity.this).load(data.Pic).into((ImageView)quickViewHolder.getView(R.id.imageview_gift));
                }
                UtilView.setTextViewHtml((TextView)quickViewHolder.getView(R.id.textview_name), data.Name);
                UtilView.setTextViewHtml((TextView)quickViewHolder.getView(R.id.textview_score), data.Money + "积分");
            }
        });
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                giftDialog.dismiss();
            }
        });
        giftDialog.show();
    }

    // 点击关闭
    OnClickListener onClickImageviewClose = new OnClickListener() {
        @Override
        public void onClick(View v) {
            showQuitDialog();
        }
    };

    // 点击聊天
    OnClickListener onClickImageviewMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LiveHelper.reqGetAuthority(LiveHelper.getUserId(), mChannelInfo.Id, false, new LiveHelper.Callback<AuthorityInfo>() {
                @Override
                public void onData(AuthorityInfo data) {
                    if (data.IsWords) { // 被禁言
                        LiveHelper.toast("您已被禁言，无法发送消息");
                        return;
                    }
                    final ChatInputDialog dlg = new ChatInputDialog(GuestLiveActivity.this, R.style.live_dialog_message_input, R.layout.live_dialog_chat_input);
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
                @Override
                public void onDataList(List<AuthorityInfo> dataList) {
                }
            });
        }
    };

    // 点击分享
    OnClickListener onClickImageviewShare = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LiveHelper.reqShareChannel(mChannelInfo.Id, new LiveHelper.Callback<Share.DataBean>() {
                @Override
                public void onData(Share.DataBean data) {
                    ShareUtil.initShareDate(GuestLiveActivity.this, data, new ShareUtil.ShareListener() {
                        @Override
                        public void onSuccess() {
                            LiveHelper.reqGetUserIntegral(LiveHelper.getUserId(), false, new LiveHelper.Callback<String>() {
                                @Override
                                public void onData(String data) {
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

    // 点击礼物
    OnClickListener onClickImageviewGift = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LiveHelper.reqGetUserIntegral(LiveHelper.getUserId(), true, new LiveHelper.Callback<String>() {
                @Override
                public void onData(final String data) {
                    LiveHelper.reqGetGivingList(new LiveHelper.Callback<GiftInfo>() {
                        @Override
                        public void onData(GiftInfo data) {
                        }
                        @Override
                        public void onDataList(List<GiftInfo> dataList) {
                            showGiftDialog(data, dataList);
                        }
                    });
                }
                @Override
                public void onDataList(List<String> dataList) {
                }
            });
        }
    };

    // 点击收藏
    OnClickListener onClickImageviewCollect = new OnClickListener() {
        @Override
        public void onClick(View v) {
            LiveHelper.reqInteractiveAdd(mChannelInfo.Id, LiveHelper.getUserId(), 1);
        }
    };

    // 点击房间详情
    OnClickListener onClickImageviewRoomDetails = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new CircleDialog.Builder(GuestLiveActivity.this)
                    .setTitle("房间详情")
                    .setText(mChannelInfo.Details)
                    .setCancelable(true)
                    .show();
        }
    };

    // 点赞
    private OnClickListener onClickImageViewLike = new OnClickListener() {
        @Override
        public void onClick(View v) {
            mHeartLayout.addFavor();
            LiveHelper.reqInteractiveAdd(mChannelInfo.Id, LiveHelper.getUserId(), 0);
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
                if (10010 == errCode) {
                    LiveHelper.toast("主播已离开，无法发消息");
                } else {
                    LiveHelper.toast("消息发送失败: module: " + module + ", errCode: " + errCode + ", errMsg: " + errMsg);
                }
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
        LiveHelper.reqGetAuthority(LiveHelper.getUserId(), mChannelInfo.Id, false, new LiveHelper.Callback<AuthorityInfo>() {
            @Override
            public void onData(AuthorityInfo data) {
                if (data.IsManage) {    // 观众是管理员
                    LiveHelper.reqGetAuthority(userId, mChannelInfo.Id, false, new LiveHelper.Callback<AuthorityInfo>() {
                        @Override
                        public void onData(final AuthorityInfo data) {
                            final String[] items = {
                                    data.IsManage ? "取消设置为管理员" : "设置为管理员",
                                    data.IsWords ? "允许发言" : "禁止发言",
                                    data.IsIn ? "允许进入房间" : "踢出房间"};
                            new CircleDialog.Builder(GuestLiveActivity.this)
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
            UserInfo gi = (UserInfo)o;
            removeGuest(gi);
            if (mChannelInfo.PublisherID.equals(gi.UserId)) {    // 主播离开房间
                new CircleDialog.Builder(GuestLiveActivity.this)
                        .setText("主播已退出房间？")
                        .setPositive("退出", new OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                quitLive();
                            }
                        })
                        .setCancelable(false)
                        .show();
            }
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
            String userId = (String)o;
            if (userId.equals(LiveHelper.getUserId())) {
                LiveHelper.toast("您已被禁止发言");
            }
        }
    };

    // 处理恢复禁言
    private EventDispatcher.Handler handleResumeSpeak = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            String userId = (String)o;
            if (userId.equals(LiveHelper.getUserId())) {
                LiveHelper.toast("您已被恢复发言");
            }
        }
    };

    // 处理踢出房间
    private EventDispatcher.Handler handleRemove = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            String userId = (String)o;
            if (userId.equals(LiveHelper.getUserId())) {
                quitLive();
                LiveHelper.toast("您已被踢出房间");
            }
        }
    };

    // 处理设置为管理员
    private EventDispatcher.Handler handleAddManager = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            String userId = (String)o;
            if (userId.equals(LiveHelper.getUserId())) {
                LiveHelper.toast("您已被设为管理员");
            }
        }
    };

    // 处理移除管理员
    private EventDispatcher.Handler handleDelManager = new EventDispatcher.Handler() {
        @Override
        public void onCallback(Object o) {
            String userId = (String)o;
            if (userId.equals(LiveHelper.getUserId())) {
                LiveHelper.toast("您已被撤除管理员");
            }
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
