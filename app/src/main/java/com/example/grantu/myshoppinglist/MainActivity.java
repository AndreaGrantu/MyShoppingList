package com.example.grantu.myshoppinglist;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.example.grantu.myshoppinglist.Utils.SectionsPagerAdapter;
import com.example.grantu.myshoppinglist.Utils.ToolbarActivity;


public class MainActivity extends ToolbarActivity implements android.app.ActionBar.TabListener {



    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private CharSequence[] tabTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);


        tabTitles = new CharSequence[] {
                getString(R.string.title_section_shop_items),
                getString(R.string.title_section_shop_history),};
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[0].toString().toUpperCase()));
        tabLayout.addTab(tabLayout.newTab().setText(tabTitles[1].toString().toUpperCase()));
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),tabTitles,tabTitles.length);


        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

    }

    @Override
    public void onTabReselected(android.app.ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
    }




}
