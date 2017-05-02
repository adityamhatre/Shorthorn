package com.thelegacycoder.theshorthornapp.Activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Controllers.LoginController;
import com.thelegacycoder.theshorthornapp.Fragments.AddArticleFragment;
import com.thelegacycoder.theshorthornapp.Fragments.HomeFragment;
import com.thelegacycoder.theshorthornapp.Fragments.LoginFragment;
import com.thelegacycoder.theshorthornapp.Fragments.LoginRegisterFragment;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.R;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements OnFragmentInteractionListener {


    private DrawerLayout drawerLayout;
    private Class currentFragment = LoginRegisterFragment.class;
    private Toolbar toolbar;

    private static NavigationView navigationView;


    boolean dev = true;
    LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initToolbar();
        init_UI_Elements();
        setListeners();


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12321);
        }

        if (dev) {
            loginController = LoginController.newInstance(this);
            loginController.login("d@d.com", "asdfasdf", false);
        }


        changeFragment(HomeFragment.newInstance("reader"));


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 12321:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12321);
                    }
                break;
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 12321);
                }
                break;
        }
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
                        changeFragment(HomeFragment.newInstance("reader"));
                        break;
                    case R.id.view_my_articles:
                        changeFragment(HomeFragment.newInstance("writer"));
                        break;
                    case R.id.delete_articles:
                        changeFragment(HomeFragment.newInstance("editor-delete"));
                        break;
                    case R.id.publish_articles:
                        changeFragment(HomeFragment.newInstance("editor-publish"));
                        break;
                    case R.id.add_categories:
                        add_category();
                        break;
                    case R.id.delete_categories:
                        delete_category();
                        break;
                }

                return false;
            }
        });
    }

    AlertDialog addCat, deleteCat;

    void add_category() {
        View view = getLayoutInflater().inflate(R.layout.dialog_add_category, null);
        addCat = new AlertDialog.Builder(this).setView(view).create();
        addCat.show();
        final EditText nc = (EditText) view.findViewById(R.id.newCat);
        view.findViewById(R.id.submit_cat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (addCat.isShowing()) {
                    if (!categories.contains(nc.getText().toString().trim())) {
                        AppController.getInstance().getDatabase().getReference("categories").child("category" + (categories.size() + 1)).setValue(nc.getText().toString().trim());
                    }
                    addCat.cancel();
                }
                Toast.makeText(HomeActivity.this, "Category added!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    void delete_category() {
        final View view = getLayoutInflater().inflate(R.layout.filter_dialog, null);

        final ListView categoryListView = (ListView) view.findViewById(R.id.category_list);
        categoryListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, categories));
        categoryListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        deleteCat = new AlertDialog.Builder(this).setView(view).setNegativeButton("Clear Filters", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ;
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int ii) {

                final ArrayList<String> selectedCategories = new ArrayList<String>();
                selectedCategories.clear();
                int len = categoryListView.getCount();
                SparseBooleanArray checked = categoryListView.getCheckedItemPositions();
                for (int i = 0; i < len; i++)
                    if (checked.get(i)) {
                        String item = categories.get(i);
  /* do whatever you want with the checked item */
                        selectedCategories.add(item);
                    }
                System.out.println(selectedCategories.toString());
                deleteCat.cancel();
                if (selectedCategories.size() > 0) {

                    AppController.getInstance().getDatabase().getReference("categories").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                if (selectedCategories.contains(snapshot.getValue(String.class))) {
                                    snapshot.getRef().setValue(null);

                                }
                            }
                            Toast.makeText(HomeActivity.this, "Category Removed", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

            }

        }).create();
        deleteCat.show();

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

    public void loginCallback(boolean loginResult) {
        if (loginResult) {
            navigationView.getMenu().getItem(0).setChecked(true);
            navigationView.getMenu().findItem(R.id.drawer_item_login).setVisible(false);
            navigationView.getMenu().findItem(R.id.drawer_item_register).setVisible(false);
            if (AppController.getInstance().getUser().getType().equalsIgnoreCase("writer")) {
                navigationView.getMenu().findItem(R.id.view_my_articles).setVisible(true);
                navigationView.getMenu().findItem(R.id.delete_categories).setVisible(false);
                navigationView.getMenu().findItem(R.id.add_categories).setVisible(false);
            }
            if (AppController.getInstance().getUser().getType().equalsIgnoreCase("editor")) {
                navigationView.getMenu().findItem(R.id.publish_articles).setVisible(true);
                navigationView.getMenu().findItem(R.id.delete_articles).setVisible(true);
                navigationView.getMenu().findItem(R.id.delete_categories).setVisible(false);
                navigationView.getMenu().findItem(R.id.add_categories).setVisible(false);
            }
            if (AppController.getInstance().getUser().getType().equalsIgnoreCase("admin")) {
                navigationView.getMenu().findItem(R.id.publish_articles).setVisible(true);
                navigationView.getMenu().findItem(R.id.delete_articles).setVisible(true);
                navigationView.getMenu().findItem(R.id.delete_categories).setVisible(true);
                navigationView.getMenu().findItem(R.id.add_categories).setVisible(true);
            }

            changeFragment(HomeFragment.newInstance("reader"));
            invalidateOptionsMenu();

            categories = new ArrayList<>();

            AppController.getInstance().getDatabase().getReference("categories").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    categories.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        categories.add(snapshot.getValue(String.class));
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {
        }

    }

    ArrayList<String> categories;
    AlertDialog alertDialog;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment != HomeFragment.class) {
            menu.add("Help")
                    .setIcon(android.R.drawable.ic_menu_help)
                    .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        if (AppController.getInstance().isLoggedIn()) {
            menu.add("Logout").setIcon(android.R.drawable.btn_minus).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this).setTitle("Confirm").setMessage("Are you sure to logout ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (dev)
                                loginController.logout();
                            else LoginFragment.getLoginController().logout();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (alertDialog.isShowing()) alertDialog.cancel();
                        }
                    });

                    alertDialog = builder.create();
                    alertDialog.show();

                    return false;
                }
            }).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            if (AppController.getInstance().getUser().getType().equalsIgnoreCase("writer")) {
                menu.add("Add new article").setIcon(android.R.drawable.ic_menu_add)
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                changeFragment(AddArticleFragment.newInstance("", ""));
                                return false;
                            }
                        });
            }
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        if (currentFragment == AddArticleFragment.class) {
            changeFragment(HomeFragment.newInstance("reader"));
        } else super.onBackPressed();
    }

    public static NavigationView getNavigationView() {
        return navigationView;
    }

    public void logoutCallback() {
        navigationView.getMenu().findItem(R.id.drawer_item_login).setVisible(true);
        navigationView.getMenu().findItem(R.id.drawer_item_register).setVisible(true);
        navigationView.getMenu().findItem(R.id.view_my_articles).setVisible(false);
        navigationView.getMenu().findItem(R.id.publish_articles).setVisible(false);
        navigationView.getMenu().findItem(R.id.delete_articles).setVisible(false);
        navigationView.getMenu().findItem(R.id.drawer_add_article).setVisible(false);
        navigationView.getMenu().findItem(R.id.add_categories).setVisible(false);
        navigationView.getMenu().findItem(R.id.delete_categories).setVisible(false);
        invalidateOptionsMenu();
        changeFragment(HomeFragment.newInstance("Logout"));
    }
}
