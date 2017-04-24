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
import com.thelegacycoder.theshorthornapp.Fragments.RegisterFragment;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by Aditya on 018, 18 Apr, 2017.
 */

public class RegisterController {
    private Context context;

    public RegisterController(Context context) {
        this.context = context;
    }


    public void register(String email, String password, final String loginType) {
        AppController.getInstance().getmAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (task.isSuccessful()) {

                    AppController.getInstance().getDatabase().getReference("users").child(task.getResult().getUser().getUid()).child("type").setValue(loginType);

                    AppController.getInstance().getDatabase().getReference("users").child(task.getResult().getUser().getUid()).child("user").setValue(task.getResult().getUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                RegisterFragment.registerCallback(true);
                                AppController.getInstance().setLoggedIn(true);
                                ((HomeActivity) context).loginCallback(true);
                            } else {
                                System.out.println("failed");
                                RegisterFragment.registerCallback(false);
                            }
                        }
                    });

                }
                if (!task.isSuccessful()) {
                    System.out.println("failed");
                    RegisterFragment.registerCallback(false);
                }
            }
        });
    }


    public static RegisterController newInstance(Context context) {
        return new RegisterController(context);
    }
}
