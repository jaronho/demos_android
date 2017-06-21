package com.example.live.im.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.live.AuthorityInfo;
import com.example.live.ChannelInfo;
import com.example.live.LiveHelper;
import com.example.live.SpaceItemDecoration;
import com.example.live.UserInfo;
import com.example.nyapp.R;
import com.jaronho.sdk.third.circledialog.CircleDialog;
import com.jaronho.sdk.third.circledialog.callback.ConfigButton;
import com.jaronho.sdk.third.circledialog.callback.ConfigDialog;
import com.jaronho.sdk.third.circledialog.params.ButtonParams;
import com.jaronho.sdk.third.circledialog.params.DialogParams;
import com.jaronho.sdk.utils.adapter.QuickRecyclerViewAdapter;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.RefreshView;
import com.squareup.picasso.Picasso;
import com.tencent.TIMCallBack;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupMemberResult;
import com.tencent.TIMGroupMemberRoleType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMValueCallBack;

import java.util.ArrayList;
import java.util.List;

import static com.example.live.ConstantsLive.kGroupIMNotSilent;
import static com.example.live.ConstantsLive.kGroupIMSilent;

public class ChatMemberActivity extends FragmentActivity {
    public static ChatMemberActivity Instance = null;
    private ChannelInfo info = null;
    private RefreshView mMemberView = null;
    private List<UserInfo> mDatas = new ArrayList<>();
    private int mPages = 0;
    private int mRecords = 0;
    private int mCurrentPage = 1;
    private LiveHelper.Callback mCallback = new LiveHelper.Callback<UserInfo>() {
        @Override
        public void onData(UserInfo data) {
        }
        @Override
        public void onDataList(List<UserInfo> dataList) {
            for (int i = 0, len = dataList.size(); i < len; ++i) {
                UserInfo info = dataList.get(i);
                info.CustomType = 3;
                mDatas.add(info);
            }
            mMemberView.getView().getAdapter().notifyDataSetChanged();
            mPages = LiveHelper.getTempPages();
            mRecords = LiveHelper.getTempRecords();
            Log.d("NYLive", "get channel user list, pages: " + mPages + ", records: " + mRecords);
        }
    };

