package com.example.freesbet.bases;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabViewPagerAdapter extends FragmentPagerAdapter {

    private final List <Fragment> mFragmentList = new ArrayList<>();
    private final List <String> mFragmentTitulo= new ArrayList<>();

    public TabViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragmentList.get(i);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitulo.add(title);
    }

    public CharSequence getPageTitle(int position){
      return mFragmentTitulo.get(position);
    }
}
