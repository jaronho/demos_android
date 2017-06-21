package com.example.live;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:  Administrator
 * Date:    2017/5/11
 * Brief:   用户信息
 */

public class UserInfo {
    public String UserId = "";              // 用户ID
    public String Name = "";               // 用户昵称
    public String ProfilePicture = "";      // 用户头像
    public int CustomType = 0;               // 额外的自定义类型

    public UserInfo(JSONObject obj) throws JSONException {
        Name = obj.getString("Name");
        UserId = obj.getString("UserId");
        ProfilePicture = obj.getString("ProfilePicture");
    }

    public UserInfo() {}
}
