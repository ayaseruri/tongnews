package org.x.tongnews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;


/**
 * Created by ayaseruri on 15/8/1.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> mFragmentList;
    private String[] mTitleList;

    public FragmentAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentList, String[] titleList){
        super(fragmentManager);
        mFragmentList = fragmentList;
        mTitleList = titleList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList[position];
    }
}
