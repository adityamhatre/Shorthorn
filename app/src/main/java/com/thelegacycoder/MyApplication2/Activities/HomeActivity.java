package com.thelegacycoder.MyApplication2.Activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.thelegacycoder.MyApplication2.Fragments.LoginFragment;
import com.thelegacycoder.MyApplication2.Fragments.LoginRegisterFragment;
import com.thelegacycoder.MyApplication2.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.MyApplication2.R;

public class HomeActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    DrawerLayout drawerLayout;
    Class currentFragment = LoginRegisterFragment.class;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();
        init_UI_Elements();

        changeFragment(LoginFragment.newInstance("init"));
        findViewById(R.id.b1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                changeFragment(LoginFragment.newInstance("aditya"));
            }
        });
        findViewById(R.id.b2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                changeFragment(LoginRegisterFragment.newInstance("aditya", "mhatre"));
            }
        });

    }

    void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.ADITYA_MHATRE, R.string.SANGEET_PUTHUR
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            actionBarDrawerToggle.syncState();
        }

    }

    void init_UI_Elements() {
    }

    void changeFragment(Fragment fragment) {
        if (currentFragment != fragment.getClass()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.container, fragment, "tag");
                fragmentTransaction.commit();
            }

            currentFragment = fragment.getClass();
        }
        closeDrawer();
        invalidateOptionsMenu();

    }


    void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    void closeDrawer() {
        if (drawerLayout != null) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment != LoginFragment.class)
            menu.add("Login")
                    .setIcon(android.R.drawable.ic_menu_search)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
