package com.thelegacycoder.theshorthornapp.Application;

import android.app.Application;

/**
 * Created by Aditya on 018, 18 Apr, 2017.
 */

public class AppController extends Application {
    public static final String ERROR = "ERROR";
    private static AppController appController;
    private boolean loggedIn;

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
    }

    public static synchronized AppController getInstance() {
        return appController;
    }


    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }
}
