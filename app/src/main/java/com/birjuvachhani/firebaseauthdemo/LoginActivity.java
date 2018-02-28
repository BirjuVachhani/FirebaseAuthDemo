package com.birjuvachhani.firebaseauthdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_email)
    EditText memail;

    @BindView(R.id.et_password)
    EditText mpassword;

    @BindView(R.id.btn_login)
    Button mlogin;

    @BindView(R.id.tv_forgot_password)
    TextView mforgotPassword;

    @BindView(R.id.tv_register)
    TextView mregister;

    @BindView(R.id.progress_bar)
    ProgressBar mprogressBar;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mlogin.setOnClickListener(this);
        mforgotPassword.setOnClickListener(this);
        mregister.setOnClickListener(this);

        mauth = FirebaseAuth.getInstance();
        if (mauth.getCurrentUser() != null) {
            mauth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        if (mauth.getCurrentUser() != null) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else showMessage("Null");
                    }
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                loginUser();
                break;
            }
            case R.id.tv_forgot_password: {
                Intent intent = new Intent(this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_register: {
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    private void loginUser() {
        String email = memail.getText().toString();
        String password = mpassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            showMessage("Please Fill all fields!");
            return;
        }
        mprogressBar.setVisibility(View.VISIBLE);
        mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                mprogressBar.setVisibility(View.INVISIBLE);
                if (!task.isSuccessful()) {
                    showMessage("Unable to login, check credentials!");
                    return;
                }
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
