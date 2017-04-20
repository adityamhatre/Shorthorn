package com.thelegacycoder.theshorthornapp.Application;

import android.app.Application;
import android.content.Context;

/**
 * Created by Aditya on 018, 18 Apr, 2017.
 */

public class AppController extends Application {
    public static final String ERROR = "ERROR";
    private static AppController appController;
    private boolean loggedIn;

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        appController = this;
        context = this;
    }

    public static synchronized AppController getInstance() {
        return appController;
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
