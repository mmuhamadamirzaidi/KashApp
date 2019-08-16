package com.kashapp;


import android.app.AlertDialog;
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
public class ForgetPasswordFragment extends Fragment {

    private FrameLayout sign_up_frame_layout;

    private EditText forget_email;
    private Button forget_reset_button;

    private AlertDialog dialog;

    private FirebaseAuth firebaseAuth;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forget_password, container, false);

        // Custom dialog
        dialog = new SpotsDialog.Builder().setContext(getActivity()).setTheme(R.style.ForgetPassword).build();

        sign_up_frame_layout = getActivity().findViewById(R.id.sign_up_frame_layout);

        forget_email = view.findViewById(R.id.forget_email);

        forget_reset_button = view.findViewById(R.id.forget_reset_button);

        firebaseAuth = FirebaseAuth.getInstance();

        forget_reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckEmailAndPassword();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Edit text listener
        forget_email.addTextChangedListener(new TextWatcher() {
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
        String email = forget_email.getText().toString().trim();

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
        if (valid) {
            dialog.show();

            forget_reset_button.setEnabled(false);
            forget_reset_button.setBackgroundResource(R.color.disable);

            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                forget_reset_button.setEnabled(false);
                                forget_reset_button.setBackgroundResource(R.color.disable);

                                Toast.makeText(getActivity(), "Email sent successfully! Check your inbox.", Toast.LENGTH_SHORT).show();

                                SendUserToSignInActivity();
                                dialog.dismiss();
                            } else {
                                forget_reset_button.setEnabled(true);
                                forget_reset_button.setBackgroundResource(R.color.colorPrimaryDark);
                                String message = task.getException().getMessage();
                                Toast.makeText(getActivity(), "Error occurred: " + message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void SendUserToSignInActivity() {
        setFragment(new SignInFragment());
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(forget_email.getText())) {
            forget_reset_button.setEnabled(true);
            forget_reset_button.setBackgroundResource(R.color.colorPrimaryDark);
        } else {
            forget_reset_button.setEnabled(false);
            forget_reset_button.setBackgroundResource(R.color.disable);
        }
    }

    // Set after finished activity reset password
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_out_from_top);
        fragmentTransaction.replace(sign_up_frame_layout.getId(), fragment);
        fragmentTransaction.commit();
    }
}
