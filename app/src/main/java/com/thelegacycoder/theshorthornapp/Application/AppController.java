package com.thelegacycoder.theshorthornapp.Application;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by Aditya on 018, 18 Apr, 2017.
 */

public class AppController extends Application {
    public static final String ERROR = "ERROR";
    private static AppController appController;
    private boolean loggedIn;
    private static FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseDatabase database;
    private StorageReference mstorageReference;

    private static Context context;

    public FirebaseDatabase getDatabase() {
        return database;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
        context = this;
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mstorageReference = FirebaseStorage.getInstance().getReference();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("App", "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d("App", "onAuthStateChanged:signed_out");
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);

    }

    public FirebaseAuth getmAuth() {
        return mAuth;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    public static synchronized AppController getInstance() {
        return appController;
    }


    public StorageReference getStorageReference() {
        return mstorageReference;
    }

    public Context getContext() {
        return context;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;

    }

}
