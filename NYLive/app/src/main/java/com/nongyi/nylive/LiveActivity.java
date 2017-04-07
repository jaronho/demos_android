package com.nongyi.nylive;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class LiveActivity extends AppCompatActivity {
    private ViewPager mViewpagerList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live);

        ImageView imageviewBack = (ImageView)findViewById(R.id.imageview_back);
        imageviewBack.setOnClickListener(onClickImageviewBack);

        TextView textviewMine = (TextView)findViewById(R.id.textview_mine);
        textviewMine.setOnClickListener(onClickTextviewMine);

        RelativeLayout layoutTabLive = (RelativeLayout)findViewById(R.id.layout_tab_live);
        layoutTabLive.setOnClickListener(onClickLayoutTabLive);

        RelativeLayout layoutTabVideo = (RelativeLayout)findViewById(R.id.layout_tab_video);
        layoutTabVideo.setOnClickListener(onClickLayoutTabVideo);

        mViewpagerList = (ViewPager)findViewById(R.id.viewpager_list);
        mViewpagerList.addOnPageChangeListener(onPageChangeViewpagerList);
        mViewpagerList.setOffscreenPageLimit(1);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ListFragment());
        fragments.add(new ListFragment());
        mViewpagerList.setAdapter(new QuickFragmentPageAdapter<>(getSupportFragmentManager(), fragments));

        switchTabLive();
    }

    OnClickListener onClickImageviewBack = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    OnClickListener onClickTextviewMine = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(LiveActivity.this, MyLiveActivity.class));
        }
    };

    OnClickListener onClickLayoutTabLive = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switchTabLive();
            mViewpagerList.setCurrentItem(0);
        }
    };

    OnClickListener onClickLayoutTabVideo = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switchTabViedo();
            mViewpagerList.setCurrentItem(1);
        }
    };

    OnPageChangeListener onPageChangeViewpagerList = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (0 == position) {
                switchTabLive();
            } else if (1 == position) {
                switchTabViedo();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    void invalidTabs() {
        RelativeLayout layoutTabLive = (RelativeLayout)findViewById(R.id.layout_tab_live);
        layoutTabLive.setEnabled(true);

        TextView textviewLive = (TextView)findViewById(R.id.textview_live);
        textviewLive.setTextColor(getResources().getColor(R.color.gray));

        View viewLiveUnderline = findViewById(R.id.view_live_underline);
        viewLiveUnderline.setVisibility(View.INVISIBLE);

        RelativeLayout layoutTabVideo = (RelativeLayout)findViewById(R.id.layout_tab_video);
        layoutTabVideo.setEnabled(true);

        TextView textviewVideo = (TextView)findViewById(R.id.textview_video);
        textviewVideo.setTextColor(getResources().getColor(R.color.gray));

        View viewVideoUnderline = (View)findViewById(R.id.view_video_underline);
        viewVideoUnderline.setVisibility(View.INVISIBLE);
    }

    void switchTabLive() {
        invalidTabs();

        RelativeLayout layoutTabLive = (RelativeLayout)findViewById(R.id.layout_tab_live);
        layoutTabLive.setEnabled(false);

        TextView textviewLive = (TextView)findViewById(R.id.textview_live);
        textviewLive.setTextColor(getResources().getColor(R.color.black));

        View viewLiveUnderline = findViewById(R.id.view_live_underline);
        viewLiveUnderline.setVisibility(View.VISIBLE);
    }

    void switchTabViedo() {
        invalidTabs();

        RelativeLayout layoutTabVideo = (RelativeLayout)findViewById(R.id.layout_tab_video);
        layoutTabVideo.setEnabled(false);

        TextView textviewVideo = (TextView)findViewById(R.id.textview_video);
        textviewVideo.setTextColor(getResources().getColor(R.color.black));

        View viewVideoUnderline = findViewById(R.id.view_video_underline);
        viewVideoUnderline.setVisibility(View.VISIBLE);
    }
}
