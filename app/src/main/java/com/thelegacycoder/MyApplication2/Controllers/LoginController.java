package com.thelegacycoder.MyApplication2.Controllers;

import com.thelegacycoder.MyApplication2.Application.AppController;
import com.thelegacycoder.MyApplication2.AsyncTasks.LoginAsyncTask;
import com.thelegacycoder.MyApplication2.Interfaces.AsyncCallback;

/**
 * Created by Aditya on 018, 18 Apr, 2017.
 */

public class LoginController {
    public void login(String email, String password) {
        new LoginAsyncTask(email, password, new AsyncCallback() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    if (response.equalsIgnoreCase(AppController.ERROR)) {
                        if (response.equalsIgnoreCase("login: success")) {
                            AppController.getInstance().setLoggedIn(true);
                        } else {
                            AppController.getInstance().setLoggedIn(false);
                        }
                    }
                }
            }
        }).execute();
    }

    public boolean logout() {
        AppController.getInstance().setLoggedIn(false);
        return true;
    }

    public static LoginController newInstance() {
        return new LoginController();
    }
}
