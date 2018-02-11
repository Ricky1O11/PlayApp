package com.games.playapp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;


public class MatchesTabPageAdapter extends FragmentPagerAdapter {
    private String[] tab_titles = new String[]{"All Matches", "Programmed Matches"};
    Context context;

    public MatchesTabPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new MatchesAllFragment();
            case 1: return new MatchesProgrammedFragment();
            default: return new MatchesAllFragment();
        }

    }

    @Override
    public int getCount() {
        return tab_titles.length;
    }

    public CharSequence getPageTitle(int position){
        return tab_titles[position];
    }

    @Override
    public int getItemPosition(Object item) {
        return POSITION_NONE;
    }
}
