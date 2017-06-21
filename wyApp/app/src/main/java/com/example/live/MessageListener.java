package com.example.live;

import android.util.Log;

import com.jaronho.sdk.library.eventdispatcher.EventCenter;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVText;

import org.json.JSONException;
import org.json.JSONObject;

import static com.example.live.ConstantsLive.AVICMD_SendAddManager;
import static com.example.live.ConstantsLive.AVICMD_SendDelManager;
import static com.example.live.ConstantsLive.AVICMD_SendForbiddenSpeak;
import static com.example.live.ConstantsLive.AVICMD_SendGift;
import static com.example.live.ConstantsLive.AVICMD_SendRemove;
import static com.example.live.ConstantsLive.AVICMD_SendResumeSpeak;
import static com.example.live.ConstantsLive.AVIMCMD_ENTERLIVE;
import static com.example.live.ConstantsLive.AVIMCMD_EXITLIVE;
import static com.example.live.ConstantsLive.AVIMCMD_PRAISE;
import static com.example.live.ConstantsLive.AVIMCMD_TEXT;

public class MessageListener implements ILVLiveConfig.ILVLiveMsgListener {
    private static MessageListener mInstance = null;

    public static MessageListener getInstance(){
        if (null == mInstance){
            synchronized (MessageListener.class){
                mInstance = new MessageListener();
            }
        }
        return mInstance;
    }

    @Override
    public void onNewTextMsg(ILVText text, String SenderId, TIMUserProfile userProfile) {
        Log.d("NYLive", "onNewTextMsg,  " + ILVTextToString(text) + ", SenderId: " + SenderId + ", " + TIMUserProfileToString(userProfile));
        ChatInfo cm = new ChatInfo();
        cm.setSenderId(SenderId);
        cm.setName(userProfile.getNickName());
        cm.setAvatar(userProfile.getFaceUrl());
        cm.setContent(text.getText());
        EventCenter.post(AVIMCMD_TEXT, cm);
    }

    @Override
    public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
        Log.d("NYLive", "onNewCustomMsg, " + ILVCustomCmdToString(cmd) + ", id: " + id + ", " + TIMUserProfileToString(userProfile));
        switch (cmd.getCmd()) {
            case AVIMCMD_ENTERLIVE: // 有观众进入房间
                UserInfo enterGuestInfo = new UserInfo();
                enterGuestInfo.UserId = userProfile.getIdentifier();
                enterGuestInfo.Name = userProfile.getNickName();
                enterGuestInfo.ProfilePicture = userProfile.getFaceUrl();
                EventCenter.post(AVIMCMD_ENTERLIVE, enterGuestInfo);
                break;
            case AVIMCMD_EXITLIVE: // 有观众离开房间
                UserInfo leaveGuestInfo = new UserInfo();
                leaveGuestInfo.UserId = userProfile.getIdentifier();
                leaveGuestInfo.Name = userProfile.getNickName();
                leaveGuestInfo.ProfilePicture = userProfile.getFaceUrl();
                EventCenter.post(AVIMCMD_EXITLIVE, leaveGuestInfo);
                break;
            case AVIMCMD_PRAISE:    // 点赞
                EventCenter.post(AVIMCMD_PRAISE);
                break;
            case AVICMD_SendForbiddenSpeak: // 禁言
                try {
                    JSONObject wordsObject = new JSONObject(cmd.getParam());
                    EventCenter.post(AVICMD_SendForbiddenSpeak, wordsObject.getString("userid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case AVICMD_SendResumeSpeak:    // 恢复禁言
                try {
                    JSONObject wordsObject = new JSONObject(cmd.getParam());
                    EventCenter.post(AVICMD_SendResumeSpeak, wordsObject.getString("userid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case AVICMD_SendRemove: // 踢出
                try {
                    JSONObject inObject = new JSONObject(cmd.getParam());
                    EventCenter.post(AVICMD_SendRemove, inObject.getString("userid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case AVICMD_SendAddManager: // 设置为管理员
                try {
                    JSONObject managerObject = new JSONObject(cmd.getParam());
                    EventCenter.post(AVICMD_SendAddManager, managerObject.getString("userid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case AVICMD_SendDelManager: // 取消管理
                try {
                    JSONObject managerObject = new JSONObject(cmd.getParam());
                    EventCenter.post(AVICMD_SendDelManager, managerObject.getString("userid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case AVICMD_SendGift:   // 发送礼物
                try {
                    JSONObject giftObject = new JSONObject(cmd.getParam());
                    EventCenter.post(AVICMD_SendGift, new GiftSendInfo(giftObject));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onNewOtherMsg(TIMMessage message) {
        Log.d("NYLive", "onNewOtherMsg, " + TIMMessageToString(message));
    }

    private String ILVTextToString(ILVText text) {
        return null == text ? "" : "text: " + text.getText() + ", destId: " + text.getDestId() + ", type: " + text.getType() + ", priority: " + text.getPriority();
    }

    private String ILVCustomCmdToString(ILVCustomCmd cmd) {
        return null == cmd ? "" : "cmd: " + cmd.getCmd() + ", " + ILVTextToString(cmd);
    }

    private String TIMUserProfileToString(TIMUserProfile profile) {
        return null == profile ? "" : "identifier: " + profile.getIdentifier() + ", nickName: " + profile.getNickName()
                + ", allowType: " + profile.getAllowType() + ", remark: " + profile.getRemark()
                + ", faceUrl: " + profile.getFaceUrl() + ", selfSignature: " + profile.getSelfSignature()
                + ", gender: " + profile.getGender() + ", birthday: " + profile.getBirthday()
                + ", language: " + profile.getLanguage() + ", location: " + profile.getLocation();
    }

    private String TIMMessageToString(TIMMessage message) {
        return null == message ? "" : "isSelf: " + message.isSelf() + ", timestamp: " + message.timestamp() + ", isRead: " + message.isRead()
                + ", status: " + message.status() + ", priority: " + message.getPriority() + ", recvFlag: " + message.getRecvFlag()
                + ", sender: " + message.getSender() + ", msgId: " + message.getMsgId() + ", msgUniqueId: " + message.getMsgUniqueId()
                + ", " + TIMUserProfileToString(message.getSenderProfile()) + ", customInt: " + message.getCustomInt()
                + ", customStr: " + message.getCustomStr() + ", isPeerReaded: " + message.isPeerReaded();
    }
}
