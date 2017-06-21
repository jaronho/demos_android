package com.example.live;

import java.util.Date;

/**
 * Author:  Administrator
 * Date:    2017/5/1
 * Brief:   聊天消息
 */

public class ChatInfo {
    public final static int TYPE_TEXT = 1;          // 文本
    public final static int TYPE_FACE = 2;          // 表情
    public final static int TYPE_PHOTO = 3;         // 图片

    public final static int STATE_SENDING = 1;      // 发送中
    public final static int STATE_SUCCESS = 2;      // 发送成功
    public final static int STATE_FAIL = 3;         // 发送失败

    private long mId = 0;                   // 消息id
    private int mGroupId = 0;               // 组id
    private int mType = TYPE_TEXT;          // 内容类型
    private int mState = STATE_SENDING;     // 发送状态
    private String mSenderId = "";          // 发送者id
    private String mName = "";        // 发送者名字
    private String mAvatar = "";            // 发送者头像
    private String mContent = "";           // 聊天内容
    private Date mTime;                     // 发送时间

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getGroupId() {
        return mGroupId;
    }

    public void setGroupId(int id) {
        mGroupId = id;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    public int getState() {
        return mState;
    }

    public void setState(int state) {
        mState = state;
    }

    public String getSenderId() {
        return mSenderId;
    }

    public void setSenderId(String senderId) {
        mSenderId = senderId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent= content;
    }

    public Date getTime() {
        return mTime;
    }

    public void setTime(Date time) {
        mTime = time;
    }
}
