package com.kashapp;


import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private Button sign_up_account_button, forget_password_button;
    private FrameLayout sign_up_frame_layout;

    private EditText sign_in_password, sign_in_email;
    private Button sign_in_button;

    private AlertDialog dialog;

    private FirebaseAuth firebaseAuth;

    public SignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        sign_up_frame_layout = getActivity().findViewById(R.id.sign_up_frame_layout);

        // Custom dialog
        dialog = new SpotsDialog.Builder().setContext(getActivity()).setTheme(R.style.SignIn).build();

        sign_up_account_button = view.findViewById(R.id.sign_up_account_button);
        forget_password_button = view.findViewById(R.id.forget_password_button);

        sign_in_email = view.findViewById(R.id.sign_in_email);
        sign_in_password = view.findViewById(R.id.sign_in_password);

        sign_in_button = view.findViewById(R.id.sign_in_button);

        firebaseAuth = FirebaseAuth.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sign_up_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignUpFragment());
            }
        });

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();
            }
        });

        forget_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new ForgetPasswordFragment());
            }
        });

        // Edit text listener
        sign_in_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        sign_in_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkInputs();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void CheckEmailAndPassword() {
        String email = sign_in_email.getText().toString().trim();
        String password = sign_in_password.getText().toString().trim();

        Boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getActivity(), "Please fill in your email address!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern)) {
            Toast.makeText(getActivity(), "Please use a valid email address!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getActivity(), "Please fill in your password!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (password.length() > 32 || password.length() < 8) {
            Toast.makeText(getActivity(), "Password should be more than 8 and less than 32 characters in length!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (valid) {
            dialog.show();

            sign_in_button.setEnabled(false);
            sign_in_button.setBackgroundResource(R.color.disable);

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                sign_in_button.setEnabled(false);
                                sign_in_button.setBackgroundResource(R.color.disable);

                                SendUserToMainActivity();
                                dialog.dismiss();
                            } else {
                                sign_in_button.setEnabled(true);
                                sign_in_button.setBackgroundResource(R.color.colorPrimaryDark);
                                String message = task.getException().getMessage();
                                Toast.makeText(getActivity(), "Error occurred: " + message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_top, R.anim.slide_out_from_bottom);
        fragmentTransaction.replace(sign_up_frame_layout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(sign_in_email.getText())) {
            if (!TextUtils.isEmpty(sign_in_password.getText()) && sign_in_password.length() >= 8) {
                sign_in_button.setEnabled(true);
                sign_in_button.setBackgroundResource(R.color.colorPrimaryDark);
            } else {
                sign_in_button.setEnabled(false);
                sign_in_button.setBackgroundResource(R.color.disable);
            }
        } else {
            sign_in_button.setEnabled(false);
            sign_in_button.setBackgroundResource(R.color.disable);
        }
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        getActivity().finish();
    }
}
