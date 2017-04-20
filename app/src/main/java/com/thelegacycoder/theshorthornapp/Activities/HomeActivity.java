package com.thelegacycoder.theshorthornapp.Activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.thelegacycoder.theshorthornapp.Fragments.HomeFragment;
import com.thelegacycoder.theshorthornapp.Fragments.LoginRegisterFragment;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.R;

public class HomeActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    DrawerLayout drawerLayout;
    Class currentFragment = LoginRegisterFragment.class;
    Toolbar toolbar;

    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();
        init_UI_Elements();
        setListeners();


        changeFragment(HomeFragment.newInstance("Welcome"));

    }

    private void setListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                closeDrawer();

                switch (item.getItemId()) {
                    case R.id.drawer_item_login:
                        changeFragment(LoginRegisterFragment.newInstance(0));
                        break;
                    case R.id.drawer_item_register:
                        changeFragment(LoginRegisterFragment.newInstance(1));
                        break;
                    case R.id.drawer_item_home:
                        changeFragment(HomeFragment.newInstance("Welcome"));
                        break;
                }

                return false;
            }
        });
    }

    void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
    }

    void init_UI_Elements() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.OPEN_DRAWER, R.string.CLOSE_DRAWER
        );

        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            actionBarDrawerToggle.syncState();
        }

        navigationView = (NavigationView) findViewById(R.id.nav_view);

    }

    void changeFragment(Fragment fragment) {
        //if (currentFragment != fragment.getClass()) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (fragmentTransaction != null) {
                fragmentTransaction.replace(R.id.container, fragment, "tag");
                fragmentTransaction.commit();
            }

            currentFragment = fragment.getClass();
        //}
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
        if (currentFragment != HomeFragment.class)
            menu.add("Help")
                    .setIcon(android.R.drawable.ic_menu_help)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
