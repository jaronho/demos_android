package com.example.util;

import android.app.Activity;

import com.example.classes.Share;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;

/**
 * Created by NY on 2017/1/18.
 * 分享工具类
 */

public class ShareUtil {

    private static UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            switch (platform) {
                case WEIXIN:
                    break;
                case WEIXIN_CIRCLE:
                    break;
            }
            MyToastUtil.showShortMessage("分享成功啦");

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            MyToastUtil.showShortMessage("分享失败啦");
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            MyToastUtil.showShortMessage("分享取消了");
        }
    };

    private static Activity sActivity;

    public static void initShareDate(Activity activity, Share.DataBean mShareData) {
        sActivity = activity;
        ShareBoardConfig config = new ShareBoardConfig();//设置分享面板
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);//面板居中
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);//圆角背景
        config.setCancelButtonVisibility(true);//取消按钮


        new ShareAction(activity)
                .withTitle(mShareData.getSharedTitle())// 设置title
                .withText(mShareData.getSharedContent())// 设置分享内容
                .withMedia(new UMImage(activity,mShareData.getSharedImage()))// 设置分享图片, 参数2为图片的url地址
                .withTargetUrl(mShareData.getSharedUrl())// 设置分享内容跳转URL
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)//分享平台
                .setCallback(umShareListener)//分享监听
                .open(config);//面板配置
    }

}
