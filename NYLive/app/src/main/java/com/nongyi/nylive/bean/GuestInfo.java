package com.nongyi.nylive.bean;

/**
 * Author:  Administrator
 * Date:    2017/5/4
 * Brief:   观众信息
 */

public class GuestInfo {
    private String mId = "";        // 观众id
    private String mName = "";  // 昵称
    private String mFaceUrl = "";   // 头像

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getFaceUrl() {
        return mFaceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        mFaceUrl = faceUrl;
    }
}
