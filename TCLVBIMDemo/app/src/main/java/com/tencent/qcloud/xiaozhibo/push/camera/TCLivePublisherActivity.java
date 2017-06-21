package com.tencent.qcloud.xiaozhibo.push.camera;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.TIMElemType;
import com.tencent.TIMMessage;
import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.common.utils.TCUtils;
import com.tencent.qcloud.xiaozhibo.common.activity.TCBaseActivity;
import com.tencent.qcloud.xiaozhibo.common.widget.TCSwipeAnimationController;
import com.tencent.qcloud.xiaozhibo.im.TCChatEntity;
import com.tencent.qcloud.xiaozhibo.im.TCChatMsgListAdapter;
import com.tencent.qcloud.xiaozhibo.im.TCChatRoomMgr;
import com.tencent.qcloud.xiaozhibo.common.widget.danmaku.TCDanmuMgr;
import com.tencent.qcloud.xiaozhibo.login.TCLoginMgr;
import com.tencent.qcloud.xiaozhibo.push.TCPusherMgr;
import com.tencent.qcloud.xiaozhibo.im.TCSimpleUserInfo;
import com.tencent.qcloud.xiaozhibo.common.widget.TCUserAvatarListAdapter;
import com.tencent.qcloud.xiaozhibo.userinfo.TCUserInfoMgr;
import com.tencent.qcloud.xiaozhibo.common.widget.beautysetting.BeautyDialogFragment;
import com.tencent.qcloud.xiaozhibo.push.DetailDialogFragment;
import com.tencent.qcloud.xiaozhibo.push.camera.widget.TCAudioControl;
import com.tencent.qcloud.xiaozhibo.common.widget.like.TCHeartLayout;
import com.tencent.qcloud.xiaozhibo.common.widget.TCInputTextMsgDialog;
import com.tencent.rtmp.ITXLivePushListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.TXLog;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import master.flame.danmaku.controller.IDanmakuView;

/**
 * Created by RTMP on 2016/8/4
 */
public class TCLivePublisherActivity extends TCBaseActivity implements ITXLivePushListener, View.OnClickListener, TCPusherMgr.PusherListener, TCInputTextMsgDialog.OnTextSendListener, TCChatRoomMgr.TCChatRoomListener, BeautyDialogFragment.OnBeautyParamsChangeListener{
    private static final String TAG = TCLivePublisherActivity.class.getSimpleName();


    public TXCloudVideoView mTXCloudVideoView;
    private ListView mListViewMsg;
    private TCInputTextMsgDialog mInputTextMsgDialog;

    private ArrayList<TCChatEntity> mArrayListChatEntity = new ArrayList<>();
    private TCChatMsgListAdapter mChatMsgListAdapter;

    private BeautyDialogFragment mBeautyDialogFragment;
    //    private SeekBar mWhiteningSeekBar;
//    private SeekBar mBeautySeekBar;
    private Button mFlashView;

    //头像列表控件
    private RecyclerView mUserAvatarList;
    private TCUserAvatarListAdapter mAvatarListAdapter;
    private float mScreenHeight;

    private ImageView mHeadIcon;
    private ImageView mRecordBall;
    private TextView mBroadcastTime;
    private TextView mHostName;
    private TextView mMemberCount;
    private TextView mDetailTime, mDetailAdmires, mDetailWatchCount;
    private BeautyDialogFragment.BeautyParams   mBeautyParams = new BeautyDialogFragment.BeautyParams();


    private long mSecond = 0;
    private Timer mBroadcastTimer;
    private BroadcastTimerTask mBroadcastTimerTask;

    private long lTotalMemberCount = 0;
    private long lMemberCount = 0;
    private long lHeartCount = 0;

    public TXLivePusher mTXLivePusher;
    protected TXLivePushConfig mTXPushConfig = new TXLivePushConfig();

    protected Handler mHandler = new Handler();

    private boolean mFlashOn = false;
    protected boolean mPasuing = false;

    protected String mPushUrl;
    private String mRoomId;
    protected String mUserId;
    private String mTitle;
    private String mCoverPicUrl;
    private String mHeadPicUrl;
    private String mNickName;
    private String mLocation;

