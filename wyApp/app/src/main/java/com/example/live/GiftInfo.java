package com.example.live;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:  Administrator
 * Date:    2017/5/11
 * Brief:   礼物信息
 */

public class GiftInfo {
    public int Id = 0;                     // 标识
    public String Name = "";               // 礼物名称
    public String Pic = "";                // 礼物图片地址
    public int TYPE = 0;                   // 类型
    public double Money = 0;                  // 积分/钱
    public boolean IsValid = true;         // 是否有效
    public String CreatedDate = "";           // 日期

    public GiftInfo(JSONObject obj) throws JSONException {
        Id = obj.getInt("Id");
        Name = obj.getString("Name");
        Pic = obj.getString("Pic");
        TYPE = obj.getInt("TYPE");
        Money = obj.getDouble("Money");
        IsValid = obj.getBoolean("IsValid");
        CreatedDate = obj.getString("CreatedDate");
    }
}
