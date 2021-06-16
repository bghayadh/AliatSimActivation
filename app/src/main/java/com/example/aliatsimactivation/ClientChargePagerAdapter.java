package com.example.aliatsimactivation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
public class ClientChargePagerAdapter extends FragmentPagerAdapter {
    private int numOfTabs;
    public ClientChargePagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs=numOfTabs;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return new ClientRechargefragment();
            case 1: return new ClientRechargeStatusfragment();
            case 2: return new ClientReportfragment();
            default:  return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }

}



