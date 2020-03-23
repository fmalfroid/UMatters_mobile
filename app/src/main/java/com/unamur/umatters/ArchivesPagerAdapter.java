package com.unamur.umatters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class ArchivesPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public ArchivesPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ArchivesAcceptedFragment tab1 = new ArchivesAcceptedFragment();
                return tab1;
            case 1:
                ArchivesPendingFragment tab2 = new ArchivesPendingFragment();
                return tab2;
            case 2:
                ArchivesRefusedFragment tab3 = new ArchivesRefusedFragment();
                return tab3;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