    private void initDatas() {
        // 群主标题
        UserInfo info1 = new UserInfo();
        info1.CustomType = 0;
        mDatas.add(info1);
        // 群主
        UserInfo info2 = new UserInfo();
        info2.UserId = info.PublisherID;
        info2.ProfilePicture = info.PublisherAvatar;
        info2.Name = info.PublisherName;
        info2.CustomType = 1;
        mDatas.add(info2);
        // 成员标题
        UserInfo info3 = new UserInfo();
        info3.CustomType = 2;
        mDatas.add(info3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;
        info = getIntent().getParcelableExtra("info");
        setContentView(R.layout.activity_chat_member);
        initDatas();
        // 标题头
        TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
        title.setTitleText("成员列表");
        // 成员列表
        mMemberView = (RefreshView)findViewById(R.id.refreshview_list);
        mMemberView.setHorizontal(false);
        LinearLayoutManager llmGuest = new LinearLayoutManager(this);
        llmGuest.setOrientation(LinearLayoutManager.VERTICAL);
        mMemberView.getView().setLayoutManager(llmGuest);
        mMemberView.getView().setHasFixedSize(true);
        mMemberView.getView().setAdapter(new WrapRecyclerViewAdapter<UserInfo>(this, mDatas, mMultiLayout) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, final UserInfo data) {
                updateItemData(quickViewHolder, data);
            }
        });
        mMemberView.setHeaderWrapper(mRefreshWrapper);
        mMemberView.setFooterWrapper(mLoadWrapper);
        mMemberView.getView().addItemDecoration(new SpaceItemDecoration(false, (int)getResources().getDimension(R.dimen.live_video_item_space)));
        onInitList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Instance = null;
    }

    private QuickRecyclerViewAdapter.MultiLayout<UserInfo> mMultiLayout = new QuickRecyclerViewAdapter.MultiLayout<UserInfo>() {
        @Override
        public int getLayoutId(int i, UserInfo userInfo) {
            switch (userInfo.CustomType) {
                case 0:     // 群主标题
                    return R.layout.item_member_line;
                case 1:     // 群主信息
                    return R.layout.item_member;
                case 2:     // 成员标题
                    return R.layout.item_member_line;
                default:    // 成员信息
                    return R.layout.item_member;
            }
        }
    };

    // 刷新包装器
    private RefreshView.Wrapper mRefreshWrapper = new RefreshView.Wrapper() {
        private ViewGroup mHeadView;
        private TextView mTipsTextView;
        @Override
        public View createView(Context context, RelativeLayout parent) {
            mHeadView = (ViewGroup) LayoutInflater.from(ChatMemberActivity.this).inflate(R.layout.live_refresh_header, parent, false);
            mTipsTextView = (TextView)mHeadView.findViewById(R.id.textview_tips);
            return mHeadView;
        }
        @Override
        public void onPullReady() {
        }
        @Override
        public void onPull(float maxOffset, float offset, boolean isForward, boolean isReady) {
            if (isReady) {
                mTipsTextView.setText("松开刷新");
            } else {
                mTipsTextView.setText("下拉刷新");
            }
        }
        @Override
        public void onPullAbort() {
        }
        @Override
        public void onRefreshing() {
            mMemberView.restore();
            mDatas.clear();
            mCurrentPage = 1;
            initDatas();
            refreshList();
        }
    };

    // 加载包装器
    private RefreshView.Wrapper mLoadWrapper = new RefreshView.Wrapper() {
        @Override
        public View createView(Context context, RelativeLayout parent) {
            return null;
        }
        @Override
        public void onPullReady() {
            if (mCurrentPage < mPages) {
                ++mCurrentPage;
                refreshList();
            } else {
                mCurrentPage = mPages;
            }
        }
        @Override
        public void onPull(float maxOffset, float offset, boolean isForward, boolean isReady) {
        }
        @Override
        public void onPullAbort() {
        }
        @Override
        public void onRefreshing() {
            mMemberView.restore();
        }
    };

    // 更新数据项
    private void updateItemData(QuickRecyclerViewAdapter.QuickViewHolder holder, final UserInfo data) {
        if (0 == data.CustomType) { // 群组标题
            TextView titleText = holder.getView(R.id.textview_title);
            titleText.setText("群主");
        } else if (2 == data.CustomType) {  // 成员标题
            TextView titleText = holder.getView(R.id.textview_title);
            titleText.setText("其他成员");
        } else {
            if (null != data.ProfilePicture && !data.ProfilePicture.isEmpty()) {
                Picasso.with(ChatMemberActivity.this).load(data.ProfilePicture).into((ImageView) holder.getView(R.id.imageview_guest));
            }
            TextView nicknameText = holder.getView(R.id.textview_nickname);
            nicknameText.setText(data.Name);
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickMember(data.UserId, data.Name);
                }
            });
        }
    }

    // 初始列表
    private void onInitList() {
        mDatas.clear();
        mCurrentPage = 1;
        initDatas();
        refreshList();
    }

    // 刷新列表
    private void refreshList() {
        int pageSize = 50;
        LiveHelper.reqGetChannelUsersList(info.Id, mCurrentPage, pageSize, mCallback);
    }

    // 点击成员
    private void onClickMember(final String userId, final String name) {
        if (userId.equals(LiveHelper.getUserId())) {
            LiveHelper.toast("无法对自己进行操作");
            return;
        }
        if (userId.equals(info.PublisherID)) {
            LiveHelper.toast("无法对群主进行操作");
            return;
        }
        if (LiveHelper.getUserId().equals(info.PublisherID)) {
            handleManagerMember(userId, name);
            return;
        }
        LiveHelper.reqGetAuthority(LiveHelper.getUserId(), info.Id, false, new LiveHelper.Callback<AuthorityInfo>() {
            @Override
            public void onData(AuthorityInfo data) {
                if (data.IsManage) {    // 观众是管理员
                    handleManagerMember(userId, name);
                }
            }
            @Override
            public void onDataList(List<AuthorityInfo> dataList) {
            }
        });
    }

    // 处理管理成员
    private void handleManagerMember(final String userId, final String name) {
        LiveHelper.reqGetAuthority(userId, info.Id, false, new LiveHelper.Callback<AuthorityInfo>() {
            @Override
            public void onData(final AuthorityInfo data) {
                final String[] items = {
                        data.IsManage ? "取消设置为管理员" : "设置为管理员",
                        data.IsWords ? "允许发言" : "禁止发言",
                        data.IsIn ? "允许进入房间" : "踢出房间"};
                new CircleDialog.Builder(ChatMemberActivity.this)
                        .configDialog(new ConfigDialog() {
                            @Override
                            public void onConfig(DialogParams params) {
                                params.animStyle = R.style.dialogWindowAnim;    // 增加弹出动画
                            }
                        })
                        .setTitle("是否对\"" + name + "\"进行操作？")
                        .setTitleColor(Color.BLUE)
                        .setItems(items, new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (0 == position) {
                                    data.IsManage = !data.IsManage;
                                    LiveHelper.reqChannelUserUpdateStatus(userId, info.Id, data.IsWords, data.IsIn, data.IsManage, new LiveHelper.Callback<AuthorityInfo>() {
                                        @Override
                                        public void onData(final AuthorityInfo data) {
                                            TIMGroupManager.getInstance().modifyGroupMemberInfoSetRole(info.VideoSource, userId, data.IsManage ? TIMGroupMemberRoleType.Admin : TIMGroupMemberRoleType.NotMember, new TIMCallBack() {
                                                @Override
                                                public void onError(int i, String s) {
                                                    Log.d("NYLive", "modifyGroupMemberInfoSetRole error, errCode: " + i + ", errMsg: " + s);
                                                    LiveHelper.toast("操作失败");
                                                }
                                                @Override
                                                public void onSuccess() {
                                                    Log.d("NYLive", "modifyGroupMemberInfoSetRole success");
                                                    LiveHelper.toast("操作成功");
                                                }
                                            });
                                        }
                                        @Override
                                        public void onDataList(List<AuthorityInfo> dataList) {
                                        }
                                    });
                                } else if (1 == position) {
                                    data.IsWords = !data.IsWords;
                                    LiveHelper.reqChannelUserUpdateStatus(userId, info.Id, data.IsWords, data.IsIn, data.IsManage, new LiveHelper.Callback<AuthorityInfo>() {
                                        @Override
                                        public void onData(final AuthorityInfo data) {
                                            TIMGroupManager.getInstance().modifyGroupMemberInfoSetSilence(info.VideoSource, userId, data.IsWords ? Integer.MAX_VALUE : 0, new TIMCallBack() {
                                                @Override
                                                public void onError(int i, String s) {
                                                    Log.d("NYLive", "modifyGroupMemberInfoSetSilence error, errCode: " + i + ", errMsg: " + s);
                                                    LiveHelper.toast("操作失败");
                                                }
                                                @Override
                                                public void onSuccess() {
                                                    Log.d("NYLive", "modifyGroupMemberInfoSetSilence success");
                                                    LiveHelper.toast("操作成功");
                                                    TIMConversation conversation = TIMManager.getInstance().getConversation(TIMConversationType.Group, info.VideoSource);
                                                    TIMMessage msg = new TIMMessage();
                                                    msg.setCustomInt(data.IsWords ? kGroupIMSilent : kGroupIMNotSilent);
                                                    msg.setSender(userId);
                                                    conversation.sendMessage(msg, new TIMValueCallBack<TIMMessage>() {
                                                        @Override
                                                        public void onError(int i, String s) {
                                                            Log.d("NYLive", "conversation sendMessage error, code: " + i + ", desc: " + s);
                                                        }
                                                        @Override
                                                        public void onSuccess(TIMMessage timMessage) {
                                                            Log.d("NYLive", "conversation sendMessage success");
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        @Override
                                        public void onDataList(List<AuthorityInfo> dataList) {
                                        }
                                    });
                                } else if (2 == position) {
                                    data.IsIn = !data.IsIn;
                                    LiveHelper.reqChannelUserUpdateStatus(userId, info.Id, data.IsWords, data.IsIn, data.IsManage, new LiveHelper.Callback<AuthorityInfo>() {
                                        @Override
                                        public void onData(final AuthorityInfo data) {
                                            List<String> userIds = new ArrayList<String>();
                                            userIds.add(userId);
                                            TIMGroupManager.getInstance().deleteGroupMemberWithReason(info.VideoSource, "", userIds, new TIMValueCallBack<List<TIMGroupMemberResult>>() {
                                                @Override
                                                public void onError(int i, String s) {
                                                    Log.d("NYLive", "deleteGroupMemberWithReason error, errCode: " + i + ", errMsg: " + s);
                                                    LiveHelper.toast("操作失败");
                                                }
                                                @Override
                                                public void onSuccess(List<TIMGroupMemberResult> timGroupMemberResults) {
                                                    Log.d("NYLive", "deleteGroupMemberWithReason success");
                                                    LiveHelper.toast("操作成功");
                                                }
                                            });
                                        }
                                        @Override
                                        public void onDataList(List<AuthorityInfo> dataList) {
                                        }
                                    });
                                }
                            }
                        })
                        .setNegative("取消", null)
                        .configNegative(new ConfigButton() {
                            @Override
                            public void onConfig(ButtonParams params) {
                                params.textColor = Color.RED;   // 取消按钮字体颜色
                            }
                        })
                        .show();
            }
            @Override
            public void onDataList(List<AuthorityInfo> dataList) {
            }
        });
    }
}
