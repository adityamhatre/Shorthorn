package com.thelegacycoder.theshorthornapp.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thelegacycoder.theshorthornapp.Adapters.SectionsPagerAdapter;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.R;

public class LoginRegisterFragment extends Fragment {
    private static final String ARG_MODE = "mode";

    OnFragmentInteractionListener onFragmentInteractionListener;
    ViewPager viewPager;
    TabLayout tabLayout;
    SectionsPagerAdapter sectionsPagerAdapter;

    int mode;

    public LoginRegisterFragment() {
    }

    public static LoginRegisterFragment newInstance(String param1, String param2) {
        LoginRegisterFragment fragment = new LoginRegisterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public static LoginRegisterFragment newInstance(int mode) { //mode: 0 => Login, mode: 1 => Register
        LoginRegisterFragment fragment = new LoginRegisterFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_MODE, mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login_register, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mode = getArguments().getInt(ARG_MODE);

        viewPager = (ViewPager) view.findViewById(R.id.container);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);


        viewPager.setAdapter(sectionsPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);

        tabLayout.getTabAt(mode).select();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            onFragmentInteractionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onFragmentInteractionListener = null;
    }
}
