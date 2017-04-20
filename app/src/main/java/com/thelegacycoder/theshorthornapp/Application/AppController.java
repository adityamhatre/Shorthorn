package com.thelegacycoder.theshorthornapp.Application;

import android.app.Application;
import android.content.Context;

import com.thelegacycoder.theshorthornapp.Activities.HomeActivity;

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
        if (loggedIn)
            if (HomeActivity.getNavigationView() != null) {
                //:TODO remove login and register from navigation view once login !. :P
                /*
                * Note from past-Aditya to future-Aditya:
                * Hey you... kahi tari vichar karun ithe he statements taaklet..jasta v4 nako karu and ithech implement kar je var lihle
                * Aani anghol kar aaj :P
                * Chal bye.
                * */
                //HomeActivity.getNavigationView().
            }
    }

}
