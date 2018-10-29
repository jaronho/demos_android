package com.liyuu.strategy.ui.transaction.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.liyuu.strategy.R;
import com.liyuu.strategy.base.BaseActivity;
import com.liyuu.strategy.contract.NullContract;
import com.liyuu.strategy.model.bean.IncomeTypesBean;
import com.liyuu.strategy.presenter.NullPresenter;
import com.liyuu.strategy.ui.transaction.fragment.BillListFragment;
import com.liyuu.strategy.ui.view.CustomTabLayout;

import java.util.List;

import butterknife.ButterKnife;


public class BillsActivity extends BaseActivity<NullPresenter>
        implements NullContract.View, ViewPager.OnPageChangeListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, BillsActivity.class));
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bills;
    }

    @Override
    public void initUI() {
        super.initUI();
        initTabLayout();
        setTitle("资金流水");
    }

    @Override
    protected void initEventAndData() {

    }

    private void initTabLayout() {
        final ViewPager pager = ButterKnife.findById(this, R.id.view_pager);
        pager.setHorizontalScrollBarEnabled(true);
        pager.setAdapter(new PageAdapter(getSupportFragmentManager()));
        pager.addOnPageChangeListener(this);

        CustomTabLayout tabLayout = ButterKnife.findById(this, R.id.tab_layout);
        tabLayout.setupWithViewPager(pager);
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

    private class PageAdapter extends FragmentPagerAdapter {

        String[] titles = new String[]{"全部", "充值/提现", "收入/支出"};
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
            //1 全部 2 充值取现 3 收入支出
            return BillListFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }
}
