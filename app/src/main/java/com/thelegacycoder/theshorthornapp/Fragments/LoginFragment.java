package com.thelegacycoder.theshorthornapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.thelegacycoder.theshorthornapp.Application.AppController;
import com.thelegacycoder.theshorthornapp.Controllers.LoginController;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.R;

public class LoginFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String email = "", password = "";


    private static Button loginButton;
    private static EditText emailInput, passwordInput;
    private static LoginController loginController;

    private static View shader;
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, "");
        args.putString(ARG_PARAM2, "");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();

                displayLoading();
                loginController.login(email, password);


            }
        });

    }


    private void displayLoading() {
        emailInput.setEnabled(false);
        passwordInput.setEnabled(false);
        shader.setVisibility(View.VISIBLE);

    }

    private static void hideLoading() {
        emailInput.setEnabled(true);
        passwordInput.setEnabled(true);
        shader.setVisibility(View.GONE);
    }

    public static void loginCallback(boolean loginResult) {
        hideLoading();
        if (loginResult) {
            loginButton.setBackgroundTintList(AppController.getInstance().getContext().getResources().getColorStateList(R.color.green));
            loginButton.setText("Login successful");
        } else {
            loginButton.setBackgroundTintList(AppController.getInstance().getContext().getResources().getColorStateList(R.color.red));
            loginButton.setText("Error Occured");
        }

    }

    private void init(View view) {
        loginController = LoginController.newInstance(getActivity());

        loginButton = (Button) view.findViewById(R.id.btn_register);
        emailInput = (EditText) view.findViewById(R.id.input_email);
        passwordInput = (EditText) view.findViewById(R.id.input_password);

        shader = view.findViewById(R.id.shader);
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public static LoginController getLoginController() {
        return loginController;
    }
}
