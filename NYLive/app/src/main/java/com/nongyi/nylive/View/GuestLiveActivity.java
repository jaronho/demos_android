package com.nongyi.nylive.view;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
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
import com.tencent.ilivesdk.core.ILiveLog;
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

public class GuestLiveActivity extends AppCompatActivity implements ILiveRoomOption.onRoomDisconnectListener {
    private final int REQUEST_PHONE_PERMISSIONS = 0;
    private AVRootView mAVRootView = null;
    private TextView mGuestCountTextView = null;
    private RefreshView mGuestView = null;
    private List<GuestInfo> mGuestDatas = new ArrayList<>();
    private RefreshView mChatView = null;
    private List<ChatMessage> mChatDatas = new ArrayList<>();
    private HeartLayout mHeartLayout = null;
    private Dialog mQuitDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);   // 不锁屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_guest);
        checkPermission();
        // 头像
        ImageView imageviewHeadIcon = (ImageView)findViewById(R.id.imageview_head_icon);
        imageviewHeadIcon.setOnClickListener(onClickImageviewHeadIcon);
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
        // 房间详情图片
        ImageView imageviewRoomDetails = (ImageView)findViewById(R.id.imageview_room_details);
        imageviewRoomDetails.setOnClickListener(onClickImageviewRoomDetails);
        // 飘心
        mHeartLayout = (HeartLayout)findViewById(R.id.heart_layout);
        // 观众列表
        initGuestView();
        // 聊天列表
        initChatView();
        // 初始房间
        initRoom();
        // 初始退出对话框
        initQuitDialog();
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
        EventCenter.unsubscribe(this);
    }

    @Override
    public void onBackPressed() {
        if (!mQuitDialog.isShowing()) {
            mQuitDialog.show();
        }
    }

    @Override
    public void onRoomDisconnect(int errCode, String errMsg) {
        ViewUtil.showToast(GuestLiveActivity.this, "与房间断开连接");
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
        // step2:进入房间配置选项
        String hostId = getIntent().getExtras().getString("host_id");
        ILVLiveRoomOption memberOption = new ILVLiveRoomOption(hostId)
                .controlRole("NormalGuest")    // 角色设置
                .autoCamera(false)
                .roomDisconnectListener(this)
                .videoMode(ILiveConstants.VIDEOMODE_BSUPPORT)
                .authBits(AVRoomMulti.AUTH_BITS_JOIN_ROOM | AVRoomMulti.AUTH_BITS_RECV_AUDIO | AVRoomMulti.AUTH_BITS_RECV_CAMERA_VIDEO | AVRoomMulti.AUTH_BITS_RECV_SCREEN_VIDEO)
                .videoRecvMode(AVRoomMulti.VIDEO_RECV_MODE_SEMI_AUTO_RECV_CAMERA_VIDEO)
                .autoMic(false);
        // step3:进入房间
        int roomId = getIntent().getExtras().getInt("room_id");
        int ret = ILVLiveManager.getInstance().joinRoom(roomId, memberOption, new ILiveCallBack() {
            @Override
            public void onSuccess(Object data) {
            }
            @Override
            public void onError(String module, int errCode, String errMsg) {
                ViewUtil.showToast(GuestLiveActivity.this, "join room failed:" + module + "|" + errCode + "|" + errMsg);
                mAVRootView.clearUserView();
                finish();
            }
        });
        checkEnterReturn(ret);
    }

    // 检查进入房间返回类型
    private void checkEnterReturn(int iRet){
        if (ILiveConstants.NO_ERR != iRet){
            ViewUtil.showToast(GuestLiveActivity.this, "enter room failed:" + iRet);
            if (ILiveConstants.ERR_ALREADY_IN_ROOM == iRet){     // 上次房间未退出处理做退出处理
                ILiveRoomManager.getInstance().quitRoom(new ILiveCallBack() {
                    @Override
                    public void onSuccess(Object data) {
                        mAVRootView.clearUserView();
                        finish();
                    }
                    @Override
                    public void onError(String module, int errCode, String errMsg) {
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
                mQuitDialog.dismiss();
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

    // 事件注册
    private void initEvents() {
        EventCenter.subscribe(Constants.EVENT_NEW_TEXT_MSG, handleNewTextMsg, this);
        EventCenter.subscribe(Constants.EVENT_GUEST_ENTER_ROOM, handleGuestEnterRoom, this);
        EventCenter.subscribe(Constants.EVENT_GUEST_LEAVE_ROOM, handleGuestLeaveRoom, this);
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

    // 点击头像
    private OnClickListener onClickImageviewHeadIcon = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(GuestLiveActivity.this, "主播: " + ILiveRoomManager.getInstance().getHostId());
        }
    };

    // 点击关闭
    OnClickListener onClickImageviewClose = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!mQuitDialog.isShowing()) {
                mQuitDialog.show();
            }
        }
    };

    // 点击聊天
    OnClickListener onClickImageviewMessage = new OnClickListener() {
        @Override
        public void onClick(View v) {
            final ChatInputDialog dlg = new ChatInputDialog(GuestLiveActivity.this, R.style.dialog_message_input, R.layout.dialog_chat_input);
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

    // 点击房间详情
    OnClickListener onClickImageviewRoomDetails = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ViewUtil.showToast(GuestLiveActivity.this, "房间id: " + ILiveRoomManager.getInstance().getRoomId());
        }
    };

    // 消息输入监听器
    private void sendChatMessage(String msg) {
        if (0 == msg.length()) {
            ViewUtil.showToast(GuestLiveActivity.this, "输入不能为空");
            return;
        }
        try {
            byte[] byteNum = msg.getBytes("utf8");
            if (byteNum.length > Global.CHAT_MESSAGE_MAX_LENGTH) {
                ViewUtil.showToast(GuestLiveActivity.this, "消息输入太长");
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
                ViewUtil.showToast(GuestLiveActivity.this, "消息发送失败: " + module + "|" + errCode + "|" + errMsg);
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
