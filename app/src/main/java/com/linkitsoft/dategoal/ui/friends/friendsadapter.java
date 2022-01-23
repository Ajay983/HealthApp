package com.linkitsoft.dategoal.ui.friends;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class friendsadapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public friendsadapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    public friendslist frndlist = new friendslist();
    public friendrequests settingsFragment = new friendrequests();
    public Friendsfavorites favfrag = new Friendsfavorites();

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {

            case 0:
                fragment = frndlist;
                break;
            case 1:
                fragment = settingsFragment;
                break;
            case 2:
                fragment = favfrag;
                break;
        }

        return fragment;

    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

//    @Override
//    public void notifyDataSetChanged() {
//        super.notifyDataSetChanged();
//    }
//
//    @NonNull
//    @Override
//    public Object instantiateItem(@NonNull ViewGroup container, int position) {
//        return super.instantiateItem(container, position);
//    }
//    @Override
//    public Parcelable saveState() {
//        return null;
//    }
//
//    @Override
//    public void restoreState(Parcelable state, ClassLoader loader) {
//
//    }
//
//    @Override
//    public void startUpdate(@NonNull ViewGroup container) {
//        super.startUpdate(container);
//    }
}