package com.example.live.im.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.live.ChannelInfo;
import com.example.live.LiveHelper;
import com.example.live.SpaceItemDecoration;
import com.example.live.UserInfo;
import com.example.nyapp.R;
import com.jaronho.sdk.utils.adapter.WrapRecyclerViewAdapter;
import com.jaronho.sdk.utils.view.RefreshView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatInfoActivity extends FragmentActivity {
    public static ChatInfoActivity Instance = null;
    private ChannelInfo info = null;
    private RefreshView mMemberView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Instance = this;
        info = getIntent().getParcelableExtra("info");
        setContentView(R.layout.activity_chat_info);
        // 标题头
        TemplateTitle title = (TemplateTitle) findViewById(R.id.chat_title);
        title.setTitleText("聊天室信息");
        // 主图
        ImageView snapshootImage = (ImageView)findViewById(R.id.imageview_snapshoot);
        if (null != info.MainMap && !info.MainMap.isEmpty()) {
            Picasso.with(this).load(info.MainMap).into(snapshootImage);
        }
        // 聊天室标题
        TextView titleText = (TextView)findViewById(R.id.textview_title);
        titleText.setText(info.Title);
        // 昵称
        TextView nicknameText = (TextView)findViewById(R.id.textview_nickname);
        nicknameText.setText(LiveHelper.getNickname());
        // 聊天室介绍
        TextView descText = (TextView)findViewById(R.id.textview_desc);
        descText.setText(info.Details);
        // 成员列表
        final List<UserInfo> memberProfiles = new ArrayList<>();
        mMemberView = (RefreshView)findViewById(R.id.refreshview_list);
        mMemberView.setHorizontal(true);
        LinearLayoutManager llmGuest = new LinearLayoutManager(this);
        llmGuest.setOrientation(LinearLayoutManager.HORIZONTAL);
        mMemberView.getView().setLayoutManager(llmGuest);
        mMemberView.getView().setHasFixedSize(true);
        mMemberView.getView().setAdapter(new WrapRecyclerViewAdapter<UserInfo>(this, memberProfiles, R.layout.live_item_guest) {
            @Override
            public void onBindViewHolder(QuickViewHolder quickViewHolder, final UserInfo data) {
                if (null != data.ProfilePicture && !data.ProfilePicture.isEmpty()) {
                    Picasso.with(ChatInfoActivity.this).load(data.ProfilePicture).into((ImageView)quickViewHolder.getView(R.id.imageview_guest));
                }
                quickViewHolder.setOnClickListener(onClickListener);
            }
        });
        mMemberView.getView().addItemDecoration(new SpaceItemDecoration(true, (int)getResources().getDimension(R.dimen.live_guest_item_space)));
        // 成员人数
        final TextView countText = (TextView)findViewById(R.id.textview_count);
        countText.setText("0人");
        // 点击跳转到成员列表
        ViewGroup layoutInfo = (ViewGroup)findViewById(R.id.layout_info);
        layoutInfo.setOnClickListener(onClickListener);
        // 获取数据
        LiveHelper.reqGetChannelUsersList(info.Id, 1, 50, new LiveHelper.Callback<UserInfo>() {
            @Override
            public void onData(UserInfo data) {
            }
            @Override
            public void onDataList(List<UserInfo> dataList) {
                int count = LiveHelper.getTempRecords() + 1;
                countText.setText(count + "人");
                UserInfo ui = new UserInfo();
                ui.UserId = info.PublisherID;
                ui.ProfilePicture = info.PublisherAvatar;
                ui.Name = info.PublisherName;
                memberProfiles.add(ui);
                for (int i = 0, len = dataList.size(); i < len; ++i) {
                    memberProfiles.add(dataList.get(i));
                }
                mMemberView.getView().getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Instance = null;
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(ChatInfoActivity.this, ChatMemberActivity.class);
            intent.putExtra("info", info);
            startActivity(intent);
        }
    };
}
