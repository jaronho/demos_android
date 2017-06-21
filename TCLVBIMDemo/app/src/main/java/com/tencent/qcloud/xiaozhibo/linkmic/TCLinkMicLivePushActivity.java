package com.tencent.qcloud.xiaozhibo.linkmic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.tencent.qcloud.xiaozhibo.R;
import com.tencent.qcloud.xiaozhibo.common.utils.TCConstants;
import com.tencent.qcloud.xiaozhibo.play.TCPlayerMgr;
import com.tencent.qcloud.xiaozhibo.push.camera.TCLivePublisherActivity;
import com.tencent.rtmp.TXLiveConstants;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by dennyfeng on 16/11/16.
 */
public class TCLinkMicLivePushActivity extends TCLivePublisherActivity implements TCLinkMicMgr.TCLinkMicListener, TCLivePlayListenerImpl.ITCLivePlayListener{

    private static final String TAG = TCLinkMicLivePushActivity.class.getName();

    private static final int  MAX_LINKMIC_MEMBER_SUPPORT = 3;

    private String                      mSessionID;
    private boolean                     mHasPendingRequest = false;

    private Vector<TCLinkMicPlayItem>   mVecPlayItems = new Vector<>();
    private Map<String, String>         mMapLinkMicMember = new HashMap<>();

    private TCLinkMicMgr                mTCLinkMicMgr;

    private boolean                     mNeedResetVideoQuality = false;

