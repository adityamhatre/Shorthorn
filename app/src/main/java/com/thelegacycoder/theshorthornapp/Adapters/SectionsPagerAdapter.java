package com.thelegacycoder.theshorthornapp.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.thelegacycoder.theshorthornapp.Fragments.LoginFragment;
import com.thelegacycoder.theshorthornapp.Fragments.RegisterFragment;

/**
 * Created by Aditya on 026, 26 Jan, 2017.
 */

public class SectionsPagerAdapter extends FragmentPagerAdapter {

    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //return HomeFragment.newInstance("Login");
                return LoginFragment.newInstance();
            case 1:
                return RegisterFragment.newInstance();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Login";
            case 1:
                return "Register";
        }
        return null;
    }
}
