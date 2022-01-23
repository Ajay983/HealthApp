package com.linkitsoft.dategoal.ui.home;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class homefragmentadapter  extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public homefragmentadapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Home_Map homeFragment = new Home_Map();
                return homeFragment;
            case 1:
                Home_List settingsFragment = new Home_List();
                return settingsFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}