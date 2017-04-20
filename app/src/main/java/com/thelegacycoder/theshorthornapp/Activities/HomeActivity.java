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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Fragments.HomeFragment;
import com.thelegacycoder.theshorthornapp.Fragments.LoginFragment;
import com.thelegacycoder.theshorthornapp.Fragments.LoginRegisterFragment;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.R;

public class HomeActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    private DrawerLayout drawerLayout;
    private Class currentFragment = LoginRegisterFragment.class;
    private Toolbar toolbar;

    private static NavigationView navigationView;
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();
        init_UI_Elements();
        setListeners();


        changeFragment(HomeFragment.newInstance("Welcome"));

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(currentFragment.getSimpleName(), "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(currentFragment.getSimpleName(), "onAuthStateChanged:signed_out");
                }
            }
        };

    }

    private void setListeners() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                closeDrawer();

                switch (item.getItemId()) {
                    case R.id.drawer_item_login:
                        if (!AppController.getInstance().isLoggedIn())
                            changeFragment(LoginRegisterFragment.newInstance(0));
                        else
                            Toast.makeText(HomeActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.drawer_item_register:
                        if (!AppController.getInstance().isLoggedIn())
                            changeFragment(LoginRegisterFragment.newInstance(1));
                        else
                            Toast.makeText(HomeActivity.this, "Already logged in", Toast.LENGTH_SHORT).show();
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
        if (currentFragment != HomeFragment.class) {
            menu.add("Help")
                    .setIcon(android.R.drawable.ic_menu_help)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            if (AppController.getInstance().isLoggedIn())
                menu.add("Logout").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        LoginFragment.getLoginController().logout();
                        return false;
                    }
                }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static NavigationView getNavigationView() {
        return navigationView;
    }
}
