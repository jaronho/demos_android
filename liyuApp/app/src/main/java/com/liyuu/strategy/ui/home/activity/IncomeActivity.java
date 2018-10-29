package com.liyuu.strategy.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.home.IncomeContract;
import com.liyuu.strategy.model.bean.IncomeTypesBean;
import com.liyuu.strategy.presenter.home.IncomePresenter;
import com.liyuu.strategy.ui.home.fragment.IncomeListFragment;
import com.liyuu.strategy.ui.view.CustomTabLayout;

import java.util.List;

import butterknife.ButterKnife;


public class IncomeActivity extends BaseActivity<IncomePresenter> implements IncomeContract.View, ViewPager.OnPageChangeListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, IncomeActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_income;
    }

    @Override
    public void initUI() {
        super.initUI();
        initTabLayout();
        setTitle(R.string.menu_income);
    }

    @Override
    protected void initEventAndData() {
        mPresenter.getIncomeType();
    }

    private void initTabLayout() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void loadTabs(List<IncomeTypesBean> tabs) {
        final ViewPager pager = ButterKnife.findById(this, R.id.view_pager);
        pager.setHorizontalScrollBarEnabled(true);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), tabs));
        pager.addOnPageChangeListener(this);

        CustomTabLayout tabLayout = ButterKnife.findById(this, R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
    }


    private class PageAdapter extends FragmentPagerAdapter {

        private List<IncomeTypesBean> tabs;

        public PageAdapter(FragmentManager fm, List<IncomeTypesBean> tabs) {
            super(fm);
            this.tabs = tabs;
        }

        private PageAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return IncomeListFragment.newInstance(tabs.get(position).getTypeId());
        }

        @Override
        public int getCount() {
            return tabs.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs.get(position).getName();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
