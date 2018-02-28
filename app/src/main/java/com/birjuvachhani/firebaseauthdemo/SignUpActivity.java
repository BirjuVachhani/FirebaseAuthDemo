package com.birjuvachhani.firebaseauthdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_email)
    EditText memail;

    @BindView(R.id.et_password)
    EditText mpassword;

    @BindView(R.id.et_confirm_password)
    EditText mconfirmPassword;

    @BindView(R.id.btn_register)
    Button mregister;

    @BindView(R.id.tv_login)
    TextView mlogin;

    @BindView(R.id.progress_bar)
    ProgressBar mprogressBar;

    @BindView(R.id.ti_password)
    TextInputLayout mpasswordLayout;

    @BindView(R.id.ti_confirm_password)
    TextInputLayout mconfirmPasswordLayout;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);

        mregister.setOnClickListener(this);
        mlogin.setOnClickListener(this);

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
        switch (v.getId()) {
            case R.id.btn_register: {
                registerUser();
                break;
            }
            case R.id.tv_login: {
                finish();
                break;
            }
        }
    }

    private void registerUser() {

        String email = memail.getText().toString();
        String password = mpassword.getText().toString();
        String confirmPassword = mconfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
            showMessage("Please fill all fields!");
            return;
        }

        if (password.length() < 8 || confirmPassword.length() < 8) {
            showMessage("Password too short!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showMessage("Passwords don't match!");
            return;
        }

        mprogressBar.setVisibility(View.VISIBLE);
        mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mprogressBar.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()) {
                    Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showMessage("Unable to create user, try again!");
                }
            }
        });

    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        mprogressBar.setVisibility(View.INVISIBLE);
        super.onResume();
    }
}
