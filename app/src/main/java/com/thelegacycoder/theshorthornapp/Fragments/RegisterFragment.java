package com.thelegacycoder.theshorthornapp.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.thelegacycoder.theshorthornapp.Controllers.RegisterController;
import com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener;
import com.thelegacycoder.theshorthornapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.thelegacycoder.theshorthornapp.Interfaces.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private String email = "", password = "", confirmPassword = "";

    private Button registerButton;
    private EditText emailInput, passwordInput, confirmPasswordInput;
    private RegisterController registerController;

    private View shader;
    private OnFragmentInteractionListener mListener;


    //private View rootView;


    public RegisterFragment() {
        // Required empty public constructor
    }


    public static RegisterFragment newInstance() {
        return new RegisterFragment();
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // rootView = view;
        init(view);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = emailInput.getText().toString().trim();
                password = passwordInput.getText().toString().trim();
                confirmPassword = confirmPasswordInput.getText().toString().trim();
                displayLoading(3);
                registerController.register(email, password);


            }
        });
    }

    private void displayLoading(int countDown) {
        Handler h = new Handler();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                emailInput.setEnabled(true);
                passwordInput.setEnabled(true);
                shader.setVisibility(View.GONE);
                registerButton.setBackgroundTintList(getResources().getColorStateList(R.color.green));
                registerButton.setText("Success");
            }
        };

        emailInput.setEnabled(false);
        passwordInput.setEnabled(false);
        shader.setVisibility(View.VISIBLE);

        h.postDelayed(r, countDown * 1000);
    }

    private void init(View view) {
        registerController = RegisterController.newInstance();

        registerButton = (Button) view.findViewById(R.id.btn_register);
        emailInput = (EditText) view.findViewById(R.id.input_email);
        passwordInput = (EditText) view.findViewById(R.id.input_password);
        confirmPasswordInput = (EditText) view.findViewById(R.id.input_confirm_password);

        shader = view.findViewById(R.id.shader);

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
}
