package com.example.aliatsimactivation;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class ResellerPagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public ResellerPagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ResellerrechargeFragment();
            case 1: return new ResellerstatusFragment();
            case 2: return new ResellertestFragment();
            default:  return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

