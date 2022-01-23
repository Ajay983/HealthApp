package com.linkitsoft.dategoal.ui.signup;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class pageradapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public pageradapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {

        switch (position) {

                case 0:
                EnterPhoneNumber homeFragment = new EnterPhoneNumber();
                return homeFragment;

                case 1:
                EnterOtp settingsFragment = new EnterOtp();
                return settingsFragment;

                case 2:

                EnterName EnterNamea = new EnterName();
                return EnterNamea;

                case 3:

                EnterBirthday EnterBirthdaya = new EnterBirthday();
                return EnterBirthdaya;

                case 4:

                EnterGender EnterGendera = new EnterGender();
                 return EnterGendera;

                case 5:

                EnterEthnicity EnterEthnicitya = new EnterEthnicity();
                return EnterEthnicitya;

                case 6:

                EnterSexuality EnterSexualitya = new EnterSexuality();
                return EnterSexualitya;

                case 7:

                EnterHerefor EnterHerefora = new EnterHerefor();
                return EnterHerefora;

                case 8:

                EnterReligon EnterReligona = new EnterReligon();
                return EnterReligona;

                case 9:

                EnterReligionYes EnterReligionYesa = new EnterReligionYes();
                return EnterReligionYesa;

                case 10:

                EnterTattoo EnterTattooa = new EnterTattoo();
                return EnterTattooa;

                case 11:

                EnterRGoal EnterRGoala = new EnterRGoal();
                return EnterRGoala;

                case 12:

                EnterPassion EnterPassiona = new EnterPassion();
                return EnterPassiona;

                case 13:

                EnterPhotos EnterPhotosa = new EnterPhotos();
                return EnterPhotosa;

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