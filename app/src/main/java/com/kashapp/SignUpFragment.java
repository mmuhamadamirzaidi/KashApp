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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {

    private Button sign_in_account_button, forget_password_button;
    private FrameLayout sign_up_frame_layout;

    private EditText sign_up_full_name, sign_up_email, sign_up_password, sign_up_confirm_password;
    private Button sign_up_button;

    private AlertDialog dialog;

    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        // Custom dialog
        dialog = new SpotsDialog.Builder().setContext(getActivity()).setTheme(R.style.Creating).build();

        sign_in_account_button = view.findViewById(R.id.sign_in_account_button);
        forget_password_button = view.findViewById(R.id.forget_password_button);

        sign_up_frame_layout = getActivity().findViewById(R.id.sign_up_frame_layout);

        sign_up_full_name = view.findViewById(R.id.sign_up_full_name);
        sign_up_email = view.findViewById(R.id.sign_up_email);
        sign_up_password = view.findViewById(R.id.sign_up_password);
        sign_up_confirm_password = view.findViewById(R.id.sign_up_confirm_password);

        sign_up_button = view.findViewById(R.id.sign_up_button);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Bottom button
        sign_in_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFragment(new SignInFragment());
            }
        });

        forget_password_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to forget password page
            }
        });

        // Sign up button
        sign_up_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Upload data to Firebase
                CreateNewAccount();
            }
        });

        // Edit text listener
        sign_up_full_name.addTextChangedListener(new TextWatcher() {
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

        sign_up_email.addTextChangedListener(new TextWatcher() {
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

        sign_up_password.addTextChangedListener(new TextWatcher() {
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

        sign_up_confirm_password.addTextChangedListener(new TextWatcher() {
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

    private void CreateNewAccount() {
        final String fullname = sign_up_full_name.getText().toString().trim();
        String email = sign_up_email.getText().toString().trim();
        String password = sign_up_password.getText().toString().trim();
        String confirmPassword = sign_up_confirm_password.getText().toString().trim();

        Boolean valid = true;

        if (TextUtils.isEmpty(fullname))
        {
            Toast.makeText(getActivity(), "Please fill in your full name!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(getActivity(), "Please fill in your email address!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (!email.matches(emailPattern ))
        {
            Toast.makeText(getActivity(), "Please use a valid email address!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(getActivity(), "Please fill in your password!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
//        String upperCaseChars = "(.*[A-Z].*)";
//        if (!password.matches(upperCaseChars ))
//        {
//            Toast.makeText(getActivity(), "Password should contain at least one upper case alphabet!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
//        String lowerCaseChars = "(.*[a-z].*)";
//        if (!password.matches(lowerCaseChars ))
//        {
//            Toast.makeText(getActivity(), "Password should contain at least one lower case alphabet!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
        if (password.length() > 32 || password.length() < 8)
        {
            Toast.makeText(getActivity(), "Password should be more than 8 and less than 32 characters in length!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
//        String numbers = "(.*[0-9].*)";
//        if (!password.matches(numbers ))
//        {
//            Toast.makeText(getActivity(), "Password should contain at least one number!", Toast.LENGTH_SHORT).show();
//            valid = false;
//        }
        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!password.matches(specialChars ))
        {
            Toast.makeText(getActivity(), "Password should contain at least one special character!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (TextUtils.isEmpty(confirmPassword))
        {
            Toast.makeText(getActivity(), "Please fill in your confirm password!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if (!confirmPassword.equals(password))
        {
            Toast.makeText(getActivity(), "Password do not match!", Toast.LENGTH_SHORT).show();
            valid = false;
        }
        if(valid)
        {
            dialog.show();

            firebaseAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful())
                            {
                                Map<Object, String> userdata = new HashMap<>();
                                userdata.put("fullname", fullname);

                                firebaseFirestore.collection("Users").add(userdata).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        if (task.isSuccessful())
                                        {
                                            sign_up_button.setEnabled(false);
                                            sign_up_button.setBackgroundResource(R.color.disable);

                                            SendUserToMainActivity();
                                            dialog.dismiss();
                                        }else{
                                            sign_up_button.setEnabled(true);
                                            sign_up_button.setBackgroundResource(R.color.colorPrimaryDark);
                                            String message = task.getException().getMessage();
                                            Toast.makeText(getActivity(), "Error occurred: "+message, Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }
                                    }
                                });
                            }
                            else
                            {
                                sign_up_button.setEnabled(true);
                                sign_up_button.setBackgroundResource(R.color.colorPrimaryDark);
                                String message = task.getException().getMessage();
                                Toast.makeText(getActivity(), "Error occurred: "+message, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }
                    });
        }
    }

    private void checkInputs() {
        if (!TextUtils.isEmpty(sign_up_full_name.getText())) {
            if (!TextUtils.isEmpty(sign_up_email.getText())) {
                if (!TextUtils.isEmpty(sign_up_password.getText()) && sign_up_password.length() >= 8) {
                    if (!TextUtils.isEmpty(sign_up_confirm_password.getText())) {
                        sign_up_button.setEnabled(true);
                        sign_up_button.setBackgroundResource(R.color.colorPrimaryDark);
                    } else {
                        sign_up_button.setEnabled(false);
                        sign_up_button.setBackgroundResource(R.color.disable);
                    }
                } else {
                    sign_up_button.setEnabled(false);
                    sign_up_button.setBackgroundResource(R.color.disable);
                }
            } else {
                sign_up_button.setEnabled(false);
                sign_up_button.setBackgroundResource(R.color.disable);
            }
        } else {
            sign_up_button.setEnabled(false);
            sign_up_button.setBackgroundResource(R.color.disable);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_from_bottom, R.anim.slide_out_from_top);
        fragmentTransaction.replace(sign_up_frame_layout.getId(), fragment);
        fragmentTransaction.commit();
    }

    private void SendUserToMainActivity() {
        Intent mainIntent = new Intent(getActivity(), MainActivity.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mainIntent);
        getActivity().finish();
    }
}
