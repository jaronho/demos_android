package com.nongyi.nylive.bean;

import android.util.Log;

import com.jaronho.sdk.library.eventdispatcher.EventCenter;
import com.nongyi.nylive.utils.Constants;
import com.tencent.TIMMessage;
import com.tencent.TIMUserProfile;
import com.tencent.livesdk.ILVCustomCmd;
import com.tencent.livesdk.ILVLiveConfig;
import com.tencent.livesdk.ILVLiveConstants;
import com.tencent.livesdk.ILVText;

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
        ChatMessage cm = new ChatMessage();
        cm.setSenderId(SenderId);
        cm.setName(userProfile.getNickName());
        cm.setContent(text.getText());
        EventCenter.post(Constants.EVENT_NEW_TEXT_MSG, cm);
    }

    @Override
    public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
        Log.d("NYLive", "onNewCustomMsg, " + ILVCustomCmdToString(cmd) + ", id: " + id + ", " + TIMUserProfileToString(userProfile));
        switch (cmd.getCmd()) {
            case 1: // 有观众进入房间
                GuestInfo enterGuestInfo = new GuestInfo();
                enterGuestInfo.setId(id);
                enterGuestInfo.setName(userProfile.getNickName());
                enterGuestInfo.setFaceUrl(userProfile.getFaceUrl());
                EventCenter.post(Constants.EVENT_GUEST_ENTER_ROOM, enterGuestInfo);
                break;
            case 2: // 有观众离开房间
                GuestInfo leaveGuestInfo = new GuestInfo();
                leaveGuestInfo.setId(id);
                leaveGuestInfo.setName(userProfile.getNickName());
                leaveGuestInfo.setFaceUrl(userProfile.getFaceUrl());
                EventCenter.post(Constants.EVENT_GUEST_LEAVE_ROOM, leaveGuestInfo);
                break;
        }
    }

    @Override
    public void onNewOtherMsg(TIMMessage message) {
        Log.d("NYLive", "onNewOtherMsg, " + TIMMessageToString(message));
    }

    private String ILVTextToString(ILVText text) {
        return "text: " + text.getText() + ", destId: " + text.getDestId() + ", type: " + text.getType() + ", priority: " + text.getPriority();
    }

    private String ILVCustomCmdToString(ILVCustomCmd cmd) {
        return "cmd: " + cmd.getCmd() + ", " + ILVTextToString(cmd);
    }

    private String TIMUserProfileToString(TIMUserProfile profile) {
        return "identifier: " + profile.getIdentifier() + ", nickName: " + profile.getNickName()
                + ", allowType: " + profile.getAllowType() + ", remark: " + profile.getRemark()
                + ", faceUrl: " + profile.getFaceUrl() + ", selfSignature: " + profile.getSelfSignature()
                + ", gender: " + profile.getGender() + ", birthday: " + profile.getBirthday()
                + ", language: " + profile.getLanguage() + ", location: " + profile.getLocation();
    }

    private String TIMMessageToString(TIMMessage message) {
        return "isSelf: " + message.isSelf() + ", timestamp: " + message.timestamp() + ", isRead: " + message.isRead()
                + ", status: " + message.status() + ", priority: " + message.getPriority() + ", recvFlag: " + message.getRecvFlag()
                + ", sender: " + message.getSender() + ", msgId: " + message.getMsgId() + ", msgUniqueId: " + message.getMsgUniqueId()
                + ", " + TIMUserProfileToString(message.getSenderProfile()) + ", customInt: " + message.getCustomInt()
                + ", customStr: " + message.getCustomStr() + ", isPeerReaded: " + message.isPeerReaded();
    }
}
