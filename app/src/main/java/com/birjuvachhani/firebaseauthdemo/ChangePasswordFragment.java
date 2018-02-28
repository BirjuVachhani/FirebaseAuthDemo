package com.birjuvachhani.firebaseauthdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by birju.vachhani on 28/02/18.
 */

public class ChangePasswordFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.btn_change)
    Button mchangePassword;

    @BindView(R.id.et_password)
    EditText mpassword;

    @BindView(R.id.et_confirm_password)
    EditText mconfirmPassword;

    @BindView(R.id.ti_password)
    TextInputLayout mpasswordLayout;

    @BindView(R.id.ti_confirm_password)
    TextInputLayout mconfirmPasswordLayout;

    @BindView(R.id.progress_bar)
    ProgressBar mprogressBar;

    FirebaseAuth mauth;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_password_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        mchangePassword.setOnClickListener(this);
        mauth = FirebaseAuth.getInstance();

        mpassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mpasswordLayout.setError(null);
                    mpasswordLayout.setErrorEnabled(false);
                    mpasswordLayout.setHintTextAppearance(R.style.TextNormal);

                } else if (s.length() < 8) {
                    mpasswordLayout.setErrorEnabled(true);
                    mpasswordLayout.setError("Password too short!");
                    mpasswordLayout.setHintTextAppearance(R.style.TextError);
                } else {
                    mpasswordLayout.setError(null);
                    mpasswordLayout.setErrorEnabled(false);
                    mpasswordLayout.setHintTextAppearance(R.style.TextSuccess);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        mconfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    mconfirmPassword.setError(null);
                    mconfirmPasswordLayout.setErrorEnabled(false);
                    mconfirmPasswordLayout.setHintTextAppearance(R.style.TextNormal);
                } else if (!mpassword.getText().toString().equals(s.toString())) {
                    mconfirmPasswordLayout.setErrorEnabled(true);
                    mconfirmPasswordLayout.setError("Password doesn't match!");
                    mconfirmPasswordLayout.setHintTextAppearance(R.style.TextError);
                } else {
                    mconfirmPasswordLayout.setError(null);
                    mconfirmPasswordLayout.setErrorEnabled(false);
                    mconfirmPasswordLayout.setHintTextAppearance(R.style.TextSuccess);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        String password = mpassword.getText().toString().trim();
        String confirmPassword = mconfirmPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            showMessage("Please enter password to change!");
            return;
        }
        if (!password.equals(confirmPassword)) {
            showMessage("Passwords doesn't match!");
            return;
        }
        mprogressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = mauth.getCurrentUser();
        if (user != null) {
            user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mprogressBar.setVisibility(View.INVISIBLE);
                    if (!task.isSuccessful()) {
                        showMessage("Unable to change password, try again!");
                        return;
                    }
                    showMessage("Password Successfully changed.");
                }
            });
        }
    }

    private void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }
}
