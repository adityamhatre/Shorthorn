package com.thelogicalcoder.MyApplication2.Activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.thelogicalcoder.MyApplication2.Fragments.ExampleFragment;
import com.thelogicalcoder.MyApplication2.Interfaces.OnFragmentInteractionListener;
import com.thelogicalcoder.theshorthornapp.R;

public class MainActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        init_UI_Elements();

    }


    void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));


        setSupportActionBar(toolbar);
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.ADITYA_MHATRE, R.string.SHRADDHA_GHOGARE
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            actionBarDrawerToggle.syncState();
        }

    }

    void init_UI_Elements() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    }

    void changeFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, "tag");
        fragmentTransaction.commit();
    }


    void aditya(View v) {
        changeFragment(ExampleFragment.newInstance("aditya", "asdf"));
        closeDrawer();
    }

    void mhatre(View v) {
        changeFragment(ExampleFragment.newInstance("mhatre", "asdf"));
        closeDrawer();
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
    public void onFragmentInteraction(Uri uri) {

    }
}
