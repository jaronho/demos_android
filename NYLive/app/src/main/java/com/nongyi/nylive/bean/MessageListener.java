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
        Log.d("NYLive", "onNewTextMsg ==> SenderId: "+SenderId+", text: "+text.getText());
        ChatMessage cm = new ChatMessage();
        cm.setSenderId(SenderId);
        cm.setName(userProfile.getNickName());
        cm.setContent(text.getText());
        EventCenter.post(Constants.EVENT_NEW_TEXT_MSG, cm);
    }

    @Override
    public void onNewCustomMsg(ILVCustomCmd cmd, String id, TIMUserProfile userProfile) {
        Log.d("NYLive", "onNewCustomMsg ==> id: "+id+", text: "+cmd.getText()+", cmd: "+cmd.getCmd());
        switch (cmd.getCmd()) {
            case 1: // 有观众进入房间
                GuestInfo enterGuestInfo = new GuestInfo();
                enterGuestInfo.setId(id);
                enterGuestInfo.setName(userProfile.getNickName());
                EventCenter.post(Constants.EVENT_GUEST_ENTER_ROOM, enterGuestInfo);
                break;
            case 2: // 有观众离开房间
                GuestInfo leaveGuestInfo = new GuestInfo();
                leaveGuestInfo.setId(id);
                leaveGuestInfo.setName(userProfile.getNickName());
                EventCenter.post(Constants.EVENT_GUEST_LEAVE_ROOM, leaveGuestInfo);
                break;
        }
    }

    @Override
    public void onNewOtherMsg(TIMMessage message) {
        Log.d("NYLive", "onNewOtherMsg ==> sender: "+message.getSender());
    }
}
