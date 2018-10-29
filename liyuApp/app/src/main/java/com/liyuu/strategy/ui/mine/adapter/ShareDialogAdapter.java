package com.liyuu.strategy.ui.mine.adapter;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseRecyclerViewAdapter;
import com.liyuu.strategy.base.RecyclerViewHolder;
import com.liyuu.strategy.model.bean.ShareInfoBean;
import com.liyuu.strategy.ui.dialog.ShareDialog;
import com.liyuu.strategy.util.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by hlw on 2017/12/7.
 */

public class ShareDialogAdapter extends BaseRecyclerViewAdapter<String> {

    private Context context;
    private ShareInfoBean shareInfoBean;
    private LayoutInflater inflater;
    private ShareDialog shareDialog;

    public ShareDialogAdapter(Context ctx, ShareInfoBean bean) {
        context = ctx;
        shareInfoBean = bean;
        this.inflater = LayoutInflater.from(ctx);
    }

    @Override
    public View onCreateView(ViewGroup group, int viewType) {
        View view = inflater.inflate(R.layout.item_share_dialog, group, false);
        return view;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, String item, int position) {
        holder.getTextView(R.id.tv_share_name).setText(item);
        int icon = getIcon(item);
        if (icon != 0)
            holder.getImageView(R.id.img_share_icon).setBackgroundResource(icon);
    }

    private int getIcon(String type) {
        int icon = 0;
        switch (type) {
            case "微信好友":
                icon = R.mipmap.share_ic_wx;
                break;
            case "朋友圈":
                icon = R.mipmap.share_ic_pyq;
                break;
            case "复制链接":
                icon = R.mipmap.share_ic_copylink;
                break;
        }
        return icon;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        shareDialog.dismiss();
        switch (getItem(position)) {
//            case "新浪微博":
//                if (!UMShareAPI.get(context).isInstall((Activity) context, SHARE_MEDIA.SINA)) {
//                    ToastUtil.showMsg("未安装新浪微博客户端");
//                    return;
//                }
//                share(SHARE_MEDIA.SINA);
//                break;
            case "微信好友":
                if (!UMShareAPI.get(context).isInstall((Activity) context, SHARE_MEDIA.WEIXIN)) {
                    ToastUtil.showMsg("未安装微信客户端");
                    return;
                }
                share(SHARE_MEDIA.WEIXIN);
                break;
            case "朋友圈":
                if (!UMShareAPI.get(context).isInstall((Activity) context, SHARE_MEDIA.WEIXIN)) {
                    ToastUtil.showMsg("未安装微信客户端");
                    return;
                }
                share(SHARE_MEDIA.WEIXIN_CIRCLE);
                break;
            case "复制链接":
                ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(shareInfoBean.share_url);
                ToastUtil.showMsg("复制成功");
                break;
        }
    }

    private void share(SHARE_MEDIA platform) {
        UMWeb web = new UMWeb(shareInfoBean.share_url);
        web.setTitle(shareInfoBean.title);//标题
        web.setThumb(new UMImage(context, shareInfoBean.share_img));  //缩略图 微博图片加载有问题
        web.setDescription(shareInfoBean.desc);//描述
        new ShareAction((Activity) context).withMedia(web)
                .setCallback(shareListener).setPlatform(platform).share();//传入平台
    }

    private UMShareListener shareListener = new UMShareListener() {
        /**
         * @descrption 分享开始的回调
         * @param platform 平台类型
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        /**
         * @descrption 分享成功的回调
         * @param platform 平台类型
         */
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(context, "成功了", Toast.LENGTH_LONG).show();
            if (shareDialog != null && shareDialog.onShareSuccessListener != null) {
                shareDialog.onShareSuccessListener.onShareSuccess();
            }
        }

        /**
         * @descrption 分享失败的回调
         * @param platform 平台类型
         * @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(context, "失败" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        /**
         * @descrption 分享取消的回调
         * @param platform 平台类型
         */
        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(context, "取消了", Toast.LENGTH_LONG).show();
        }
    };

    public void setShareDialog(ShareDialog shareDialog) {
        this.shareDialog = shareDialog;
    }
}