    @Override
    protected void initView() {
        super.initView();

        mTCLinkMicMgr = TCLinkMicMgr.getInstance();
        mTCLinkMicMgr.setLinkMicListener(this);

        if (TCLinkMicMgr.supportLinkMic() == false) {
            return;
        }

        mSessionID = getLinkMicSessionID();

        TCLinkMicPlayItem item1 = new TCLinkMicPlayItem(this, this, mShowLog, 1);
        TCLinkMicPlayItem item2 = new TCLinkMicPlayItem(this, this, mShowLog, 2);
        TCLinkMicPlayItem item3 = new TCLinkMicPlayItem(this, this, mShowLog, 3);
        item1.setmTimeOutRunnable(new TCLinkMicTimeoutRunnable());
        item2.setmTimeOutRunnable(new TCLinkMicTimeoutRunnable());
        item3.setmTimeOutRunnable(new TCLinkMicTimeoutRunnable());
        mVecPlayItems.add(item1);
        mVecPlayItems.add(item2);
        mVecPlayItems.add(item3);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mNeedResetVideoQuality) {
                    if (mMapLinkMicMember.size() == 0) {
                        if (mTXLivePusher != null) {
                            mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
                        }
                    }
                    mNeedResetVideoQuality = false;
                }
            }
        }, 1000);


        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.resume();
            if (!TextUtils.isEmpty(item.mPlayUrl) && !TextUtils.isEmpty(item.mUserID)) {
                item.startLoading();
                item.startPlay(item.mPlayUrl);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.stopPlay(true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.destroy();
        }

        mVecPlayItems.clear();

        if (mTCLinkMicMgr != null) {
            mTCLinkMicMgr.setLinkMicListener(null);
            mTCLinkMicMgr = null;
        }
    }

    @Override
    protected void startPublish() {
        //兼容大主播是新版本，小主播是旧版本的情况：“新版本大主播推流地址后面带上&mix=session_id:xxx”
        //1、采用这种方式，保证旧版本小主播可以拉到大主播的加速地址
        //2、新版本大主播拉小主播的加速地址，有没有session_id都没有影响
        //3、新版本混流由大主播调用CGI的方式启动混流，可以正常混流
        mPushUrl = mPushUrl + "&mix=session_id:" + mSessionID;

        TCStreamMergeMgr.getInstance().setMainVideoStream(mPushUrl);

        super.startPublish();
    }

    @Override
    protected void stopPublish() {
        super.stopPublish();

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            item.stopPlay(false);
        }

        //混流：清除状态
        TCStreamMergeMgr.getInstance().resetMergeState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_kick_out1:
            case R.id.btn_kick_out2:
            case R.id.btn_kick_out3:
                for (int i = 0; i < mVecPlayItems.size(); ++i) {
                    TCLinkMicPlayItem item = mVecPlayItems.get(i);
                    if (item.getKickoutBtnId() == v.getId()) {

                        //混流：减少一路
                        TCStreamMergeMgr.getInstance().delSubVideoStream(item.mPlayUrl);

                        //通知其它小主播：有小主播退出连麦
                        broadcastMemberExitNotify(item.mUserID);

                        //清理数据
                        mTCLinkMicMgr.kickOutLinkMicMember(item.mUserID);
                        mMapLinkMicMember.remove(item.mUserID);

                        //结束播放
                        item.stopPlay(true);
                        item.empty();

                        if (mMapLinkMicMember.size() == 0) {
                            //无人连麦，设置视频质量：高清
                            mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
                        }

                        break;
                    }
                }
                break;

            default:
                super.onClick(v);
        }
    }

    private void handleLinkMicFailed(TCLinkMicPlayItem item, String message) {
        if (item == null) {
            return;
        }

        if (item.mPending == true) {
            mHandler.removeCallbacks(item.getTimeOutRunnable());
        }

        mTCLinkMicMgr.kickOutLinkMicMember(item.mUserID);
        mMapLinkMicMember.remove(item.mUserID);

        item.stopPlay(true);
        item.empty();

        //无人连麦，设置视频质量：高清
        if (mMapLinkMicMember.size() == 0) {
            if (mPasuing == false) {
                mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
            }
            else {
                mNeedResetVideoQuality = true;
            }
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceiveLinkMicRequest(final String strUserId, final String strNickName) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.ConfirmDialogStyle)
                .setCancelable(true)
                .setTitle("提示")
                .setMessage(strNickName + "向您发起连麦请求")
                .setPositiveButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTCLinkMicMgr.sendLinkMicResponse(strUserId, TCConstants.LINKMIC_RESPONSE_TYPE_ACCEPT, createResponseParam(TCConstants.LINKMIC_RESPONSE_TYPE_ACCEPT, mSessionID));

                        for (TCLinkMicPlayItem item: mVecPlayItems) {
                            if (item.mUserID == null || item.mUserID.length() == 0) {
                                item.mUserID = strUserId;
                                item.mPending = true;
                                item.startLoading();

                                //设置超时逻辑
                                TCLinkMicTimeoutRunnable timeoutRunnable = (TCLinkMicTimeoutRunnable)(item.getTimeOutRunnable());
                                timeoutRunnable.setUserID(strUserId);
                                mHandler.removeCallbacks(timeoutRunnable);
                                mHandler.postDelayed(timeoutRunnable, 15000);

                                //加入连麦成员列表
                                mMapLinkMicMember.put(strUserId, "");

                                //第一个小主播加入连麦，设置视频质量：连麦大主播
                                if (mMapLinkMicMember.size() == 1) {
                                    mNeedResetVideoQuality = false;
                                    mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_LINKMIC_MAIN_PUBLISHER);
                                }

                                break;
                            }
                        }

                        dialog.dismiss();
                        mHasPendingRequest = false;
                    }
                })
                .setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mTCLinkMicMgr.sendLinkMicResponse(strUserId, TCConstants.LINKMIC_RESPONSE_TYPE_REJECT, createResponseParam(TCConstants.LINKMIC_RESPONSE_TYPE_REJECT, "主播拒绝了您的连麦请求"));
                        dialog.dismiss();
                        mHasPendingRequest = false;
                    }
                });

        mHandler.post(new Runnable() {
            @Override
            public void run() {

                if (mMapLinkMicMember.size() >= MAX_LINKMIC_MEMBER_SUPPORT) {
                    mTCLinkMicMgr.sendLinkMicResponse(strUserId, TCConstants.LINKMIC_RESPONSE_TYPE_REJECT, createResponseParam(TCConstants.LINKMIC_RESPONSE_TYPE_REJECT, "主播端连麦人数超过最大限制"));
                    return;
                } else if (mHasPendingRequest == true) {
                    mTCLinkMicMgr.sendLinkMicResponse(strUserId, TCConstants.LINKMIC_RESPONSE_TYPE_REJECT, createResponseParam(TCConstants.LINKMIC_RESPONSE_TYPE_REJECT, "请稍后，主播正在处理其它人的连麦请求"));
                    return;
                }

                final AlertDialog alertDialog = builder.create();
                alertDialog.setCancelable(false);
                alertDialog.setCanceledOnTouchOutside(false);
                alertDialog.show();

                mHasPendingRequest = true;

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        alertDialog.dismiss();
                        mHasPendingRequest = false;
                    }
                }, 10000);
            }
        });
    }

    @Override
    public void onReceiveLinkMicResponse(final String strUserId, final int result, final String strSessionId) {
    }

    @Override
    public void onReceiveKickedOutNotify() {
    }

    @Override
    public void onReceiveMemberJoinNotify(final String strUserID, final String strPlayUrl) {
        if (TextUtils.isEmpty(strUserID) || TextUtils.isEmpty(strPlayUrl)) {
            return;
        }

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (!mMapLinkMicMember.containsKey(strUserID)) {
                    return;
                }

                TCLinkMicPlayItem item = getPlayItemByUserID(strUserID);
                if (item == null) {
                    return;
                }

                mMapLinkMicMember.put(strUserID, strPlayUrl);

                if (item.mPlayUrl == null || item.mPlayUrl.length() == 0) {
                    TCPlayerMgr.getInstance().getPlayUrlWithSignature(mUserId, strPlayUrl, new TCPlayerMgr.OnGetPlayUrlWithSignature() {
                        @Override
                        public void onGetPlayUrlWithSignature(int errCode, String playUrl) {
                            TCLinkMicPlayItem playItem = getPlayItemByUserID(strUserID);
                            if (playItem != null) {
                                if (errCode == 0 && !TextUtils.isEmpty(playUrl)) {
                                    if (!mPasuing) {
                                        playItem.clearLog();
                                        playItem.startPlay(playUrl);
                                    }
                                } else {
                                    handleLinkMicFailed(playItem, "获取拉流防盗链key失败");
                                }
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onReceiveMemberExitNotify(final String strUserID) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TCLinkMicPlayItem item = getPlayItemByUserID(strUserID);
                if (item == null) {
                    return;
                }

                if (item.mPending == true) {
                    mHandler.removeCallbacks(item.getTimeOutRunnable());
                }

                //混流：减少一路
                TCStreamMergeMgr.getInstance().delSubVideoStream(item.mPlayUrl);

                //通知其它小主播：有小主播退出连麦
                broadcastMemberExitNotify(item.mUserID);

                //结束播放
                item.stopPlay(true);
                item.empty();

                //清理数据
                mMapLinkMicMember.remove(strUserID);

                //无人连麦，设置视频质量：高清
                if (mMapLinkMicMember.size() == 0) {
                    if (mPasuing == false) {
                        mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
                    } else {
                        mNeedResetVideoQuality = true;
                    }
                }
            }
        });
    }

    @Override
    public void onLivePlayEvent(String playUrl, int event, Bundle bundle) {
        TCLinkMicPlayItem item = getPlayItemByPlayUrl(playUrl);
        if (item == null) {
            return;
        }

        item.setLogText(null, bundle, event);

        if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END || event == TXLiveConstants.PLAY_ERR_GET_RTMP_ACC_URL_FAIL) {
            if (item.mPending == true) {
                handleLinkMicFailed(item, "拉流失败，结束连麦");
            }
            else {
                handleLinkMicFailed(item, "连麦观众视频断流，结束连麦");

                //混流：减少一路
                TCStreamMergeMgr.getInstance().delSubVideoStream(playUrl);
            }
        }
        else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
            item.mPending = false;

            //结束loading
            item.stopLoading(true);

            //混流：增加一路
            TCStreamMergeMgr.getInstance().addSubVideoStream(item.mPlayUrl);

            //通知其它小主播：有新的小主播加入连麦
            broadcastMemberJoinNotify(item.mUserID, item.mPlayUrl);
        }
    }

    @Override
    public void onLivePlayNetStatus(String playUrl, Bundle status) {
        TCLinkMicPlayItem item = getPlayItemByPlayUrl(playUrl);
        if (item != null) {
            item.setLogText(status, null, 0);
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {
        int width = bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_WIDTH);
        int height = bundle.getInt(TXLiveConstants.NET_STATUS_VIDEO_HEIGHT);
        TCStreamMergeMgr.getInstance().setMainVideoStreamResolution(width, height);
        super.onNetStatus(bundle);
    }

    private String getLinkMicSessionID() {
        //说明：
        //1.sessionID是混流依据，sessionID相同的流，后台混流Server会混为一路视频流；因此，sessionID必须全局唯一

        //2.直播码频道ID理论上是全局唯一的，使用直播码作为sessionID是最为合适的
        //String strSessionID = TCLinkMicMgr.getInstance().getStreamIDByStreamUrl(mPushUrl)

        //3.直播码是字符串，混流Server目前只支持64位数字表示的sessionID，暂时按照下面这种方式生成sessionID
        //  待混流Server改造完成后，再使用直播码作为sessionID

        long timeStamp = System.currentTimeMillis();
        long sessionID = (((long)3891 << 48) | timeStamp);      // 3891是bizid, timeStamp是当前毫秒值
        return String.valueOf(sessionID);
    }

    private TCLinkMicPlayItem getPlayItemByUserID(String strUserID) {
        if (strUserID == null || strUserID.length() == 0) {
            return null;
        }

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            if (strUserID.equalsIgnoreCase(item.mUserID)) {
                return item;
            }
        }

        return null;
    }

    private TCLinkMicPlayItem getPlayItemByPlayUrl(String playUrl) {
        if (playUrl == null || playUrl.length() == 0) {
            return null;
        }

        for (TCLinkMicPlayItem item: mVecPlayItems) {
            if (playUrl.equalsIgnoreCase(item.mPlayUrl)) {
                return item;
            }
        }

        return null;
    }

    private String createResponseParam(int responseType, String param) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (responseType == TCConstants.LINKMIC_RESPONSE_TYPE_ACCEPT) {
                jsonObject.put("sessionID", param);

                JSONArray jsonArray = new JSONArray();
                for (TCLinkMicPlayItem item: mVecPlayItems) {
                    if (item.mUserID != null && item.mUserID.length() > 0 && item.mPlayUrl != null && item.mPlayUrl.length() > 0) {
                        JSONObject stream = new JSONObject();
                        stream.put("userID", item.mUserID);
                        stream.put("playUrl", item.mPlayUrl);
                        jsonArray.put(stream);
                    }
                }

                jsonObject.put("streams", jsonArray);
            }
            else if (responseType == TCConstants.LINKMIC_RESPONSE_TYPE_REJECT) {
                jsonObject.put("reason", param);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    private void broadcastMemberJoinNotify(String userID, String playUrl) {
        //向所有正在和主播连麦的小主播发送通知：有新的小主播加入连麦
        for (TCLinkMicPlayItem item: mVecPlayItems) {
            if (item.mUserID != null && item.mUserID.length() > 0 && item.mPlayUrl != null && item.mPlayUrl.length() > 0) {
                if (item.mUserID.equalsIgnoreCase(userID) == false) {
                    mTCLinkMicMgr.sendMemberJoinNotify(item.mUserID, userID, playUrl);
                }
            }
        }

        //把所有正在连麦的小主播的拉流信息，发送给新加入的连麦者
        for (TCLinkMicPlayItem item: mVecPlayItems) {
            if (item.mUserID != null && item.mUserID.length() > 0 && item.mPlayUrl != null && item.mPlayUrl.length() > 0) {
                if (item.mUserID.equalsIgnoreCase(userID) == false) {
                    mTCLinkMicMgr.sendMemberJoinNotify(userID, item.mUserID, item.mPlayUrl);
                }
            }
        }
    }

    private void broadcastMemberExitNotify(String userID) {
        //向所有正在和主播连麦的小主播发送通知：有小主播退出连麦
        for (TCLinkMicPlayItem item: mVecPlayItems) {
            if (item.mUserID != null && item.mUserID.length() > 0 && item.mPlayUrl != null && item.mPlayUrl.length() > 0) {
                if (item.mUserID.equalsIgnoreCase(userID) == false) {
                    mTCLinkMicMgr.sendMemberExitNotify(item.mUserID, userID);
                }
            }
        }
    }


    protected void showLog() {
        super.showLog();
        for (TCLinkMicPlayItem item : mVecPlayItems) {
            item.showLog(!mShowLog);
        }
    }

    class TCLinkMicTimeoutRunnable implements Runnable {
        private String strUserID = "";

        public void setUserID(String userID) {
            strUserID = userID;
        }

        @Override
        public void run() {
            TCLinkMicPlayItem item = getPlayItemByUserID(strUserID);
            if (item != null && item.mPending == true) {

                mTCLinkMicMgr.kickOutLinkMicMember(strUserID);
                mMapLinkMicMember.remove(strUserID);

                item.stopPlay(true);
                item.empty();

                if (mMapLinkMicMember.size() == 0) {
                    //无人连麦，设置视频质量：高清
                    mTXLivePusher.setVideoQuality(TXLiveConstants.VIDEO_QUALITY_HIGH_DEFINITION);
                }

                Toast.makeText(getApplicationContext(), "连麦超时", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
