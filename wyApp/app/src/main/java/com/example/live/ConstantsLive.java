package com.example.live;

/**
 * Author:  Administrator
 * Date:    2017/5/6
 * Brief:   ConstantsLive
 */

public class ConstantsLive {
    // ILive
    public static final int APP_ID = 1400027763;
    public static final int ACCOUNT_TYPE = 11667;

    public static final String APP_KEY = "AndroidAppKey";
    public static final String APP_SECRET = "62E226E5-48F1-485F-BA61-844EEE7F115A";

    public static final int AVIMCMD_TEXT = -1;         // 普通的聊天消息
    public static final int AVIMCMD_NONE = AVIMCMD_TEXT + 1;               // 无事件

    // 以下事件为TCAdapter内部处理的通用事件
    public static final int AVIMCMD_ENTERLIVE = AVIMCMD_NONE + 1;          // 用户加入直播
    public static final int AVIMCMD_EXITLIVE = AVIMCMD_ENTERLIVE + 1;         // 用户退出直播
    public static final int AVIMCMD_PRAISE = AVIMCMD_EXITLIVE + 1;           // 点赞消息

    public static final int AVICMD_SendGift = 2100;                             // 发送礼物
    public static final int AVICMD_SendForbiddenSpeak = 2101;                   // 禁言
    public static final int AVICMD_SendResumeSpeak = 2102;                      // 恢复禁言
    public static final int AVICMD_SendRemove = 2103;                           // 踢出
    public static final int AVICMD_SendAddManager = 2104;                       // 设置为管理员
    public static final int AVICMD_SendDelManager = 2105;                       // 取消管理

    public static final int kGroupIMSilent = 10001;                         // 禁言IM
    public static final int kGroupIMNotSilent = 10002;                      // 解禁IM

    public static final int CHAT_MESSAGE_MAX_COUNT = 250;    // 聊天消息最多条数
    public static final int CHAT_MESSAGE_MAX_LENGTH = 160;  // 聊天消息最大长度

    public static final int GUEST_MAX_COUNT = 150;  // 观众头像最多显示人数
}
