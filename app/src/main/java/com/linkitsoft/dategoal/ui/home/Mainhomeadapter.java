package com.linkitsoft.dategoal.ui.home;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class Mainhomeadapter  extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public Mainhomeadapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                home_main homeFragment = new home_main();
                return homeFragment;
            case 1:
                homechild settingsFragment = new homechild();
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