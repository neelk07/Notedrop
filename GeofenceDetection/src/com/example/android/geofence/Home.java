package com.example.android.geofence;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import com.example.android.geofence.home_fragments.AddFriend;
import com.example.android.geofence.home_fragments.MyNotes;

/**
 * Created by neel on 4/13/14.
 */
public class Home extends FragmentActivity {

    ViewPager viewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        viewPager = (ViewPager) findViewById(R.id.viewpager_home);
        viewPager.setAdapter( new FragmentAdapter( getSupportFragmentManager() ) );
    }


    private class FragmentAdapter extends FragmentStatePagerAdapter {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return new MyNotes();
                case 1:
                    return new AddFriend();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}