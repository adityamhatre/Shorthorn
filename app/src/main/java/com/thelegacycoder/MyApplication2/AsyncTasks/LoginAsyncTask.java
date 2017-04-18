package com.thelegacycoder.MyApplication2.AsyncTasks;

import android.os.AsyncTask;

import com.thelegacycoder.MyApplication2.Application.AppController;
import com.thelegacycoder.MyApplication2.Interfaces.AsyncCallback;

/**
 * Created by Aditya on 018, 18 Apr, 2017.
 */

public class LoginAsyncTask extends AsyncTask<Void, Void, String> {

    private String email, password;
    private AsyncCallback asyncCallback;

    public LoginAsyncTask(String email, String password, AsyncCallback asyncCallback) {
        this.email = email;
        this.password = password;
        this.asyncCallback = asyncCallback;
    }

    @Override
    protected String doInBackground(Void... voids) {
        return AppController.ERROR;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);
        asyncCallback.onResponse(response);
    }
}
