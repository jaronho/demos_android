package com.nongyi.nylive.utils;

/**
 * Author:  Administrator
 * Date:    2017/5/3
 * Brief:   Constants
 */

public class Constants {
    public static final int AVIMCMD_TEXT = -1;         // 普通的聊天消息
    public static final int AVIMCMD_NONE = AVIMCMD_TEXT + 1;               // 无事件

    // 以下事件为TCAdapter内部处理的通用事件
    public static final int AVIMCMD_ENTERLIVE = AVIMCMD_NONE + 1;          // 用户加入直播,
    public static final int AVIMCMD_EXITLIVE = AVIMCMD_ENTERLIVE + 1;         // 用户退出直播,
    public static final int AVIMCMD_PRAISE = AVIMCMD_EXITLIVE + 1;           // 点赞消息,
    public static final int AVIMCMD_HOST_LEAVE = AVIMCMD_PRAISE + 1;         // 主播离开,
    public static final int AVIMCMD_HOST_BACK = AVIMCMD_HOST_LEAVE + 1;      // 主播回来,

    // 自定义事件
    public static final int EVENT_NEW_TEXT_MSG = 1;     // 新文本消息
    public static final int EVENT_GUEST_ENTER_ROOM = 2; // 有观众进入房间
    public static final int EVENT_GUEST_LEAVE_ROOM = 3; // 有观众离开房间
}
