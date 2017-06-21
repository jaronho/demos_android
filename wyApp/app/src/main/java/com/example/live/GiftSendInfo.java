package com.example.live;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Author:  Administrator
 * Date:    2017/5/23
 * Brief:   礼物发送信息
 */

public class GiftSendInfo {
    public String fromId = "";              // 送礼人id
    public String fromNickname = "";        // 送礼人昵称
    public String fromPic = "";             // 送礼人头像
    public String giftId = "";              // 礼物id
    public String giftName = "";            // 礼物名称
    public String giftPic = "";             // 礼物图标
    public String giftNumber = "";          // 礼物数量

    public GiftSendInfo() {}

    public GiftSendInfo(JSONObject obj) throws JSONException {
        fromId = obj.getString("fromId");
        fromNickname = obj.getString("fromNickname");
        fromPic = obj.getString("fromPic");
        giftId = obj.getString("giftId");
        giftName = obj.getString("giftName");
        giftPic = obj.getString("giftPic");
        giftNumber = obj.getString("giftNumber");
    }

    public String toString() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("fromId", fromId);
            obj.put("fromNickname", fromNickname);
            obj.put("fromPic", fromPic);
            obj.put("giftId", giftId);
            obj.put("giftName", giftName);
            obj.put("giftPic", giftPic);
            obj.put("giftNumber", giftNumber);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj.toString();
    }
}