    private TCPusherMgr mTCPusherMgr;
    private TCChatRoomMgr mTCChatRoomMgr;

    private TCAudioControl mAudioCtrl;
    protected Button mBtnAudioCtrl;
    private LinearLayout mAudioPluginLayout;
    private Button mBtnAudioEffect;
    private Button mBtnAudioClose;
    //点赞动画
    private TCHeartLayout mHeartLayout;

    //弹幕
    private TCDanmuMgr mDanmuMgr;

    private RelativeLayout mControllLayer;
    private TCSwipeAnimationController mTCSwipeAnimationController;

    //分享相关
    private SHARE_MEDIA mShare_meidia = SHARE_MEDIA.MORE;
    private String mShareUrl = TCConstants.SVR_LivePlayShare_URL;
    private boolean mSharedNotPublished = false; //分享之后还未开始推流

    //log相关
    protected boolean mShowLog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_publish);

        Intent intent = getIntent();
        mUserId = intent.getStringExtra(TCConstants.USER_ID);
        mPushUrl = intent.getStringExtra(TCConstants.PUBLISH_URL);
        mTitle = intent.getStringExtra(TCConstants.ROOM_TITLE);
        mCoverPicUrl = intent.getStringExtra(TCConstants.COVER_PIC);
        mHeadPicUrl = intent.getStringExtra(TCConstants.USER_HEADPIC);
        mNickName = intent.getStringExtra(TCConstants.USER_NICK);
        mLocation = intent.getStringExtra(TCConstants.USER_LOC);
        mShare_meidia = (SHARE_MEDIA) intent.getSerializableExtra(TCConstants.SHARE_PLATFORM);

        initView();

        mBeautyDialogFragment = new BeautyDialogFragment();

        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.disableLog(!mShowLog);
        }

        mScreenHeight = getResources().getDisplayMetrics().heightPixels;

        //初始化消息回调
        mTCChatRoomMgr = TCChatRoomMgr.getInstance();
        mTCChatRoomMgr.setMessageListener(this);

        mTCPusherMgr = TCPusherMgr.getInstance();
        mTCPusherMgr.setPusherListener(this);

        mTCChatRoomMgr.createGroup();

        TelephonyManager tm = (TelephonyManager) this.getApplicationContext().getSystemService(Service.TELEPHONY_SERVICE);
        tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
    }

    final PhoneStateListener listener = new PhoneStateListener(){
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch(state){
                //电话等待接听
                case TelephonyManager.CALL_STATE_RINGING:
                    if (mTXLivePusher != null) mTXLivePusher.pausePusher();
                    break;
                //电话接听
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    if (mTXLivePusher != null) mTXLivePusher.pausePusher();
                    break;
                //电话挂机
                case TelephonyManager.CALL_STATE_IDLE:
                    if (mTXLivePusher != null) mTXLivePusher.resumePusher();
                    break;
            }
        }
    };


    @Override
    public void onReceiveExitMsg() {
        super.onReceiveExitMsg();

        TXLog.d(TAG, "publisher broadcastReceiver receive exit app msg");
        //在被踢下线的情况下，执行退出前的处理操作：停止推流、关闭群组
        stopRecordAnimation();
        mTXCloudVideoView.onPause();
        stopPublish();
        quitRoom();
    }

    protected void initView() {

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.rl_publish_root);

        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mTCSwipeAnimationController.processEvent(event);
            }
        });

        mTXCloudVideoView = (TXCloudVideoView) findViewById(R.id.video_view);

        mControllLayer = (RelativeLayout) findViewById(R.id.rl_controllLayer);

        mTCSwipeAnimationController = new TCSwipeAnimationController(this);
        mTCSwipeAnimationController.setAnimationView(mControllLayer);

        mUserAvatarList = (RecyclerView) findViewById(R.id.rv_user_avatar);
        mAvatarListAdapter = new TCUserAvatarListAdapter(this, TCLoginMgr.getInstance().getLastUserInfo().identifier);
        mUserAvatarList.setAdapter(mAvatarListAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mUserAvatarList.setLayoutManager(linearLayoutManager);

        mListViewMsg = (ListView) findViewById(R.id.im_msg_listview);
        mHeartLayout = (TCHeartLayout) findViewById(R.id.heart_layout);

        mFlashView = (Button) findViewById(R.id.flash_btn);

        mInputTextMsgDialog = new TCInputTextMsgDialog(this, R.style.InputDialog);
        mInputTextMsgDialog.setmOnTextSendListener(this);

        mBroadcastTime = (TextView) findViewById(R.id.tv_broadcasting_time);
        mBroadcastTime.setText(String.format(Locale.US, "%s", "00:00:00"));
        mRecordBall = (ImageView) findViewById(R.id.iv_record_ball);

        mHeadIcon = (ImageView) findViewById(R.id.iv_head_icon);
        showHeadIcon(mHeadIcon, TCUserInfoMgr.getInstance().getHeadPic());
        mMemberCount = (TextView) findViewById(R.id.tv_member_counts);
        mMemberCount.setText("0");

        mChatMsgListAdapter = new TCChatMsgListAdapter(this, mListViewMsg, mArrayListChatEntity);
        mListViewMsg.setAdapter(mChatMsgListAdapter);

        IDanmakuView danmakuView = (IDanmakuView) findViewById(R.id.danmakuView);
        mDanmuMgr = new TCDanmuMgr(this);
        mDanmuMgr.setDanmakuView(danmakuView);

        //AudioControl
        mBtnAudioCtrl = (Button) findViewById(R.id.btn_audio_ctrl);
        mAudioCtrl = (TCAudioControl) findViewById(R.id.layoutAudioControlContainer);
        mAudioPluginLayout = (LinearLayout) findViewById(R.id.audio_plugin);
        mAudioCtrl.setPluginLayout(mAudioPluginLayout);
        mBtnAudioEffect = (Button) findViewById(R.id.btn_audio_effect);
        mBtnAudioClose = (Button) findViewById(R.id.btn_audio_close);

    }

    protected void startPublish() {
        mSharedNotPublished = false;
        mSharedNotPublished = false;
        if (mTXLivePusher == null) {
            mTXLivePusher = new TXLivePusher(TCLivePublisherActivity.this);
            mBeautyDialogFragment.setBeautyParamsListner(mBeautyParams,this);
            mTXLivePusher.setPushListener(TCLivePublisherActivity.this);
            mTXPushConfig.setAutoAdjustBitrate(false);

            //切后台推流图片
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish, options);
            mTXPushConfig.setPauseImg(bitmap);
            mTXPushConfig.setPauseFlag(TXLiveConstants.PAUSE_FLAG_PAUSE_VIDEO|TXLiveConstants.PAUSE_FLAG_PAUSE_AUDIO);
            mTXPushConfig.setBeautyFilter(mBeautyParams.mBeautyProgress, mBeautyParams.mWhiteProgress);
            mTXPushConfig.setFaceSlimLevel(mBeautyParams.mFaceLiftProgress);
            mTXPushConfig.setEyeScaleLevel(mBeautyParams.mBigEyeProgress);
            mTXLivePusher.setConfig(mTXPushConfig);
        }
        mAudioCtrl.setPusher(mTXLivePusher);
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setVisibility(View.VISIBLE);
            mTXCloudVideoView.clearLog();
        }
        //mBeautySeekBar.setProgress(100);

        //设置视频质量：高清
        mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
        mTXCloudVideoView.enableHardwareDecode(true);
        mTXLivePusher.startCameraPreview(mTXCloudVideoView);
        mTXLivePusher.setMirror(true);
        mTXLivePusher.startPusher(mPushUrl);
    }

    protected void stopPublish() {
        if (mTXLivePusher != null) {
            mTXLivePusher.stopCameraPreview(false);
            mTXLivePusher.setPushListener(null);
            mTXLivePusher.stopPusher();
        }
        if (mAudioCtrl != null) {
            mAudioCtrl.unInit();
            mAudioCtrl = null;
        }
    }

    /**
     * 加载主播头像
     *
     * @param view   view
     * @param avatar 头像链接
     */
    private void showHeadIcon(ImageView view, String avatar) {
        TCUtils.showPicWithUrl(this, view, avatar, R.drawable.face);
    }

    private ObjectAnimator mObjAnim;

    /**
     * 开启红点与计时动画
     */
    private void startRecordAnimation() {

        mObjAnim = ObjectAnimator.ofFloat(mRecordBall, "alpha", 1f, 0f, 1f);
        mObjAnim.setDuration(1000);
        mObjAnim.setRepeatCount(-1);
        mObjAnim.start();

        //直播时间
        if (mBroadcastTimer == null) {
            mBroadcastTimer = new Timer(true);
            mBroadcastTimerTask = new BroadcastTimerTask();
            mBroadcastTimer.schedule(mBroadcastTimerTask, 1000, 1000);
        }
    }

    /**
     * 关闭红点与计时动画
     */
    private void stopRecordAnimation() {

        if (null != mObjAnim)
            mObjAnim.cancel();

        //直播时间
        if (null != mBroadcastTimer) {
            mBroadcastTimerTask.cancel();
        }
    }

    @Override
    public void onTextSend(String msg, boolean danmuOpen) {
        if (msg.length() == 0)
            return;
        try {
            byte[] byte_num = msg.getBytes("utf8");
            if (byte_num.length > 160) {
                Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("我:");
        entity.setContext(msg);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (danmuOpen) {
            if (mDanmuMgr != null) {
                mDanmuMgr.addDanmu(TCUserInfoMgr.getInstance().getHeadPic(), TCUserInfoMgr.getInstance().getNickname(), msg);
            }
            mTCChatRoomMgr.sendDanmuMessage(msg);
        } else {
            mTCChatRoomMgr.sendTextMessage(msg);
        }

    }

    private Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }

    /**
     * 记时器
     */
    private class BroadcastTimerTask extends TimerTask {
        public void run() {
            //Log.i(TAG, "timeTask ");
            ++mSecond;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mTCSwipeAnimationController.isMoving())
                        mBroadcastTime.setText(TCUtils.formattedTime(mSecond));
                }
            });
