package com.grcen.bestthoughts.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.grcen.bestthoughts.fragments.MainFragment;

public class MainFragmentAdapter extends FragmentPagerAdapter {
    private String[] mTitle = {"Gif", "视频", "图片", "文字"};

    public MainFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return MainFragment.newInstance(i);
    }

    @Override
    public int getCount() {
        return mTitle.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mTitle[position];
    }
}
