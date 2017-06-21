package com.example.live;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:  Administrator
 * Date:    2017/5/11
 * Brief:   权限信息
 */

public class AuthorityInfo {
    public String userId = "";              // 用户Id
    public int ChannelId = 0;                  // 频道ID
    public boolean IsWords = false;         // true=禁言
    public boolean IsIn = false;            // true=踢出
    public boolean IsManage = false;        // true=管理

    public AuthorityInfo(JSONObject obj) throws JSONException {
        userId = obj.getString("userId");
        ChannelId = obj.getInt("ChannelId");
        IsWords = obj.getBoolean("IsWords");
        IsIn = obj.getBoolean("IsIn");
        IsManage = obj.getBoolean("IsManage");
    }
}