//            if (MySelfInfo.getInstance().getIdStatus() == TCConstants.HOST)
//                mHandler.sendEmptyMessage(UPDAT_WALL_TIME_TIMER_TASK);
        }
    }

    public void showDetailDialog() {
        //确认则显示观看detail
        stopRecordAnimation();
        DetailDialogFragment dialogFragment = new DetailDialogFragment();
        Bundle args = new Bundle();
        args.putString("time", TCUtils.formattedTime(mSecond));
        args.putString("heartCount", String.format(Locale.CHINA, "%d", lHeartCount));
        args.putString("totalMemberCount", String.format(Locale.CHINA, "%d", lTotalMemberCount));
        dialogFragment.setArguments(args);
        dialogFragment.setCancelable(false);
        if (dialogFragment.isAdded())
            dialogFragment.dismiss();
        else
            dialogFragment.show(getFragmentManager(), "");

    }

    /**
     * 显示确认消息
     *
     * @param msg     消息内容
     * @param isError true错误消息（必须退出） false提示消息（可选择是否退出）
     */
    public void showComfirmDialog(String msg, Boolean isError) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.ConfirmDialogStyle);
        builder.setCancelable(true);
        builder.setTitle(msg);

        if (!isError) {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    stopPublish();
                    quitRoom();
                    stopRecordAnimation();
                    showDetailDialog();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
            //当情况为错误的时候，直接停止推流
            stopPublish();
            quitRoom();
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    stopRecordAnimation();
                    showDetailDialog();
                }
            });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }


    /**
     * 退出房间
     * 包含后台退出与IMSDK房间退出操作
     */
    public void quitRoom() {

        mTCChatRoomMgr.deleteGroup();
        mTCPusherMgr.changeLiveStatus(mUserId, TCPusherMgr.TCLiveStatus_Offline);
    }

    @Override
    public void onBackPressed() {

        showComfirmDialog(TCConstants.TIPS_MSG_STOP_PUSH, false);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDanmuMgr != null) {
            mDanmuMgr.resume();
        }
        mTXCloudVideoView.onResume();

        if (mPasuing) {
            mPasuing = false;

            if (mTXLivePusher != null) {
                mTXLivePusher.resumePusher();
            }
        }

        if (mTXLivePusher != null) {
            mTXLivePusher.resumeBGM();
        }

        if (mSharedNotPublished)
            startPublish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mDanmuMgr != null) {
            mDanmuMgr.pause();
        }
        mTXCloudVideoView.onPause();
        if (mTXLivePusher != null) {
            mTXLivePusher.pauseBGM();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();

        mPasuing = true;
        if (mTXLivePusher != null) {
//            mTXLivePusher.stopCameraPreview(false);
            mTXLivePusher.pausePusher();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mDanmuMgr != null) {
            mDanmuMgr.destroy();
            mDanmuMgr = null;
        }
        stopRecordAnimation();
        mTXCloudVideoView.onDestroy();

        stopPublish();
        mTCChatRoomMgr.removeMsgListener();
        mTCPusherMgr.setPusherListener(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_cam:
                mTXLivePusher.switchCamera();
                break;
            case R.id.flash_btn:
                if (!mTXLivePusher.turnOnFlashLight(!mFlashOn)) {
                    Toast.makeText(getApplicationContext(), "打开闪光灯失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                mFlashOn = !mFlashOn;
                mFlashView.setBackgroundDrawable(mFlashOn ?
                        getResources().getDrawable(R.drawable.icon_flash_pressed) :
                        getResources().getDrawable(R.drawable.icon_flash));

                break;
            case R.id.beauty_btn:
                if (mBeautyDialogFragment.isAdded())
                    mBeautyDialogFragment.dismiss();
                else
                    mBeautyDialogFragment.show(getFragmentManager(), "");
                break;
            case R.id.btn_close:
                showComfirmDialog(TCConstants.TIPS_MSG_STOP_PUSH, false);
//                for(int i = 0; i< 100; i++)
//                    mHeartLayout.addFavor();
                break;
            case R.id.btn_message_input:
                showInputMsgDialog();
                break;
            case R.id.btn_audio_ctrl:
                if(null != mAudioCtrl ) {
                    mAudioCtrl.setVisibility(mAudioCtrl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                }
                break;
            case R.id.btn_audio_effect:
                mAudioCtrl.setVisibility(mAudioCtrl.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_audio_close:
                mAudioCtrl.stopBGM();
                mAudioPluginLayout.setVisibility(View.GONE);
                mAudioCtrl.setVisibility(View.GONE);
                break;
            case R.id.btn_log:
                showLog();
                break;
            default:
                //mLayoutFaceBeauty.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * 发消息弹出框
     */
    private void showInputMsgDialog() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mInputTextMsgDialog.getWindow().getAttributes();

        lp.width = (int) (display.getWidth()); //设置宽度
        mInputTextMsgDialog.getWindow().setAttributes(lp);
        mInputTextMsgDialog.setCancelable(true);
        mInputTextMsgDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mInputTextMsgDialog.show();
    }

    @Override
    protected void showErrorAndQuit(String errorMsg) {

        mTXCloudVideoView.onPause();
        stopPublish();
        quitRoom();
        stopRecordAnimation();

        super.showErrorAndQuit(errorMsg);

    }

    @Override
    public void onPushEvent(int event, Bundle bundle) {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(null,bundle,event);
        }
        if (event < 0) {
            if (event == TXLiveConstants.PUSH_ERR_NET_DISCONNECT) {//网络断开，弹对话框强提醒，推流过程中直播中断需要显示直播信息后退出
                showComfirmDialog(TCConstants.ERROR_MSG_NET_DISCONNECTED, true);
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_CAMERA_FAIL) {//未获得摄像头权限，弹对话框强提醒，并退出
                showErrorAndQuit(TCConstants.ERROR_MSG_OPEN_CAMERA_FAIL);
            } else if (event == TXLiveConstants.PUSH_ERR_OPEN_MIC_FAIL || event == TXLiveConstants.PUSH_ERR_MIC_RECORD_FAIL) { //未获得麦克风权限，弹对话框强提醒，并退出
                Toast.makeText(getApplicationContext(), bundle.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
                showErrorAndQuit(TCConstants.ERROR_MSG_OPEN_MIC_FAIL);
            } else {
                //其他错误弹Toast弱提醒，并退出
                Toast.makeText(getApplicationContext(), bundle.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();

                mTXCloudVideoView.onPause();
                TCPusherMgr.getInstance().changeLiveStatus(mUserId, TCPusherMgr.TCLiveStatus_Offline);
                stopRecordAnimation();
                finish();
            }
        }

        if (event == TXLiveConstants.PUSH_WARNING_HW_ACCELERATION_FAIL) {
            Log.d(TAG, "当前机型不支持视频硬编码");
            mTXPushConfig.setVideoResolution(TXLiveConstants.VIDEO_RESOLUTION_TYPE_360_640);
            mTXPushConfig.setVideoBitrate(700);
            mTXPushConfig.setHardwareAcceleration(TXLiveConstants.ENCODE_VIDEO_SOFTWARE);
            mTXLivePusher.setConfig(mTXPushConfig);
        }

        if (event == TXLiveConstants.PUSH_EVT_PUSH_BEGIN) {
            TCPusherMgr.getInstance().changeLiveStatus(mUserId, TCPusherMgr.TCLiveStatus_Online);
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.setLogText(bundle,null,0);
        }
    }

    private void notifyMsg(final TCChatEntity entity) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
//                if(entity.getType() == TCConstants.PRAISE) {
//                    if(mArrayListChatEntity.contains(entity))
//                        return;
//                }

                if (mArrayListChatEntity.size() > 1000)
                {
                    while (mArrayListChatEntity.size() > 900)
                    {
                        mArrayListChatEntity.remove(0);
                    }
                }
                
                mArrayListChatEntity.add(entity);
                mChatMsgListAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 向服务器获取推流地址 回调
     *
     * @param errCode   错误码，0表示获取成功
     * @param groupId   群ID
     * @param pusherUrl 推流地址
     * @param timeStamp 时间戳
     */
    @Override
    public void onGetPusherUrl(int errCode, String groupId, String pusherUrl, String timeStamp) {
        if (errCode == 0) {
            mPushUrl = pusherUrl;
            startRecordAnimation();
            // 友盟的分享组件并不完善，为了各种异常情况下正常推流，要多做一些事情
            if (mShare_meidia == SHARE_MEDIA.MORE) {
                startPublish();
            } else {
                startShare(timeStamp);

                boolean isSupportShare = false;
                if (mShare_meidia == SHARE_MEDIA.SINA) {
                    isSupportShare = true;
                } else if (mShare_meidia == SHARE_MEDIA.QZONE) {
                    if (UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QQ) || UMShareAPI.get(this).isInstall(this, SHARE_MEDIA.QZONE)) {
                        isSupportShare = true;
                    }
                } else if (UMShareAPI.get(this).isInstall(this, mShare_meidia)) {
                    isSupportShare = true;
                }
                if (isSupportShare) {
                    startPublish();
                }
            }
        } else {
            if (null == groupId) {
                showErrorAndQuit(TCConstants.ERROR_MSG_CREATE_GROUP_FAILED + errCode);
            } else {
                showErrorAndQuit(TCConstants.ERROR_MSG_GET_PUSH_URL_FAILED + errCode);
            }
        }
    }

    private void startShare(final String timeStamp) {
        ShareAction shareAction = new ShareAction(TCLivePublisherActivity.this);
        try {
            mShareUrl = mShareUrl + "?sdkappid=" + java.net.URLEncoder.encode(String.valueOf(TCConstants.IMSDK_APPID), "utf-8")
                    + "&acctype=" + java.net.URLEncoder.encode(String.valueOf(TCConstants.IMSDK_ACCOUNT_TYPE), "utf-8")
                    + "&userid=" + java.net.URLEncoder.encode(mUserId, "utf-8")
                    + "&type=0"
                    + "&ts=" + java.net.URLEncoder.encode(timeStamp, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        UMWeb web = new UMWeb(mShareUrl);
        if (mCoverPicUrl.isEmpty()) {
            web.setThumb(new UMImage(TCLivePublisherActivity.this.getApplicationContext(), R.drawable.bg));
        } else {
            web.setThumb(new UMImage(TCLivePublisherActivity.this.getApplicationContext(), mCoverPicUrl));
        }
        web.setTitle(mTitle);
        shareAction.withText(mNickName + "正在直播");
        shareAction.withMedia(web);
        shareAction.setCallback(umShareListener);

        shareAction.setPlatform(mShare_meidia).share();
        mSharedNotPublished = true;
    }


    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
        }

        @Override
        public void onResult(SHARE_MEDIA platform) {
            Log.d("plat", "platform" + platform);
            Toast.makeText(TCLivePublisherActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            //开启推流
            TCLivePublisherActivity.this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mSharedNotPublished)
                        startPublish();
                }
            });
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(TCLivePublisherActivity.this,"分享失败"+t.getMessage(),Toast.LENGTH_LONG).show();
            //开启推流
            TCLivePublisherActivity.this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mSharedNotPublished)
                     startPublish();
                }
            });
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(TCLivePublisherActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
            //开启推流
            TCLivePublisherActivity.this.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mSharedNotPublished)
                        startPublish();
                }
            });
        }
    };

    @Override
    public void onChangeLiveStatus(int errCode) {
        Log.d(TAG, "onChangeLiveStatus:" + errCode);
    }

    @Override
    public void onJoinGroupCallback(int code, String msg) {
        if (0 == code) {
            //获取推流地址
            Log.d(TAG, "onJoin group success" + msg);
            mTCPusherMgr.getPusherUrl(mUserId, msg, mTitle, mCoverPicUrl, mNickName, mHeadPicUrl, mLocation);
        } else if (TCConstants.NO_LOGIN_CACHE == code) {
            TXLog.d(TAG, "onJoin group failed" + msg);
            showErrorAndQuit(TCConstants.ERROR_MSG_NO_LOGIN_CACHE);
        } else {
            TXLog.d(TAG, "onJoin group failed" + msg);
            showErrorAndQuit(TCConstants.ERROR_MSG_JOIN_GROUP_FAILED + code);
        }
    }

    /**
     * 消息发送回调
     *
     * @param errCode    错误码，0代表发送成功
     * @param timMessage 发送的TIM消息
     */
    @Override
    public void onSendMsgCallback(int errCode, TIMMessage timMessage) {
        if (timMessage != null)
            if (errCode == 0) {
                TIMElemType elemType = timMessage.getElement(0).getType();
                if (elemType == TIMElemType.Text) {
                    //发送文本消息成功
                    Log.d(TAG, "onSendTextMsgsuccess:" + errCode);
                } else if (elemType == TIMElemType.Custom) {
                    Log.d(TAG, "onSendCustomMsgsuccess:" + errCode);
                }

            } else {
                Log.d(TAG, "onSendMsgfail:" + errCode + " msg:" + timMessage.getMsgId());
            }

    }

    @Override
    public void onReceiveMsg(int type, TCSimpleUserInfo userInfo, String content) {
        switch (type) {
            case TCConstants.IMCMD_ENTER_LIVE:
                handleMemberJoinMsg(userInfo);
                break;
            case TCConstants.IMCMD_EXIT_LIVE:
                handleMemberQuitMsg(userInfo);
                break;
            case TCConstants.IMCMD_PRAISE:
                handlePraiseMsg(userInfo);
                break;
            case TCConstants.IMCMD_PAILN_TEXT:
                handleTextMsg(userInfo, content);
                break;
            case TCConstants.IMCMD_DANMU:
                handleDanmuMsg(userInfo, content);
                break;
            default:
                break;
        }
    }

    public void handleTextMsg(TCSimpleUserInfo userInfo, String text) {
        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContext(text);
        entity.setType(TCConstants.TEXT_TYPE);

        notifyMsg(entity);
    }

    public void handleMemberJoinMsg(TCSimpleUserInfo userInfo) {

        //更新头像列表 返回false表明已存在相同用户，将不会更新数据
        if (!mAvatarListAdapter.addItem(userInfo))
            return;

        lTotalMemberCount++;
        lMemberCount++;
        mMemberCount.setText(String.format(Locale.CHINA, "%d", lMemberCount));

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (userInfo.nickname.equals(""))
            entity.setContext(userInfo.userid + "加入直播");
        else
            entity.setContext(userInfo.nickname + "加入直播");
        entity.setType(TCConstants.MEMBER_ENTER);
        notifyMsg(entity);
    }

    public void handleMemberQuitMsg(TCSimpleUserInfo userInfo) {
        if (lMemberCount > 0)
            lMemberCount--;
        else
            Log.d(TAG, "接受多次退出请求，目前人数为负数");
        mMemberCount.setText(String.format(Locale.CHINA, "%d", lMemberCount));

        mAvatarListAdapter.removeItem(userInfo.userid);

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (userInfo.nickname.equals(""))
            entity.setContext(userInfo.userid + "退出直播");
        else
            entity.setContext(userInfo.nickname + "退出直播");
        entity.setType(TCConstants.MEMBER_EXIT);
        notifyMsg(entity);
    }

    @Override
    public void triggerSearch(String query, Bundle appSearchData) {
        super.triggerSearch(query, appSearchData);
    }

    public void handlePraiseMsg(TCSimpleUserInfo userInfo) {

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName("通知");
        if (userInfo.nickname.equals(""))
            entity.setContext(userInfo.userid + "点了个赞");
        else
            entity.setContext(userInfo.nickname + "点了个赞");

        mHeartLayout.addFavor();
        lHeartCount++;

        //todo：修改显示类型
        entity.setType(TCConstants.PRAISE);
        notifyMsg(entity);
    }

    public void handleDanmuMsg(TCSimpleUserInfo userInfo, String text) {

        TCChatEntity entity = new TCChatEntity();
        entity.setSenderName(userInfo.nickname);
        entity.setContext(text);
        entity.setType(TCConstants.TEXT_TYPE);
        notifyMsg(entity);

        if (mDanmuMgr != null) {
            mDanmuMgr.addDanmu(userInfo.headpic, userInfo.nickname, text);
        }
    }

    @Override
    public void onGroupDelete() {
        //useless
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (null != mAudioCtrl && mAudioCtrl.getVisibility() != View.GONE && ev.getRawY() < mAudioCtrl.getTop()) {
            mAudioCtrl.setVisibility(View.GONE);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {//是否选择，没选择就不会继续
            if (requestCode == mAudioCtrl.REQUESTCODE) {
                if (data == null) {
                    Log.e(TAG, "null data");
                } else {
                    Uri uri = data.getData();//得到uri，后面就是将uri转化成file的过程。
                    if (mAudioCtrl != null) {
                        mAudioCtrl.processActivityResult(uri);
                    } else {
                        Log.e(TAG, "NULL Pointer! Get Music Failed");
                    }
                }
            }
        }
    }

    protected void showLog() {
        mShowLog = !mShowLog;
        if (mTXCloudVideoView != null) {
            mTXCloudVideoView.disableLog(!mShowLog);
        }
        Button liveLog = (Button) findViewById(R.id.btn_log);
        if (mShowLog) {
            if (liveLog != null) liveLog.setBackgroundResource(R.drawable.icon_log_on);

        } else {
            if (liveLog != null) liveLog.setBackgroundResource(R.drawable.icon_log_off);
        }
    }

    @Override
    public void onBeautyParamsChange(BeautyDialogFragment.BeautyParams params, int key) {
        switch (key){
            case BeautyDialogFragment.BEAUTYPARAM_BEAUTY:
            case BeautyDialogFragment.BEAUTYPARAM_WHITE:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setBeautyFilter(params.mBeautyProgress, params.mWhiteProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_FACE_LIFT:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setFaceSlimLevel(params.mFaceLiftProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_BIG_EYE:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setEyeScaleLevel(params.mBigEyeProgress);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_FILTER:
                if (mTXLivePusher != null) {
                    mTXLivePusher.setFilter(TCUtils.getFilterBitmap(getResources(), params.mFilterIdx));
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_MOTION_TMPL:
                if (mTXLivePusher != null){
                    mTXLivePusher.setMotionTmpl(params.mMotionTmplPath);
                }
                break;
            case BeautyDialogFragment.BEAUTYPARAM_GREEN:
                if (mTXLivePusher != null){
                    mTXLivePusher.setGreenScreenFile(TCUtils.getGreenFileName(params.mGreenIdx));
                }
                break;
            default:
                break;
        }
    }
}
