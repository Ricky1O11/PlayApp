package com.games.playapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Map;


public class BgTabPageAdapter extends FragmentPagerAdapter {
    private String[] tab_titles = new String[]{"Basic Info", "Expansions", "Expands", "Matches played"};
    Context context;

    public BgTabPageAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0: return new BasicInfoFragment();
            case 1: return new BoardgameExpansionsFragment();
            case 2: return new BoardgameExpandsFragment();
            default: return new BoardgameMatchesFragment();
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
