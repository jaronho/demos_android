package com.liyuu.strategy.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.liyuu.strategy.R;
import com.liyuu.strategy.app.SPKeys;
import com.liyuu.strategy.base.SimpleActivity;
import com.liyuu.strategy.util.PreferenceUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 640 on 2018/1/17 0017.
 */

public class GuideActivity extends SimpleActivity {
    /**
     * 当前引导页的标记位，如果引导页有修改的话，则修改该值，并且不能小于当前值
     */
    public static final int GUIDE_VERSION = 1;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;
    private ImageView mGoBtn;
    private int[] viewIds = new int[]{R.layout.view_guide1, R.layout.view_guide2, R.layout.view_guide3};// , R.layout.view_guide4

    public static void start(Context context) {
        context.startActivity(new Intent(context, GuideActivity.class));
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initEventAndData() {
        PreferenceUtils.put(SPKeys.FILE_COMMON, SPKeys.GUIDE_VERSION, GUIDE_VERSION);

        mViewPager.setAdapter(new ImageAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class ImageAdapter extends PagerAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return viewIds.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View view = LayoutInflater.from(GuideActivity.this).inflate(viewIds[position], null);
            if (position == viewIds.length - 1) {
                mGoBtn = ButterKnife.findById(view, R.id.btn_go);
                mGoBtn.setOnClickListener(this);
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_go:
                    Intent intent = new Intent(GuideActivity.this, MainActivity.class);
                    intent.putExtra("is_from_guide", true);
                    startActivity(intent);
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    break;
            }
        }
    }
}
