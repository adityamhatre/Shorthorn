package com.thelegacycoder.theshorthornapp.Controllers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.thelegacycoder.theshorthornapp.Activities.HomeActivity;
import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Fragments.LoginFragment;
import com.thelegacycoder.theshorthornapp.R;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Aditya on 018, 18 Apr, 2017.
 */

public class LoginController {
    private Context context;

    public LoginController(Context context) {
        this.context = context;
    }

    public void login(String email, String password) {
        HomeActivity.getmAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            LoginFragment.loginCallback(true);
                            AppController.getInstance().setLoggedIn(true);
                            if (HomeActivity.getNavigationView() != null) {
                                HomeActivity.getNavigationView().getMenu().removeItem(1);
                                HomeActivity.getNavigationView().getMenu().removeItem(2);
                            }
                        }

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            LoginFragment.loginCallback(false);
                        }
                    }
                });
    }

    public boolean logout() {
        HomeActivity.getmAuth().signOut();
        AppController.getInstance().setLoggedIn(false);
        HomeActivity.getNavigationView().getMenu().clear();
        HomeActivity.getNavigationView().inflateMenu(R.menu.nav_items);


        //TODO: this is not yet tested...baghun ghe device var run karun
        //TODO: ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓ ↓
        HomeActivity.getNavigationView().setCheckedItem(0);

        return true;
    }

    public static LoginController newInstance(Context context) {
        return new LoginController(context);
    }
}
