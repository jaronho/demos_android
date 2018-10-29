package com.liyuu.strategy.ui;

import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.App;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.base.BaseFragment;
import com.liyuu.strategy.component.ImageLoader;
import com.liyuu.strategy.component.RxBus;
import com.liyuu.strategy.contract.main.MineContract;
import com.liyuu.strategy.presenter.main.MinePresenter;
import com.liyuu.strategy.ui.dialog.AlertDialog;
import com.liyuu.strategy.ui.dialog.BaseDialog;
import com.liyuu.strategy.ui.dialog.BaseDialogImpl;
import com.liyuu.strategy.ui.dialog.CustomerWeChatDialog;
import com.liyuu.strategy.ui.mine.WebViewActivity;
import com.liyuu.strategy.ui.mine.activity.MessageActivity;
import com.liyuu.strategy.ui.mine.activity.MineRecordActivity;
import com.liyuu.strategy.ui.mine.activity.SettingActivity;
import com.liyuu.strategy.ui.mine.activity.UserEditActivity;
import com.liyuu.strategy.ui.transaction.activity.NewsWelfareActivity;
import com.liyuu.strategy.util.PreferenceUtils;
import com.liyuu.strategy.util.UserInfoUtil;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.YSFUserInfo;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment<MinePresenter> implements MineContract.View {
    @BindView(R.id.iv_news_banner)
    ImageView ivNewsBanner;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.tv_my_record)
    TextView tvMyRecord;
    @BindView(R.id.tv_my_invite_friend)
    TextView tvMyInviteFriend;
    @BindView(R.id.tv_my_news)
    TextView tvMyNews;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.v_line_1)
    View vLine1;
    @BindView(R.id.v_line_2)
    View vLine2;
    @BindView(R.id.v_line_3)
    View vLine3;
    @BindView(R.id.img_header)
    ImageView imgHeader;

    @Inject
    YSFOptions qiYuOpyions;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initUI() {
        super.initUI();
        updateUser();
        ImageLoader.loadHead(App.getInstance(), PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HEADER_URL), imgHeader);
        String customer_wechat = PreferenceUtils.getString(SPKeys.FILE_URL, SPKeys.CUSTOMER_WE_CHAT);
        tvWx.setText(customer_wechat);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.checkStatus();
    }

    @OnClick({R.id.layout_head, R.id.tv_my_record,
            R.id.tv_my_invite_friend, R.id.tv_my_news, R.id.tv_my_live_service,
            R.id.tv_my_message, R.id.tv_my_setting, R.id.iv_news_banner, R.id.layout_service_wx})
    void onCLick(View view) {
        switch (view.getId()) {
            case R.id.layout_head:
                if (!UserInfoUtil.isWithLogin(getActivity())) return;
                UserEditActivity.start(getActivity());
                break;
//            case R.id.img_header:
//                if (!UserInfoUtil.isWithLogin(getActivity())) return;
//                    UserEditActivity.start(getActivity());
//                break;
            case R.id.tv_my_record://我的战绩
                if (!UserInfoUtil.isWithLogin(getActivity())) return;
                MineRecordActivity.start(getActivity());
                break;
            case R.id.tv_my_invite_friend:
                if (!UserInfoUtil.isWithLogin(getActivity()))
                    break;
                String inviteUrl = PreferenceUtils.getString(SPKeys.FILE_URL, SPKeys.URL_INVITE);
                if (!TextUtils.isEmpty(inviteUrl))
                    WebViewActivity.start(getActivity(), inviteUrl);
                break;
            case R.id.tv_my_news:
                String totuialUrl = PreferenceUtils.getString(SPKeys.FILE_URL, SPKeys.URL_TUTORIAL);
                if (!TextUtils.isEmpty(totuialUrl))
                    WebViewActivity.start(getActivity(), totuialUrl);
                break;
            case R.id.tv_my_live_service:
                if (!UserInfoUtil.isWithLogin(getActivity()))
                    return;

                String userHeaderUrl = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HEADER_URL);
                if (!TextUtils.isEmpty(userHeaderUrl)) {
                    qiYuOpyions.uiCustomization.rightAvatar = userHeaderUrl;
                }

                String openId = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_OPEN_ID);
                if (!TextUtils.isEmpty(openId)) {
                    YSFUserInfo info = new YSFUserInfo();
                    info.userId = openId;
                    Unicorn.setUserInfo(info);
                }

                String title = "在线客服";
                /**
                 * 设置访客来源，标识访客是从哪个页面发起咨询的，用于客服了解用户是从什么页面进入。
                 * 三个参数分别为：来源页面的url，来源页面标题，来源页面额外信息（保留字段，暂时无用）。
                 * 设置来源后，在客服会话界面的"用户资料"栏的页面项，可以看到这里设置的值。
                 */
                ConsultSource source = new ConsultSource("android_sourceUrl", "android_sourceTitle", "android custom information string");
                /**
                 * 请注意： 调用该接口前，应先检查Unicorn.isServiceAvailable()，
                 * 如果返回为false，该接口不会有任何动作
                 *
                 * @param context 上下文
                 * @param title   聊天窗口的标题
                 * @param source  咨询的发起来源，包括发起咨询的url，title，描述信息等
                 */
                Unicorn.openServiceActivity(getActivity(), title, source);
                break;
            case R.id.tv_my_message:
                MessageActivity.start(getActivity());
                break;
            case R.id.tv_my_setting:
                SettingActivity.start(getActivity());
                break;
            case R.id.layout_service_wx:
                showDialog();
                break;
            case R.id.iv_news_banner:
                NewsWelfareActivity.start(getActivity());
                break;
        }
    }

    @Override
    public void onEventAccept(int code, Object object) {
        switch (code) {
            case RxBus.Code.USER_LOGIN_SUCCESS_WITH_USERINDEXBEAN:
            case RxBus.Code.USER_LOGIN_OUT:
                updateUser();
                break;
            case RxBus.Code.USER_HEADER_REFRESH:
                ImageLoader.loadHead(App.getInstance(), PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HEADER_URL), imgHeader);
                break;
            case RxBus.Code.EDIT_NICKNAME_SUCCESS:
                updateUser();
                break;
            case RxBus.Code.NEWS_WELFARE_BUYING_SUCCESS:
                ivNewsBanner.setVisibility(View.GONE);
                break;
        }
    }

    private void updateUser() {
        mPresenter.checkStatus();
        if (UserInfoUtil.isLogin()) {
            tvNickname.setText(PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME));
            String is_activity = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_IS_ACTIVITY);
            if ("1".equals(is_activity)) {
                ivNewsBanner.setVisibility(View.VISIBLE);
                ImageLoader.load(getActivity(), PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_ACTIVITY_IMAGE), ivNewsBanner);
            }
        } else {
            ivNewsBanner.setVisibility(View.GONE);
            tvNickname.setText(R.string.click_to_login);
        }
        ImageLoader.loadHead(App.getInstance(), PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_HEADER_URL), imgHeader);
    }

    @Override
    public void checkViewChange(boolean isChecking) {
        tvMyInviteFriend.setVisibility(isChecking ? View.GONE : View.VISIBLE);
        tvMyRecord.setVisibility(isChecking ? View.GONE : View.VISIBLE);
        tvMyNews.setVisibility(isChecking ? View.GONE : View.VISIBLE);
        vLine1.setVisibility(isChecking ? View.GONE : View.VISIBLE);
        vLine2.setVisibility(isChecking ? View.GONE : View.VISIBLE);
        vLine3.setVisibility(isChecking ? View.GONE : View.VISIBLE);
    }

    private void showDialog() {
        String wx = tvWx.getText().toString();
        if(TextUtils.isEmpty(wx)) return;
        copy(wx);
        CustomerWeChatDialog dialog = CustomerWeChatDialog.newInstance(wx);
        dialog.setBaseImpl(new BaseDialogImpl() {
            @Override
            public void dialogCancle(String dialogName, BaseDialog dialog) {
                dialog.dismiss();
            }

            @Override
            public void dialogSure(String dialogName, BaseDialog dialog) {
                dialog.dismiss();
                Intent intent = new Intent();
                ComponentName cmp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.LauncherUI");
                intent.setAction(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_LAUNCHER);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setComponent(cmp);
                startActivity(intent);
            }
        });
        dialog.show(getChildFragmentManager());
    }

    private void copy(String content) {
        // 得到剪贴板管理器
        ClipboardManager cmb = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        cmb.setText(content.trim());
    }
}
