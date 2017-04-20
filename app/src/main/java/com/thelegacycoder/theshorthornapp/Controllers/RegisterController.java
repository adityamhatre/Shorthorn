package com.thelegacycoder.theshorthornapp.Controllers;

import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.AsyncTasks.RegisterAsyncTask;
import com.thelegacycoder.theshorthornapp.Interfaces.AsyncCallback;

/**
 * Created by Aditya on 018, 18 Apr, 2017.
 */

public class RegisterController {
    public void register(String email, String password) {
        new RegisterAsyncTask(email, password, new AsyncCallback() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    if (response.equalsIgnoreCase(AppController.ERROR)) {
                        if (response.equalsIgnoreCase("Register: success")) {
                            AppController.getInstance().setLoggedIn(true);
                        } else {
                            AppController.getInstance().setLoggedIn(false);
                        }
                    }
                }
            }
        }).execute();
    }


    public static RegisterController newInstance() {
        return new RegisterController();
    }
}
