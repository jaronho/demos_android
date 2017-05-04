package com.example.nyapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.fragments.Fragment_LiveList;
import com.example.nyapp.R;
import com.jaronho.sdk.utils.adapter.QuickFragmentPageAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyLiveListActivity extends AppCompatActivity {
    private ViewPager mViewpagerList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_live_list);

        ImageView imageviewBack = (ImageView)findViewById(R.id.imageview_back);
        imageviewBack.setOnClickListener(onClickImageviewBack);

        RelativeLayout layoutTabJoin = (RelativeLayout)findViewById(R.id.layout_tab_join);
        layoutTabJoin.setOnClickListener(onClickLayoutTabJoin);

        RelativeLayout layoutTabCollect = (RelativeLayout)findViewById(R.id.layout_tab_collect);
        layoutTabCollect.setOnClickListener(onClickLayoutTabCollect);

        RelativeLayout layoutTabStart = (RelativeLayout)findViewById(R.id.layout_tab_start);
        layoutTabStart.setOnClickListener(onClickLayoutTabStart);

        mViewpagerList = (ViewPager)findViewById(R.id.viewpager_list);
        mViewpagerList.addOnPageChangeListener(onPageChangeViewpagerList);
        mViewpagerList.setOffscreenPageLimit(2);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment_LiveList());
        fragments.add(new Fragment_LiveList());
        fragments.add(new Fragment_LiveList());
        mViewpagerList.setAdapter(new QuickFragmentPageAdapter<>(getSupportFragmentManager(), fragments));

        switchTabJoin();
    }

    OnClickListener onClickImageviewBack = new OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    OnClickListener onClickLayoutTabJoin = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switchTabJoin();
            mViewpagerList.setCurrentItem(0);
        }
    };

    OnClickListener onClickLayoutTabCollect = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switchTabCollect();
            mViewpagerList.setCurrentItem(1);
        }
    };

    OnClickListener onClickLayoutTabStart = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switchTabStart();
            mViewpagerList.setCurrentItem(2);
        }
    };

    ViewPager.OnPageChangeListener onPageChangeViewpagerList = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (0 == position) {
                switchTabJoin();
            } else if (1 == position) {
                switchTabCollect();
            } else if (2 == position) {
                switchTabStart();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    void invalidTabs() {
        RelativeLayout layoutTabJoin= (RelativeLayout)findViewById(R.id.layout_tab_join);
        layoutTabJoin.setEnabled(true);

        TextView textviewJoin = (TextView)findViewById(R.id.textview_join);
        textviewJoin.setTextColor(getResources().getColor(R.color.gray2));

        View viewJoinUnderline = findViewById(R.id.view_join_underline);
        viewJoinUnderline.setVisibility(View.INVISIBLE);

        RelativeLayout layoutTabCollect = (RelativeLayout)findViewById(R.id.layout_tab_collect);
        layoutTabCollect.setEnabled(true);

        TextView textviewCollect = (TextView)findViewById(R.id.textview_collect);
        textviewCollect.setTextColor(getResources().getColor(R.color.gray2));

        View viewCollectUnderline = findViewById(R.id.view_collect_underline);
        viewCollectUnderline.setVisibility(View.INVISIBLE);

        RelativeLayout layoutTabStart = (RelativeLayout)findViewById(R.id.layout_tab_start);
        layoutTabStart.setEnabled(true);

        TextView textviewStart = (TextView)findViewById(R.id.textview_start);
        textviewStart.setTextColor(getResources().getColor(R.color.gray2));

        View viewStartUnderline = findViewById(R.id.view_start_underline);
        viewStartUnderline.setVisibility(View.INVISIBLE);
    }

    void switchTabJoin() {
        invalidTabs();

        RelativeLayout layoutTabJoin= (RelativeLayout)findViewById(R.id.layout_tab_join);
        layoutTabJoin.setEnabled(false);

        TextView textviewJoin = (TextView)findViewById(R.id.textview_join);
        textviewJoin.setTextColor(getResources().getColor(R.color.black));

        View viewJoinUnderline = findViewById(R.id.view_join_underline);
        viewJoinUnderline.setVisibility(View.VISIBLE);
    }

    void switchTabCollect() {
        invalidTabs();

        RelativeLayout layoutTabCollect = (RelativeLayout)findViewById(R.id.layout_tab_collect);
        layoutTabCollect.setEnabled(false);

        TextView textviewCollect = (TextView)findViewById(R.id.textview_collect);
        textviewCollect.setTextColor(getResources().getColor(R.color.black));

        View viewCollectUnderline = findViewById(R.id.view_collect_underline);
        viewCollectUnderline.setVisibility(View.VISIBLE);
    }

    void switchTabStart() {
        invalidTabs();

        RelativeLayout layoutTabStart = (RelativeLayout)findViewById(R.id.layout_tab_start);
        layoutTabStart.setEnabled(false);

        TextView textviewStart = (TextView)findViewById(R.id.textview_start);
        textviewStart.setTextColor(getResources().getColor(R.color.black));

        View viewStartUnderline = findViewById(R.id.view_start_underline);
        viewStartUnderline.setVisibility(View.VISIBLE);
    }
}
