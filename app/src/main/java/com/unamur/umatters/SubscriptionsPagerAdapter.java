package com.unamur.umatters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class SubscriptionsPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public SubscriptionsPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                SubscriptionsBoxFragment tab1 = new SubscriptionsBoxFragment();
                return tab1;
            case 1:
                SubscriptionsListFragment tab2 = new SubscriptionsListFragment();
                return tab2;
            default:
                return null;
        }
    }
    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
