package com.example.grantu.myshoppinglist.Utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.grantu.myshoppinglist.UI.Fragments.HistoryListsFragment;
import com.example.grantu.myshoppinglist.UI.Fragments.ShoppingItemsFragment;

/**
 * Created by Grantu on 03/09/2016.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {
    CharSequence tabTitles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int tabsNum; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is create
    public SectionsPagerAdapter(FragmentManager fm, CharSequence[] tabTitles, int length) {
        super(fm);
        this.tabTitles=tabTitles;
        this.tabsNum=length;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position){
            case 0:
                return new ShoppingItemsFragment();
            case 1:
                return new HistoryListsFragment();
            default:
                return new ShoppingItemsFragment();
        }

    }

    @Override
    public int getCount() {
        return tabsNum;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}
