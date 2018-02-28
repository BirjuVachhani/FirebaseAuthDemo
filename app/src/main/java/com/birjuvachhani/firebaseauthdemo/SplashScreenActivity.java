package com.birjuvachhani.firebaseauthdemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreenActivity extends AppCompatActivity implements OnCompleteListener<Void> {

    @BindView(R.id.progress_bar)
    ProgressBar mprogressBar;

    FirebaseAuth mauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);

        mprogressBar.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.background), android.graphics.PorterDuff.Mode.MULTIPLY);
        mauth = FirebaseAuth.getInstance();

        authenticateUser();
    }

    void authenticateUser() {
        mprogressBar.setVisibility(View.VISIBLE);
        FirebaseUser user = mauth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(this);
        }
        else
        {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onComplete(@NonNull Task<Void> task) {
        mprogressBar.setVisibility(View.INVISIBLE);
        if (!task.isSuccessful() || mauth.getCurrentUser()==null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else if(mauth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
