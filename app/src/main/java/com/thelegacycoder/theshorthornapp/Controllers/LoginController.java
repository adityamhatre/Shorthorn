package com.thelegacycoder.theshorthornapp.Controllers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thelegacycoder.theshorthornapp.Activities.HomeActivity;
import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Fragments.LoginFragment;
import com.thelegacycoder.theshorthornapp.Models.User;

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
        AppController.getInstance().getmAuth().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (task.isSuccessful()) {
                            AppController.getInstance().getDatabase().getReference().child("users").child(AppController.getInstance().getmAuth().getCurrentUser().getUid()).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    AppController.getInstance().setUser(new User(dataSnapshot.getValue().toString().toLowerCase()));
                                    LoginFragment.loginCallback(true);
                                    AppController.getInstance().setLoggedIn(true);
                                    ((HomeActivity) context).loginCallback(true);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    throw databaseError.toException();
                                }
                            });
                        }

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            LoginFragment.loginCallback(false);
                        }
                    }
                });
    }

    public void login(String email, String password, Boolean callBack) {
        if (!callBack)
            AppController.getInstance().getmAuth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                            if (task.isSuccessful()) {
                                AppController.getInstance().getDatabase().getReference().child("users").child(AppController.getInstance().getmAuth().getCurrentUser().getUid()).child("type").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        AppController.getInstance().setUser(new User(dataSnapshot.getValue().toString().toLowerCase()));
                                        AppController.getInstance().setLoggedIn(true);
                                        ((HomeActivity) context).loginCallback(true);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        throw databaseError.toException();
                                    }
                                });

                            }

                            if (!task.isSuccessful()) {
                                Log.w(TAG, "signInWithEmail:failed", task.getException());

                            }
                        }
                    });
    }

    public boolean logout() {
        AppController.getInstance().getmAuth().signOut();
        AppController.getInstance().setLoggedIn(false);
        ((HomeActivity) context).logoutCallback();
        return true;
    }

    public static LoginController newInstance(Context context) {
        return new LoginController(context);
    }
}
