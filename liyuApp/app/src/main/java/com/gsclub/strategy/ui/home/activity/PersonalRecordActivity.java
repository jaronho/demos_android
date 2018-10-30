package com.gsclub.strategy.ui.home.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gsclub.strategy.R;
import com.gsclub.strategy.app.SPKeys;
import com.gsclub.strategy.base.BaseActivity;
import com.gsclub.strategy.contract.home.PersonalRecordContract;
import com.gsclub.strategy.model.bean.RankUserBean;
import com.gsclub.strategy.presenter.home.PersonalRecordPresenter;
import com.gsclub.strategy.ui.home.fragment.PersonalRecord5DYieldFragment;
import com.gsclub.strategy.ui.home.fragment.PersonalRecordListFragment;
import com.gsclub.strategy.ui.view.CustomTabLayout;
import com.gsclub.strategy.util.PreferenceUtils;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人战绩界面
 */
public class PersonalRecordActivity extends BaseActivity<PersonalRecordPresenter>
        implements PersonalRecordContract.View, ViewPager.OnPageChangeListener {
    @BindView(R.id.tv_nickname)
    TextView tvNickname;

    private static final String RANK_ID_INT = "rank_id";

    private int rankID;//排行榜rankid

    public static void start(Context context, int rankID) {
        Intent intent = new Intent(context, PersonalRecordActivity.class);
        intent.putExtra(RANK_ID_INT, rankID);
        context.startActivity(intent);
    }

    @Override
    protected void initInject() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_personal_record;
    }

    @Override
    public void initUI() {
        super.initUI();
        initTabLayout();
        setTitle("个人战绩");
        String nickname = PreferenceUtils.getString(SPKeys.FILE_USER_INFO, SPKeys.USER_INFO_NICKNAME);
        tvNickname.setText(nickname);
    }

    @Override
    protected void initEventAndData() {
        rankID = getIntent().getIntExtra(RANK_ID_INT, -1);
        if (rankID == -1)
            finishUI();

        mPresenter.getUserDetail(rankID);
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

    @Override
    public void showRankUserMessage(RankUserBean data) {
        getTextView(R.id.tv_nickname).setText(data.getUser_data().getNickname());
        getTextView(R.id.tv_five_day_profit).setText(String.format("%s%s", data.getUser_data().getFiveRate(), "%"));
        getTextView(R.id.tv_all_profit).setText(String.format("%s%s", data.getUser_data().getCountRate(), "%"));
        getTextView(R.id.tv_all_win_precent).setText(String.format("%s%s", data.getUser_data().getCountWinRate(), "%"));
    }

    private class PageAdapter extends FragmentPagerAdapter {

        String[] titles = new String[]{"5日收益", "交易记录"};

        PageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return PersonalRecord5DYieldFragment.newInstance(position + 1, rankID);
            }
            return PersonalRecordListFragment.newInstance(position + 1, rankID);
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
