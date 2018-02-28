package com.birjuvachhani.firebaseauthdemo;

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
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.et_password)
    EditText memail;

    @BindView(R.id.btn_reset_password)
    Button mresetPassword;

    @BindView(R.id.tv_back)
    TextView mback;

    @BindView(R.id.progress_bar)
    ProgressBar mprogressBar;

    @BindView(R.id.tv_reset_msg)
    TextView mresetMsg;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        ButterKnife.bind(this);

        mresetPassword.setOnClickListener(this);
        mback.setOnClickListener(this);

        mauth = FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_reset_password: {
                sendResetEmail();
                break;
            }
            case R.id.tv_back: {
                finish();
                break;
            }
        }
    }

    private void sendResetEmail() {
        mresetMsg.setVisibility(View.INVISIBLE);
        String email = memail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            showMessage("Please enter email address");
            return;
        }
        mprogressBar.setVisibility(View.VISIBLE);
        mauth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mprogressBar.setVisibility(View.INVISIBLE);
                if (!task.isSuccessful()) {
                    showMessage("unable to send email, Please verify Email Id!");
                    mresetMsg.setVisibility(View.INVISIBLE);
                    return;
                }
                mresetMsg.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
