package com.nongyi.nylive;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author:  jaron.ho
 * Date:    2017-04-07
 * Brief:   QuickFragmentPageAdapter
 */

public class QuickFragmentPageAdapter<T extends Fragment> extends FragmentPagerAdapter {
    private List<T> mList = null;
    private List<String> mTitles = null;

    public QuickFragmentPageAdapter(FragmentManager fm, List<T> list) {
        super(fm);
        mList = list;
    }

    public QuickFragmentPageAdapter(FragmentManager fm, List<T> list, List<String> titles) {
        super(fm);
        mList = list;
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (null == mTitles || position >= mTitles.size()) {
            return super.getPageTitle(position);
        }
        return mTitles.get(position);
    }
}
