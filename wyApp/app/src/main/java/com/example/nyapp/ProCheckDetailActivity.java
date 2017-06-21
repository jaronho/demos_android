package com.example.nyapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.adapter.TitleFragmentPagerAdapter;
import com.example.fragments.ParameterFragment;
import com.example.fragments.ProductIntroduceFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
/**
 * 产品详情
 */
public class ProCheckDetailActivity extends AppCompatActivity {

    @BindView(R.id.tab_check_detail)
    TabLayout mTabCheckDetail;
    @BindView(R.id.vp_check_detail)
    ViewPager mVpCheckDetail;
    private int mId;
    private String mResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_check_detail);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mResult = intent.getStringExtra("ProDetailData");
        mId = intent.getIntExtra("Id", 0);

        initData();
    }

    private void initData() {
        List<Fragment> fragments = new ArrayList<>();
        ParameterFragment fragment1 = new ParameterFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("ProDetailData", mResult);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);

        ProductIntroduceFragment fragment2 = new ProductIntroduceFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("Id", mId);
        fragment2.setArguments(bundle2);
        fragments.add(fragment2);

        TitleFragmentPagerAdapter titleFragmentPagerAdapter = new TitleFragmentPagerAdapter(getSupportFragmentManager(), fragments, new String[]{"参数", "产品介绍"});
        mVpCheckDetail.setAdapter(titleFragmentPagerAdapter);
        mTabCheckDetail.setupWithViewPager(mVpCheckDetail);
    }

    @OnClick(R.id.layout_back)
    public void onClick() {
        finish();
    }
}
